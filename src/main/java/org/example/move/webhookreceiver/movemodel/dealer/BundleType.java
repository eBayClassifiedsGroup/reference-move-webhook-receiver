package org.example.move.webhookreceiver.movemodel.dealer;

import static org.example.move.webhookreceiver.movemodel.dealer.AddOn.AUTO_REFRESH;
import static org.example.move.webhookreceiver.movemodel.dealer.AddOn.DEALER_LOGO_ON_VIP;
import static org.example.move.webhookreceiver.movemodel.dealer.AddOn.DEALER_PAGE_BASIC;
import static org.example.move.webhookreceiver.movemodel.dealer.AddOn.DEALER_PAGE_ENHANCED;
import static org.example.move.webhookreceiver.movemodel.dealer.AddOn.HIDE_RELATED_LISTINGS_ON_VIP;

import java.util.Set;

public enum BundleType {

    BUSINESS(
        DEALER_PAGE_BASIC),

    BUSINESS_PRO(
        DEALER_PAGE_ENHANCED,
        DEALER_LOGO_ON_VIP),

    BUSINESS_ELITE(
        DEALER_PAGE_ENHANCED,
        DEALER_LOGO_ON_VIP,
        HIDE_RELATED_LISTINGS_ON_VIP,
        AUTO_REFRESH),

    BASIC,

    PLUS,

    PREMIUM;

    private Set<AddOn> addOns;

    BundleType(AddOn... addOns) {
        this.addOns = Set.of(addOns);
    }

    public Set<AddOn> getAddOns() {
        return addOns;
    }
}
