package org.example.move.webhookreceiver.shared.dealer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Date;
import java.util.List;
import java.util.Set;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class DealerLogMessageV2 {

    private String id;

    private String name;

    private String foreignId;

    private String tenantId;

    private String partnerName;

    private String chatId;

    private String description;

    private List<String> openingHours;

    private String additionalOperatingHours;

    private List<Location> locations;

    private String googlePlaceId;

    private String logoUrl;

    private List<String> heroImageUrls;

    private List<DealerLogContact> contacts;

    private List<DealerLogSubscription> subscriptions;

    private Set<AddOn> addOns;

    private JsonNode additionalFields;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date createdDate;

    private boolean active;

}
