//------------------------------------------------------------------
// Copyright 2020 mobile.de GmbH.
// Author/Developer: Philipp Bartsch
//
// This code is licensed under MIT license (see LICENSE for details)
//------------------------------------------------------------------
package org.example.move.webhookreceiver.rest.listingurl;

import ecg.move.sellermodel.webhook.ListingUrl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.move.webhookreceiver.rest.WebhookEventType;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
@Api(tags = {"Listing Distribution Event Receiver"}, description = "Receive a notification if a listing has been dispatched to another marketplace. Includes foreign listing VIP URLs, ids and such.")
@Slf4j
public class ListingUrlReceiverController {

    @PostMapping(value = "/webhook/listing-url",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Webhook receiver for event type LISTING-URL. When subscribed to this type of event, you will"
        + " be notified whenever a listing has been delivered to a marketplace."
        + " Example: MoVe delivers to Marketplace A (this is you) and Marketplace B (this is not you). As soon as a"
        + " listing has been delivered to Marketplace B, you'll receive a Listing-URL event that gives you the VIP URL"
        + " of the listing in Marketplace A.")
    public ResponseEntity<Void> processUrlEvent(@RequestBody @ApiParam("The event") ListingUrlEventEnvelope event) {
        if (!WebhookEventType.LISTING_URL.equals(event.getEventType())) {
            log.error("Unexpected event type received: {}", event.getEventType());
            return ResponseEntity.badRequest().build();
        }

        ListingUrl payload = event.getPayload();
        if (payload == null) {
            log.error("No payload received for: {}", event.getEventType());
            return ResponseEntity.badRequest().build();
        }

        log.info("Received event: MoVe listing '#{}', upstream known as {}/{}, has been dispatched to '{}', where it can be reached under '{}'.",
            payload.getListingId(),
            payload.getPartnerId(),
            payload.getPartnerListingId(),
            payload.getMarketplaceId(),
            payload.getUrls().get(0).getUrl());

        return ResponseEntity.ok().build();
    }
}
