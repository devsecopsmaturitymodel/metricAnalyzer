package org.owasp.dsomm.metricCA.analyzer.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.owasp.dsomm.metricCA.analyzer.security.config.TokenConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Order(1)
@Component
public class TokenFilter extends OncePerRequestFilter {

  @Autowired
  private TokenConfiguration tokenConfiguration;
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {

    try {
      final String authorizationHeader = request.getHeader("Authorization");
      String jwt = null;

      if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        jwt = authorizationHeader.substring(7);
        if (jwt.equals(tokenConfiguration.getToken())) {
          System.out.println("Token Present");
          filterChain.doFilter(request, response);
        } else {
          response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
      } else {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
      }

    } catch (Exception e) {
      e.printStackTrace();
      response.setStatus(401);
      response.setHeader("Access-Control-Allow-Origin", "*");
    }
  }

}

