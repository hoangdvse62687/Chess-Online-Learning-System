package com.chess.chessapi.security;

import com.chess.chessapi.constants.AppMessage;
import com.chess.chessapi.exceptions.AccessDeniedException;
import com.chess.chessapi.services.RedisUserPrincipleService;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private RedisUserPrincipleService redisUserPrincipleService;

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                Long userId = tokenProvider.getUserIdFromToken(jwt);

                UserDetails userDetails = this.redisUserPrincipleService.find(userId);
                if( userDetails == null){
                    userDetails = this.customUserDetailsService.loadUserById(userId);
                }
                UserPrincipal userPrincipal = (UserPrincipal) userDetails;
                this.redisUserPrincipleService.save(userPrincipal);

                if(userPrincipal.isStatus()){
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }else {
                    throw new AccessDeniedException(AppMessage.PERMISSION_DENY_MESSAGE);
                }
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String chessToken = request.getHeader("Authorization");
        if (StringUtils.hasText(chessToken) && chessToken.startsWith("Chess ")) {
            return chessToken.substring(6, chessToken.length());
        }
        return null;
    }
}
