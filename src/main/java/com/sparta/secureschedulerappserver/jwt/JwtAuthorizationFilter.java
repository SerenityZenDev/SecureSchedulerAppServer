package com.sparta.secureschedulerappserver.jwt;

import com.sparta.secureschedulerappserver.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtTokenError jwtTokenError;


    @Override
    protected void doFilterInternal(
        HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)
        throws ServletException, IOException {

        String accessTokenValue = jwtUtil.getAccessTokenFromRequest(req);
        String claimToToken;

        if (StringUtils.hasText(accessTokenValue)) {
            // JWT 토큰 substring
            accessTokenValue = jwtUtil.substringToken(accessTokenValue);
            log.info(accessTokenValue);
            claimToToken = accessTokenValue;

            try{
                if (!jwtUtil.validateAccessToken(accessTokenValue)) {
                    jwtTokenError.messageToClient(res, 400, "토큰에 문제", "failed");
                    return;
                }
            }catch (ExpiredJwtException e) {
                logger.error("Expired JWT token, 만료된 JWT AccessToken 입니다.");
                String refreshTokenValue = jwtUtil.getRefreshTokenFromRequest(req);
                refreshTokenValue = jwtUtil.substringToken(refreshTokenValue);

                if (StringUtils.hasText(refreshTokenValue) && jwtUtil.validateRefreshToken(refreshTokenValue)){
                    String newAccessToken = jwtUtil.createAccessToken(jwtUtil.getUserInfoFromToken(refreshTokenValue).getSubject());
                    jwtUtil.addAccessTokenToCookie(newAccessToken, res);
                    claimToToken = jwtUtil.substringToken(newAccessToken);
                } else {
                    jwtTokenError.messageToClient(res, 400, "토큰에 문제", "failed");
                    return;
                }
            }


            Claims info = jwtUtil.getUserInfoFromToken(claimToToken);

            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                jwtTokenError.messageToClient(res, 400, "토큰에 문제", "failed");
                return;
            }
        } else if (
            req.getRequestURI().startsWith("/user/") ||
                req.getRequestURI().startsWith("/swagger-ui/") ||
                req.getRequestURI().startsWith("/v3/") ||
                req.getRequestURI().startsWith("/swagger-resources/") ||
                req.getRequestURI().startsWith("/webjars/") ||
                req.getRequestURI().startsWith("/favicon.ico")
        ) {
            filterChain.doFilter(req, res);
            return;
        } else {

            jwtTokenError.messageToClient(res, 400, "토큰에 문제", "failed");
            return;
        }

        filterChain.doFilter(req, res);
    }

    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null,
            userDetails.getAuthorities());
    }


}
