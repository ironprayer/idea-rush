package com.bid.idearush.global.filter;

import com.bid.idearush.global.security.UserAuthentication;
import com.bid.idearush.global.util.HttpUtils;
import com.bid.idearush.global.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = HttpUtils.getJwtFromRequest(request);

        if (StringUtils.hasText(jwt)) {
            if (JwtUtils.validateToken(jwt)) {
                Long userId = JwtUtils.parseUserId(jwt);
                UserAuthentication authentication = new UserAuthentication(userId);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                request.setAttribute(RequestDispatcher.ERROR_MESSAGE, "유효하지 않은 토큰입니다.");
                request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.UNAUTHORIZED.value());
            }
        } else {
            request.setAttribute(RequestDispatcher.ERROR_MESSAGE, "토큰이 없습니다.");
            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.UNAUTHORIZED.value());
        }

        filterChain.doFilter(request, response);
    }

}
