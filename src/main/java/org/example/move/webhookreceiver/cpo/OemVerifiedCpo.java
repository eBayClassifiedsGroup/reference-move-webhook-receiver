package org.example.move.webhookreceiver.cpo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OemVerifiedCpo {

    @ApiModelProperty(
        value = "The vehicle described in the listing is subject to a Certified Preowned scheme. This field uses "
            + "normalized, technical identifiers, not marketing names.",
        example = "mercedes-au",
        allowableValues = "audi-au, bmw-au, jaguar-au, mini-au, mercedes-au, nissan-au, toyota-au")
    String cpoProgramName;

}
