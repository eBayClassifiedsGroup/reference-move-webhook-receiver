//------------------------------------------------------------------
// Copyright 2020 mobile.de GmbH.
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
import ecg.move.sellermodel.dealer.DealerLogMessageV2;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.move.IntegrationTestBase;
import org.example.move.webhookreceiver.rest.hmac.HmacChecker;
import org.example.move.webhookreceiver.rest.model.EventWrapper;
import org.example.move.webhookreceiver.rest.model.WebhookEventType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

@SuppressWarnings( {"OptionalUsedAsFieldOrParameterType"})
@Slf4j
class DealerEventIntegrationTest extends IntegrationTestBase {

    @Autowired
    HmacChecker signatureChecker;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void dealer_update_can_be_posted() throws JsonProcessingException {
        // given
        DealerLogMessageV2 dealersEvent = aDealerEvent().getPayload();

        //when
        ResponseEntity<?> response = postDealerUpdate(dealersEvent, Optional.empty());

        //then
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    private ResponseEntity<Void> postDealerUpdate(DealerLogMessageV2 event, Optional<String> givenHmac)
        throws JsonProcessingException {

        EventWrapper payload = EventWrapper.builder()
            .payload(event)
            .eventType(WebhookEventType.DEALERS)
            .timestamp(new Date())
            .build();

        String actualHmac = signatureChecker.createSignatureAsBase64String(objectMapper.writeValueAsString(payload));

        return rest.exchange(
            "/webhook/dealer",
            POST,
            new HttpEntity<>(
                payload,
                CollectionUtils.toMultiValueMap(
                    Map.of("signature", List.of(givenHmac.orElse(actualHmac)))
                )
            ),
            Void.class);
    }

    private EventWrapper<DealerLogMessageV2> aDealerEvent() {

        try {
            TypeReference<EventWrapper<DealerLogMessageV2>> eventWrapperTypeReference = new TypeReference<>() {
            };
            return objectMapper
                .readValue(readResourceFile("webhook/real-dealers-event.json"), eventWrapperTypeReference);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
