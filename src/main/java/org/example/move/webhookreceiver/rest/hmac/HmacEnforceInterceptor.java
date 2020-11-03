//------------------------------------------------------------------
// Copyright 2020 mobile.de GmbH.
// Author/Developer: Philipp Bartsch
//
// This code is licensed under MIT license (see LICENSE for details)
//------------------------------------------------------------------
package org.example.move.webhookreceiver.rest.hmac;

import java.nio.charset.Charset;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class HmacEnforceInterceptor implements HandlerInterceptor {

    @Autowired
    HmacChecker signatureChecker;

    @Override
    public boolean preHandle(
        HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String givenSignature = request.getHeader("signature");
        if (givenSignature != null) {
            String message = IOUtils.toString(request.getInputStream(), Charset.defaultCharset());
            boolean matches = signatureChecker.isMatchingSignature(givenSignature, message);
            if (!matches) {
                response.reset();
                response.sendError(400, "Invalid signature");
            }
            return matches;
        } else {
            return true;
        }
    }

}
