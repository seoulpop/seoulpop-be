package com.ssafy.seoulpop.config;

import static com.ssafy.seoulpop.exception.ErrorCode.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.ssafy.seoulpop.exception.BaseException;
import com.ssafy.seoulpop.member.service.TokenService;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final TokenService tokenService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        try {
            String token = tokenService.resolveToken((HttpServletRequest) request).replace("Bearer ", "");

            if (tokenService.validateToken(token)) { // access_token 유효할 때
                Authentication authentication = tokenService.readAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else { // access_token 유효하지 않을 때 재발급
                String accessToken =
                    tokenService.reissueAccessToken((HttpServletRequest) request,
                        (HttpServletResponse) response).accessToken();
                ((HttpServletResponse) response).setHeader("Authorization", accessToken);
                Authentication authentication = tokenService.readAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (BaseException e) {
            request.setAttribute("errorCode", e.getErrorCode());
            request.setAttribute("httpStatus", e.getErrorCode().getHttpStatus());
        } catch (SignatureException | MalformedJwtException e) {
            request.setAttribute("errorCode", UNAUTHORIZED);
            request.setAttribute("httpStatus", HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorCode", INTERNAL_SERVER_ERROR);
            request.setAttribute("httpStatus", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        chain.doFilter(request, response);
    }
}