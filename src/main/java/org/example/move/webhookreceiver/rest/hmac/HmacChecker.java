//------------------------------------------------------------------
// Copyright 2020 mobile.de GmbH.
// Author/Developer: Philipp Bartsch
//
// This code is licensed under MIT license (see LICENSE for details)
//------------------------------------------------------------------
package org.example.move.webhookreceiver.rest.hmac;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HmacChecker {

    private static final String ALGORITHM = "HmacSHA256";
    private final String hmacPrivateKey;

    public HmacChecker(@Value("${signaturecheck.password:}") String hmacPrivateKey) {
        this.hmacPrivateKey = hmacPrivateKey;
    }

    public boolean isMatchingSignature(String givenSignature, String message) {
        String actualSignature = createSignatureAsBase64String(hmacPrivateKey, message);
        return givenSignature.equals(actualSignature);
    }

    public String createSignatureAsBase64String(String message) {
        return createSignatureAsBase64String(hmacPrivateKey, message);
    }

    public String createSignatureAsBase64String(String key, String message) {
        try {
            SecretKey secret = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(secret);
            mac.update(message.getBytes());
            byte[] binarySignature = mac.doFinal();
            return new String(Base64.getEncoder().encode(binarySignature));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Failed to create HMAC Signature");
        }
    }
}
