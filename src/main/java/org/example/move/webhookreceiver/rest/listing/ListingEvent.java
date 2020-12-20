//------------------------------------------------------------------
// Copyright 2020 mobile.de GmbH.
// Author/Developer: Philipp Bartsch
//
// This code is licensed under MIT license (see LICENSE for details)
//------------------------------------------------------------------
package org.example.move.webhookreceiver.rest.listing;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import ecg.move.sellermodel.listing.Listing;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.move.webhookreceiver.movemodel.listing.ListingBeforeAfter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListingEvent implements IsListing {

    @JsonProperty("listing")
    private ListingBeforeAfter beforeAfter;

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
