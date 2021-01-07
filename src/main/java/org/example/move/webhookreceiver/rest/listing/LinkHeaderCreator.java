//------------------------------------------------------------------
// Copyright 2020 mobile.de GmbH.
// Author/Developer: Philipp Bartsch
//
// This code is licensed under MIT license (see LICENSE for details)
//------------------------------------------------------------------
package org.example.move.webhookreceiver.rest.listing;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LinkHeaderCreator {

    /*
        The purpose of the link header is to return the assigned listing id and a number of VIP URLs back to MoVe.
        Structurally the link header is a comma separated list of key-value pairs.
        The format is `<value>; rel="key"`. You can provide multiple pairs and are free to choose key names.
        MoVe expects that at least "listing-id" is provided to see what listing id has been assigned by the receiver.

        Example for a receiver that has 2 sites: hipster.com which is english/french and fancy.nl being english
        and dutch localisation:

        <https://hipster.com/en/123>; rel="hipster-en", <https://hipser.com/fr/123>; rel="hipster-fr", <https://fancy.nl/en/123>; rel="fancy-en", <https://fancy.nl/nl/123>; rel="fancy-nl" <123>; rel="listing-id"
     */
    private static final String LINK_HEADER_FORMAT = "<%s>; rel=\"%s\", <%s>; rel=\"listing-id\"";

    String createHeader(String listingId, String vipUrl) {
        return String.format(LINK_HEADER_FORMAT, vipUrl, "en", listingId);
    }
}
