package com.bid.idearush.global.filter;

import com.bid.idearush.global.security.UserAuthentication;
import com.bid.idearush.global.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        if (requestURI.matches("/api/auth/.*") ||
                requestURI.matches("/api/sse/connect/idea/.*") ||
                ((requestURI.matches("/api/ideas") ||
                        requestURI.matches("/api/ideas/.*") ||
                        requestURI.matches("/api/ideas/.*/bid")) &&
                        "GET".equalsIgnoreCase(method)) ||
                requestURI.matches("/chat/.*") || requestURI.matches("/view/.*") || requestURI.matches("/image/.*")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = getJwtFromRequest(request);

        if (StringUtils.hasText(jwt)) {
            if (jwtUtils.validateToken(jwt)) {
                Long userId = jwtUtils.parseUserId(jwt);
                UserAuthentication authentication = new UserAuthentication(userId);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            } else {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 토큰입니다.");
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "토큰이 없습니다.");
        }
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }
        return null;
    }

}