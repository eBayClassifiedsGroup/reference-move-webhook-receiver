//------------------------------------------------------------------
// Copyright 2020 mobile.de GmbH.
// Author/Developer: Philipp Bartsch
//
// This code is licensed under MIT license (see LICENSE for details)
//------------------------------------------------------------------
package org.example.move.webhookreceiver.rest.listing;

import static ecg.move.sellermodel.webhook.BadRequestModel.ErrorCodeEnum.INVALID_ENRICHED_LISTING;
import static ecg.move.sellermodel.webhook.BadRequestModel.ErrorCodeEnum.INVALID_LISTING;
import static ecg.move.sellermodel.webhook.SpecificError.ErrorCodeEnum.INACTIVE_SELLER;
import static ecg.move.sellermodel.webhook.SpecificError.ErrorCodeEnum.UNKNOWN_SELLER;

import ecg.move.sellermodel.webhook.BadRequestModel;
import ecg.move.sellermodel.webhook.BadRequestModel.ErrorCodeEnum;
import ecg.move.sellermodel.webhook.SpecificError;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.move.webhookreceiver.movemodel.listing.ListingBeforeAfter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
@Api(tags = {"Listing Lifecycle Event Receiver"}, description = "Receive listing updates. Comes in the flavors \"listing\" or \"enriched listing\", which also contains associated dealer data and promotions.")
@Slf4j
public class ListingReceiverController {
    /*
        The example controller implementation uses this id to test the case of unknown sellers.
        In reality, you'll likely have a database or external system that you check for the existence and
        state of a seller.
     */
    public static final String UNKNOWN_SELLER_ID = "unknown-seller-id";
    // this is used by the test to simulate an inactive seller
    public static final String INACTIVE_SELLER_ID = "inactive-seller-id";

    private static final String LINK_HEADER_NAME = "Link";

    private final LinkHeaderCreator linkHeaderCreator;

    @PostMapping(value = "/webhook/listing",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Webhook receiver for event type LISTING.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The request was accepted."),
        @ApiResponse(code = 400, message = "The request failed. See body for details.", response = BadRequestModel.class)
    })
    public ResponseEntity processEvent(@RequestBody @ApiParam("Event envelope") ListingEventEnvelope event) {

        if (event.getPayload() == null) {
            log.error("No payload received for: {}", event.getEventType());
            return ResponseEntity
                .badRequest()
                .body(createMessageNotUnderstoodError(INVALID_LISTING));
        }

        String sellerId = event.getPayload().getBeforeAfter().getNewState().getSeller().getForeignId();
        if (!isSellerKnown(sellerId)) {
            log.error("Received payload for unknown dealer: {}", event.getEventType());
            return ResponseEntity
                .badRequest()
                .body(createSellerError(INVALID_LISTING, UNKNOWN_SELLER, sellerId));
        }

        if (!isSellerActive(sellerId)) {
            log.error("Received payload for unknown dealer: {}", event.getEventType());
            return ResponseEntity
                .badRequest()
                .body(createSellerError(INVALID_LISTING, INACTIVE_SELLER, sellerId));
        }

        log.info("Received event type '{}', payload: '{}'.", event.getEventType(), event.getPayload());
        String linkHeader = processListing(event.getPayload().getBeforeAfter());
        log.info("Responding to event type '{}' with link header '{}'.", event.getEventType(), linkHeader);

        return ResponseEntity.ok().header(LINK_HEADER_NAME, linkHeader).build();
    }

    @PostMapping(value = "/webhook/enriched-listing",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Webhook receiver for event type ENRICHED-LISTING.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The request was accepted."),
        @ApiResponse(code = 400, message = "The request failed. See body for details.", response = BadRequestModel.class)
    })
    public ResponseEntity processEvent(
        @RequestBody @ApiParam("Event envelope") EnrichedListingEventEnvelope event
    ) {
        if (event.getPayload() == null) {
            log.error("No payload received for: {}", event.getEventType());
            return ResponseEntity
                .badRequest()
                .body(createMessageNotUnderstoodError(INVALID_ENRICHED_LISTING));
        }

        String sellerId = event.getPayload().getBeforeAfter().getNewState().getSeller().getForeignId();
        if (!isSellerKnown(sellerId)) {
            log.error("Received payload for unknown dealer: {}", event.getEventType());
            return ResponseEntity
                .badRequest()
                .body(createSellerError(INVALID_ENRICHED_LISTING, UNKNOWN_SELLER, sellerId));
        }

        if (!isSellerActive(sellerId)) {
            log.error("Received payload for unknown dealer: {}", event.getEventType());
            return ResponseEntity
                .badRequest()
                .body(createSellerError(INVALID_ENRICHED_LISTING, INACTIVE_SELLER, sellerId));
        }

        log.info("Received event type '{}', payload: '{}'.", event.getEventType(), event.getPayload());
        String linkHeader = processListing(event.getPayload().getBeforeAfter());
        log.info("Responding to event type '{}' with link header '{}'.", event.getEventType(), linkHeader);

        return ResponseEntity.ok().header(LINK_HEADER_NAME, linkHeader).build();
    }

    // this is a dummy method that returns static content
    private String processListing(ListingBeforeAfter listingPayload) {

        // the receiver is expected to assign an id to the listing and return it for tracking purposes to MoVe
        // for testability reasons we just use the move id here and add a suffix
        String localListingId = listingPayload.getLatest().getId() + "-received";

        // this points to the VIP URL under which the listing will be, eventually, visible
        String localListingVehicleInformationPageUrl = "http://www.marketplace.com/id=" + localListingId;

        return linkHeaderCreator.createHeader(localListingId, localListingVehicleInformationPageUrl);
    }

    private boolean isSellerKnown(String sellerId) {
        return !sellerId.equalsIgnoreCase(UNKNOWN_SELLER_ID);
    }

    private boolean isSellerActive(String sellerId) {
        return !sellerId.startsWith(INACTIVE_SELLER_ID);
    }

    private BadRequestModel createMessageNotUnderstoodError(ErrorCodeEnum generalErrorCode) {
        SpecificError specificError = new SpecificError();
        specificError.setErrorCode(SpecificError.ErrorCodeEnum.OTHER_ERROR);
        specificError.setMessage("The payload was malformed.");

        BadRequestModel model = new BadRequestModel();
        model.setErrorCode(generalErrorCode);
        model.setSpecificErrors(List.of(specificError));
        return model;
    }

    private BadRequestModel createSellerError(
        ErrorCodeEnum generalErrorCode,
        SpecificError.ErrorCodeEnum specificErrorCode,
        String sellerId) {

        SpecificError specificError = new SpecificError();
        specificError.setErrorCode(specificErrorCode);
        specificError.setMessage(String.format("Problematic seller id: %s", sellerId));

        BadRequestModel model = new BadRequestModel();
        model.setErrorCode(generalErrorCode);
        model.setSpecificErrors(List.of(specificError));
        return model;
    }
}
