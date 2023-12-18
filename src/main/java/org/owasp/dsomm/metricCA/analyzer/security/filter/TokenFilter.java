package org.owasp.dsomm.metricCA.analyzer.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.owasp.dsomm.metricCA.analyzer.security.config.TokenConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Order(1)
@Component
public class TokenFilter extends OncePerRequestFilter {

    @Value("${metricCA.access.token.enabled}")
    private boolean enabled;

    @Autowired
    TokenConfiguration tokenConfiguration;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        if(enabled){
            //TODO
            System.out.println(tokenConfiguration.getToken());
        }
    }
}
