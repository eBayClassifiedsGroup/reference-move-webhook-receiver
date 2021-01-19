//------------------------------------------------------------------
// Copyright 2021 mobile.de GmbH.
// Author/Developer: Philipp Bartsch
//
// This code is licensed under MIT license (see LICENSE for details)
//------------------------------------------------------------------
package org.example.move.webhookreceiver.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.move.webhookreceiver.rest.ListingEventIntegrationTest.readResourceFile;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ecg.move.sellermodel.promotion.PublicPromotionLargeModel;
import ecg.move.sellermodel.webhook.ListingUrl;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.example.move.IntegrationTestBase;
import org.example.move.webhookreceiver.rest.hmac.HmacChecker;
import org.example.move.webhookreceiver.rest.listingurl.ListingUrlEventEnvelope;
import org.example.move.webhookreceiver.rest.promotion.PromotionEventEnvelope;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

@Slf4j
class PromotionEventIntegrationTest extends IntegrationTestBase {

    @Autowired
    HmacChecker signatureChecker;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void promotion_can_be_posted() throws JsonProcessingException {
        // given
        PublicPromotionLargeModel promotion = aPromotionEvent().getPayload();

        //when
        ResponseEntity<?> response = postPromotionEvent(promotion);

        //then
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    private ResponseEntity<Void> postPromotionEvent(PublicPromotionLargeModel promotion)
        throws JsonProcessingException {

        PromotionEventEnvelope payload = PromotionEventEnvelope.builder()
            .payload(promotion)
            .eventType(WebhookEventType.PROMOTIONS)
            .timestamp(new Date())
            .build();

        String actualHmac = signatureChecker.createSignatureAsBase64String(objectMapper.writeValueAsString(payload));

        return rest.exchange(
            "/webhook/promotion",
            POST,
            new HttpEntity<>(
                payload,
                CollectionUtils.toMultiValueMap(
                    Map.of("signature", List.of(actualHmac))
                )
            ),
            Void.class);
    }

    private PromotionEventEnvelope aPromotionEvent() {

        try {
            TypeReference<PromotionEventEnvelope> event = new TypeReference<>() {
            };
            return objectMapper
                .readValue(readResourceFile("webhook/real-promotion-event.json"), event);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
