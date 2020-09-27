package org.example.move.webhookreceiver.shared.listing;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import ecg.move.sellermodel.listing.Listing;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListingBeforeAfter {
    @JsonProperty("newState")
    private Listing newState;

    @JsonProperty("oldState")
    private Listing oldState;

    @JsonIgnore
    public Listing getLatest() {
        return newState != null ? newState : oldState;
    }

    public boolean isCreate() {
        return newState != null && oldState == null;
    }

}
