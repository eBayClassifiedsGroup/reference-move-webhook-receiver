package org.example.move.webhookreceiver.movemodel.dealer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class Location {

    private String street;

    private String houseNumber;

    private String additionalAddressInfo;

    private String zipCode;

    private String city;

    private String province;
    /**
     * Two letter country code of this location. The country codes are following ISO-3166-1 alpha-2.
     */
    private String country;

    private GeoCoord geoCoord;

}
