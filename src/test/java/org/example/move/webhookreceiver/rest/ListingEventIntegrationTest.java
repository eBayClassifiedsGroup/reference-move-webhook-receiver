//------------------------------------------------------------------
// Copyright 2020 mobile.de GmbH.
// Author/Developer: Philipp Bartsch
//
// This code is licensed under MIT license (see LICENSE for details)
//------------------------------------------------------------------
package org.example.move.webhookreceiver.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.move.IntegrationTestBase;
import org.example.move.webhookreceiver.movemodel.listing.ListingBeforeAfter;
import org.example.move.webhookreceiver.rest.hmac.HmacChecker;
import org.example.move.webhookreceiver.rest.model.EnrichedListingEvent;
import org.example.move.webhookreceiver.rest.model.EventWrapper;
import org.example.move.webhookreceiver.rest.model.WebhookEventType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

@SuppressWarnings( {"ConstantConditions", "OptionalUsedAsFieldOrParameterType"})
@Slf4j
class ListingEventIntegrationTest extends IntegrationTestBase {

    @Autowired
    HmacChecker signatureChecker;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void enriched_listing_can_be_posted() throws JsonProcessingException {
        // given
        EnrichedListingEvent enrichedListingEvent = enrichedListingEvent().getPayload();
        String listingId = enrichedListingEvent.getEntityId();

        //when
        ResponseEntity<?> response = postEnrichedListingCreate(enrichedListingEvent);

        //then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getHeaders().get("Link").get(0)).isEqualTo(createExpectedLinkHeader(listingId));
    }

    @Test
    void hmac_is_enforced_when_provided() throws JsonProcessingException {
        // given
        EnrichedListingEvent enrichedListingEvent = enrichedListingEvent().getPayload();

        // when
        ResponseEntity<?> response = postEnrichedListingCreate(enrichedListingEvent, "broken_signature");

        // then: a mismatching signature will be rejected as 400
        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    private String createExpectedLinkHeader(String listingId) {
        return String.format(
            "<http://www.marketplace.com/id=%s>; rel=\"en\", "
                + "<%s>; rel=\"listing-id\"", listingId + "-received", listingId + "-received");
    }

    private ResponseEntity<?> postEnrichedListingCreate(EnrichedListingEvent enrichedListingEvent)
        throws JsonProcessingException {

        EnrichedListingEvent payload = EnrichedListingEvent.builder()
            .beforeAfter(ListingBeforeAfter.builder()
                .newState(enrichedListingEvent.getBeforeAfter().getNewState())
                .build())
            .dealer(enrichedListingEvent.getDealer())
            .promotions(enrichedListingEvent.getPromotions())
            .build();

        return enrichedListingCall(payload, Optional.empty());
    }

    private ResponseEntity<?> postEnrichedListingCreate(EnrichedListingEvent enrichedListingEvent, String hmac)
        throws JsonProcessingException {
        return enrichedListingCall(
            EnrichedListingEvent.builder()
                .beforeAfter(ListingBeforeAfter.builder()
                    .newState(enrichedListingEvent.getBeforeAfter().getNewState())
                    .build())
                .dealer(enrichedListingEvent.getDealer())
                .promotions(enrichedListingEvent.getPromotions())
                .build(),
            Optional.of(hmac)
        );
    }

    private ResponseEntity<Void> enrichedListingCall(EnrichedListingEvent event, Optional<String> givenHmac)
        throws JsonProcessingException {

        EventWrapper payload = EventWrapper.builder()
            .payload(event)
            .eventType(WebhookEventType.ENRICHED_LISTING)
            .timestamp(new Date())
            .build();

        String actualHmac = signatureChecker.createSignatureAsBase64String(objectMapper.writeValueAsString(payload));

        return rest.exchange(
            "/webhook/enriched-listing",
            POST,
            new HttpEntity<>(
                payload,
                CollectionUtils.toMultiValueMap(
                    Map.of("signature", List.of(givenHmac.orElse(actualHmac)))
                )
            ),
            Void.class);
    }

    private EventWrapper<EnrichedListingEvent> enrichedListingEvent() {
        ObjectMapper om = new ObjectMapper();
        try {
            TypeReference<EventWrapper<EnrichedListingEvent>> eventWrapperTypeReference = new TypeReference<>() {
            };
            return om
                .readValue(readResourceFile("webhook/real-enriched-listing-event.json"), eventWrapperTypeReference);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    static String readResourceFile(String resource) throws URISyntaxException, IOException {
        final Path resourcePath = Paths.get(ClassLoader.getSystemResource(resource).toURI());
        return new String(Files.readAllBytes(resourcePath));
    }
}
