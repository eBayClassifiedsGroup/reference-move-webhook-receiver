package org.example.move.webhookreceiver.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import ecg.move.sellermodel.listing.Listing;
import org.example.move.webhookreceiver.movemodel.dealer.DealerLogMessageV2;
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
public class EnrichedListingEvent implements WebhookPayload {

    @JsonProperty("listing")
    private ListingBeforeAfter beforeAfter;

    @JsonProperty("dealer")
    private DealerLogMessageV2 dealer;

    @JsonProperty("promotions")
    private Promotion[] promotions;

    @JsonIgnore
    public String getPartnerId() {
        return Optional.ofNullable(getMostRecentListing()).map(Listing::getPartnerName).orElse(null);
    }

    @JsonIgnore
    public String getForeignId() {
        return Optional.ofNullable(getMostRecentListing()).map(Listing::getForeignId).orElse(null);
    }

    @JsonIgnore
    public String getListingId() {
        return Optional.ofNullable(getMostRecentListing()).map(Listing::getId).orElse(null);
    }

    @JsonIgnore
    private Listing getMostRecentListing() {
        return getBeforeAfter().getNewState()  != null ? getBeforeAfter().getNewState() : getBeforeAfter().getOldState();
    }
}
