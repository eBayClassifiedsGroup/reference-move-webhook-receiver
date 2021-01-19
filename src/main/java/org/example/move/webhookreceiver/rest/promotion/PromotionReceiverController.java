//------------------------------------------------------------------
// Copyright 2020 mobile.de GmbH.
// Author/Developer: Philipp Bartsch
//
// This code is licensed under MIT license (see LICENSE for details)
//------------------------------------------------------------------
package org.example.move.webhookreceiver.rest.promotion;

import ecg.move.sellermodel.promotion.PublicPromotionLargeModel;
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
@Api(tags = {"Promotion Event Receiver"}, description = "Receive pure promotion events.")
@Slf4j
public class PromotionReceiverController {

    @PostMapping(value = "/webhook/promotion",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Webhook receiver for event type PROMOTION. This kind of event describes any kind of"
        + " promotion applied to a listing.")
    public ResponseEntity<Void> processUrlEvent(@RequestBody @ApiParam("The event") PromotionEventEnvelope event) {
        if (!WebhookEventType.PROMOTIONS.equals(event.getEventType())) {
            log.error("Unexpected event type received: {}", event.getEventType());
            return ResponseEntity.badRequest().build();
        }

        PublicPromotionLargeModel payload = event.getPayload();
        if (payload == null) {
            log.error("No payload received for: {}", event.getEventType());
            return ResponseEntity.badRequest().build();
        }

        log.info("Received event: promotion for listing #{}, type '{}'.",
            payload.getListingId(),
            payload.getPromotionType());

        return ResponseEntity.ok().build();
    }
}
