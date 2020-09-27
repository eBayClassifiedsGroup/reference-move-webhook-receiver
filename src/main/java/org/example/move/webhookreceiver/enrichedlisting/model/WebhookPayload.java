package org.example.move.webhookreceiver.enrichedlisting.model;

public interface WebhookPayload {

    String getPartnerId();

    String getForeignId();

    String getListingId();
}
