//------------------------------------------------------------------
// Copyright 2020 mobile.de GmbH.
// Author/Developer: Philipp Bartsch
//
// This code is licensed under MIT license (see LICENSE for details)
//------------------------------------------------------------------
package org.example.move.webhookreceiver.rest;

import static org.assertj.core.api.Assertions.assertThat;

import org.example.move.webhookreceiver.rest.hmac.HmacChecker;
import org.junit.jupiter.api.Test;

class HmacCheckerTest {

    private final String A_PASSWORD = "1234";
    private final String A_MESSAGE = "This is a message!";
    // this is a correct signature for A_MESSAGE for "HmacSHA256" algorithm based on A_PASSWORD
    private final String A_CORRECT_SIGNATURE = "DETk8gddVlrEM5weMbEjiJwlaGSDrqq0nZyS1yYhCiM=";

    private HmacChecker systemUnderTest = new HmacChecker(A_PASSWORD);

    @Test
    void signature_check_returns_true_on_match() {
        boolean matches = systemUnderTest.isMatchingSignature(A_CORRECT_SIGNATURE, A_MESSAGE);

        assertThat(matches).isTrue();
    }

    @Test
    void signature_check_returns_false_on_mismatch() {
        boolean matches = systemUnderTest.isMatchingSignature("wrong signature", A_MESSAGE);

        assertThat(matches).isFalse();
    }
}