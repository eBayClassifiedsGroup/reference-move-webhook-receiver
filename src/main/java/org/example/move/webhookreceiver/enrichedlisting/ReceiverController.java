package org.example.move.webhookreceiver.enrichedlisting;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.move.webhookreceiver.enrichedlisting.model.EventWrapper;
import org.example.move.webhookreceiver.enrichedlisting.model.WebhookEventType;
import org.example.move.webhookreceiver.enrichedlisting.model.payload.EnrichedListingEvent;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
@Api(tags = {"Webhook Receiver"})
@Slf4j
public class ReceiverController {

    private static final String LINK_HEADER_NAME = "Link";

    private final LinkHeaderCreator linkHeaderCreator;

    @PostMapping(value = "/webhook/enriched-listing",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Webhook receiver for event type ENRICHED-LISTING.")
    public ResponseEntity processEnrichedListingEvent(@RequestBody @ApiParam("The event") EventWrapper<EnrichedListingEvent> event) {

        if (!WebhookEventType.ENRICHED_LISTING.equals(event.getEventType())) {
            log.error("Unexpected event type received: {}", event.getEventType());
            return ResponseEntity.badRequest().build();
        }

        if (event.getPayload() == null) {
            log.error("No payload received for: {}", event.getEventType());
            return ResponseEntity.badRequest().build();
        }

        log.info("Received event type '{}', payload: '{}'.", event.getEventType(), event.getPayload());

        // the receiver is expected to assign an id to the listing and return it for tracking purposes to MoVe
        String localListingId = UUID.randomUUID().toString();
        // this points to the VIP URL under which the listing will be, eventually, visible
        String localListingVehicleInformationPageUrl = "http://www.marketplace.com/id=" + localListingId;

        String linkHeader = linkHeaderCreator.createHeader(localListingId, localListingVehicleInformationPageUrl);

        log.info("Responding to event type '{}' with link header '{}'.", event.getEventType(), linkHeader);

        return ResponseEntity.ok().header(LINK_HEADER_NAME, linkHeader).build();
    }
}
