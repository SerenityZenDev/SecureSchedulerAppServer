package com.sparta.secureschedulerappserver.jwt;

import com.sparta.secureschedulerappserver.redis.RefreshTokenRedisRepository;
import com.sparta.secureschedulerappserver.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
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
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;


    @Override
    protected void doFilterInternal(
        HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)
        throws ServletException, IOException {


        String accessToken = jwtUtil.getAccessTokenFromRequest(req);
        if(StringUtils.hasText(accessToken)) {
            try{
                String subAccessToken = jwtUtil.substringToken(accessToken);
                Claims info = jwtUtil.getUserInfoFromToken(subAccessToken);

                Long userId = info.get("userId", Long.class);
                String username = info.get("username", String.class);

                setAuthentication(userId, username);
            }catch (SecurityException | MalformedJwtException | SignatureException e) {
                logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
                return;
            } catch (ExpiredJwtException e) {
                if(refreshTokenRedisRepository.findByKey(accessToken).isEmpty()){
                    logger.error("Expired JWT token, 만료된 JWT token 입니다.");
                    return;
                }
                Claims info = e.getClaims();

                Long userId = info.get("userId", Long.class);
                String username = info.get("username", String.class);

                String newAccessToken = jwtUtil.createAccessToken(userId, username);
                jwtUtil.addAccessTokenToCookie(newAccessToken, res);

                refreshTokenRedisRepository.updateAccessToken(accessToken, newAccessToken);

                setAuthentication(userId, username);
            } catch (UnsupportedJwtException e) {
                logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
                return;
            } catch (IllegalArgumentException e) {
                logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
                return;
            }

        }

        filterChain.doFilter(req, res);
    }

    // 인증 처리
    public void setAuthentication(Long userId, String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(userId, username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(Long userId, String username) {
        UserDetails userDetails = userDetailsService.getUser(userId, username);
        return new UsernamePasswordAuthenticationToken(userDetails, null,
            userDetails.getAuthorities());
    }


}
