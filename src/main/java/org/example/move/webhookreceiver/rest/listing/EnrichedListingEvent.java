//------------------------------------------------------------------
// Copyright 2020 mobile.de GmbH.
// Author/Developer: Philipp Bartsch
//
// This code is licensed under MIT license (see LICENSE for details)
//------------------------------------------------------------------
package org.example.move.webhookreceiver.rest.listing;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import ecg.move.sellermodel.dealer.DealerLogMessageV2;
import ecg.move.sellermodel.listing.Listing;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.move.webhookreceiver.movemodel.listing.ListingBeforeAfter;
import org.example.move.webhookreceiver.movemodel.promotion.Promotion;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnrichedListingEvent implements IsListing {

    @JsonProperty("listing")
    private ListingBeforeAfter beforeAfter;

    @JsonProperty("dealer")
    private DealerLogMessageV2 dealer;

    @JsonProperty("promotions")
    private Promotion[] promotions;

    @JsonIgnore
    public String getForeignId() {
        return Optional.ofNullable(getMostRecentListing()).map(Listing::getForeignId).orElse(null);
    }

    @JsonIgnore
    public String getEntityId() {
        return Optional.ofNullable(getMostRecentListing()).map(Listing::getId).orElse(null);
    }

    @JsonIgnore
    private Listing getMostRecentListing() {
        return getBeforeAfter().getNewState()  != null ? getBeforeAfter().getNewState() : getBeforeAfter().getOldState();
    }
}
