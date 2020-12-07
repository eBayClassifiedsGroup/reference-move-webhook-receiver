//------------------------------------------------------------------
// Copyright 2020 mobile.de GmbH.
// Author/Developer: Philipp Bartsch
//
// This code is licensed under MIT license (see LICENSE for details)
//------------------------------------------------------------------
package org.example.move.webhookreceiver.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.move.webhookreceiver.rest.ReceiverControllerIntegrationTest.readResourceFile;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.move.IntegrationTestBase;
import org.example.move.webhookreceiver.rest.hmac.HmacChecker;
import org.example.move.webhookreceiver.rest.model.DealersEvent;
import org.example.move.webhookreceiver.rest.model.EventWrapper;
import org.example.move.webhookreceiver.rest.model.WebhookEventType;
import org.example.move.webhookreceiver.rest.model.WebhookPayload;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

@SuppressWarnings( {"ConstantConditions", "OptionalUsedAsFieldOrParameterType"})
@Slf4j
class DealerEventIntegrationTest extends IntegrationTestBase {

    @Autowired
    HmacChecker signatureChecker;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void enriched_listing_can_be_posted() throws JsonProcessingException {
        // given
        DealersEvent dealersEvent = aDealerEvent().getPayload();

        //when
        ResponseEntity<?> response = postDealerUpdate(dealersEvent, Optional.empty());

        //then
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    private ResponseEntity<Void> postDealerUpdate(DealersEvent event, Optional<String> givenHmac)
        throws JsonProcessingException {

        EventWrapper<WebhookPayload> payload = EventWrapper.builder()
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

    private EventWrapper<DealersEvent> aDealerEvent() {

        try {
            TypeReference<EventWrapper<DealersEvent>> eventWrapperTypeReference = new TypeReference<>() {
            };
            return objectMapper
                .readValue(readResourceFile("webhook/real-dealers-event.json"), eventWrapperTypeReference);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
