//------------------------------------------------------------------
// Copyright 2020 mobile.de GmbH.
// Author/Developer: Philipp Bartsch
//
// This code is licensed under MIT license (see LICENSE for details)
//------------------------------------------------------------------
package org.example.move.webhookreceiver.rest;

import ecg.move.sellermodel.dealer.DealerLogMessageV2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.move.webhookreceiver.rest.model.EventWrapper;
import org.example.move.webhookreceiver.rest.model.WebhookEventType;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
@Api(tags = {"Webhook Receiver"})
@Slf4j
public class DealerReceiverController {

    @PostMapping(value = "/webhook/dealer",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Webhook receiver for event type DEALER.")
    public ResponseEntity<Void> processDealersEvent(
        @RequestHeader("signature") @ApiParam("Payload signature") String signature,
        @RequestBody @ApiParam("The event") EventWrapper<DealerLogMessageV2> event) {
        if (!WebhookEventType.DEALERS.equals(event.getEventType())) {
            log.error("Unexpected event type received: {}", event.getEventType());
            return ResponseEntity.badRequest().build();
        }

        if (event.getPayload() == null) {
            log.error("No payload received for: {}", event.getEventType());
            return ResponseEntity.badRequest().build();
        }

        log.info("Received event type '{}', payload: '{}'.", event.getEventType(), event.getPayload());
        processDealerUpdate(event.getPayload());

        return ResponseEntity.ok().build();
    }

    private void processDealerUpdate(DealerLogMessageV2 dealerUpdate) {
        log.trace("Processing dealer {}", dealerUpdate.getId());
    }
}
