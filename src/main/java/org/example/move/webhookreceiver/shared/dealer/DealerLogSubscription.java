package org.example.move.webhookreceiver.shared.dealer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Date;
import java.util.Set;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class DealerLogSubscription {

    private String name;

    private String description;

    private SubscriptionType type;

    private BundleType bundle;

    private Integer quantity;

    private Integer lengthMonths;

    private Integer tierLimit;

    private Integer bumpUps;

    private Integer topAds;

    private Integer provincialTopAds;

    private Integer priceDrops;

    private Integer hpGalleries;

    private Integer autoRefreshInterval;

    private Set<AddOn> addOns;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date endDate;

}