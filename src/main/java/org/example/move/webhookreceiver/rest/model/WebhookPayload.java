//------------------------------------------------------------------
// Copyright 2020 mobile.de GmbH.
// Author/Developer: Philipp Bartsch
//
// This code is licensed under MIT license (see LICENSE for details)
//------------------------------------------------------------------
package org.example.move.webhookreceiver.rest.model;

public interface WebhookPayload {

    String getPartnerId();

    String getForeignId();

    String getListingId();
}
