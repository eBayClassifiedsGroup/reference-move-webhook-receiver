package org.example.move.webhookreceiver.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LinkHeaderCreator {

    private static final String LINK_HEADER_FORMAT = "<%s>; rel=\"%s\", <%s>; rel=\"listing-id\"";

    String createHeader(String listingId, String vipUrl) {
        return String.format(LINK_HEADER_FORMAT, vipUrl, "en", listingId);
    }
}
