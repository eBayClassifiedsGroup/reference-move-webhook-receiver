//------------------------------------------------------------------
// Copyright 2020 mobile.de GmbH.
// Author/Developer: Philipp Bartsch
//
// This code is licensed under MIT license (see LICENSE for details)
//------------------------------------------------------------------
package org.example.move.webhookreceiver.config;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/*
    To check the hmac the request body needs to be read at least twice, which is not possible out of the box.
    This mechanism here makes it possible.
 */
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Component
@WebFilter(filterName = "ContentCachingFilter", urlPatterns = "/*")
public class RereadablePayloadFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse,
        FilterChain filterChain) throws ServletException, IOException {

        RereadableBodyHttpServletRequest rereadableBodyHttpServletRequest = new RereadableBodyHttpServletRequest(httpServletRequest);
        filterChain.doFilter(rereadableBodyHttpServletRequest, httpServletResponse);
    }
}
