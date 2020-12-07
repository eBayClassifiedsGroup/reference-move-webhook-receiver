//------------------------------------------------------------------
// Copyright 2020 mobile.de GmbH.
// Author/Developer: Philipp Bartsch
//
// This code is licensed under MIT license (see LICENSE for details)
//------------------------------------------------------------------
package org.example.move.webhookreceiver.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import ecg.move.sellermodel.dealer.DealerLogMessageV2;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DealersEvent implements WebhookPayload {

    @JsonProperty("payload")
    private DealerLogMessageV2 dealer;

    @JsonIgnore
    public String getForeignId() {
        return Optional.ofNullable(getDealer()).map(DealerLogMessageV2::getForeignId).orElse(null);
    }

    @JsonIgnore
    public String getEntityId() {
        return Optional.ofNullable(getDealer()).map(DealerLogMessageV2::getId).orElse(null);
    }
}
