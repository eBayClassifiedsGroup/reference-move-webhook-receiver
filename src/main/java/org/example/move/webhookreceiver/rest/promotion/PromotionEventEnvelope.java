//------------------------------------------------------------------
// Copyright 2020 mobile.de GmbH.
// Author/Developer: Philipp Bartsch
//
// This code is licensed under MIT license (see LICENSE for details)
//------------------------------------------------------------------
package org.example.move.webhookreceiver.rest.promotion;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import ecg.move.sellermodel.promotion.PublicPromotionLargeModel;
import ecg.move.sellermodel.webhook.ListingUrl;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.move.webhookreceiver.rest.WebhookEventType;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PromotionEventEnvelope {

    @ApiModelProperty(
            value = "The event type - should be \"promotions\".",
            example = "listing-url")
    private WebhookEventType eventType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @ApiModelProperty(
            value = "Event timestamp.",
            example = "2020-12-07T09:13:38Z")
    private Date timestamp;

    @ApiModelProperty(
            value = "The promotion data.")
    private PublicPromotionLargeModel payload;
}
