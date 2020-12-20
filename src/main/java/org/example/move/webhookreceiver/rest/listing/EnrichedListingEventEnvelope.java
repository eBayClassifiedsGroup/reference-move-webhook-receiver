//------------------------------------------------------------------
// Copyright 2020 mobile.de GmbH.
// Author/Developer: Philipp Bartsch
//
// This code is licensed under MIT license (see LICENSE for details)
//------------------------------------------------------------------
package org.example.move.webhookreceiver.rest.listing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.move.webhookreceiver.rest.WebhookEventType;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnrichedListingEventEnvelope {

    @ApiModelProperty(
            value = "The event type - should be \"enriched-listings\".",
            example = "enriched-listings")
    private WebhookEventType eventType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @ApiModelProperty(
            value = "Event timestamp.",
            example = "2020-12-07T09:13:38Z")
    private Date timestamp;

    @ApiModelProperty(
            value = "The enriched listing data.")
    private EnrichedListingEvent payload;
}
