package org.example.move.webhookreceiver.shared.dealer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Set;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class DealerLogContact {

    private SubscriptionType type;

    private String email;

    private Set<String> phoneNumbers;

    private String url;
}