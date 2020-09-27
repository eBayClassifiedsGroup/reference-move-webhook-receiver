package org.example.move.webhookreceiver.movemodel.promotion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Promotion {

    @JsonProperty("id")
    public String id;

    @JsonProperty("promotion")
    public String promotionType;

    @JsonProperty("active")
    public Boolean active;

}
