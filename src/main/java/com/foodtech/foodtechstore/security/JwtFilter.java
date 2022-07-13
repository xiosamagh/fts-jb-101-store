package com.foodtech.foodtechstore.security;



import com.foodtech.foodtechstore.admin.exception.AdminNotExistException;
import com.foodtech.foodtechstore.auth.entity.CustomAdminDetails;
import com.foodtech.foodtechstore.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.springframework.util.StringUtils.hasText;

@Component
@Log
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    public static final String AUTHORIZATION = "Authorization";
    private final JwtProvider jwtProvider;
    private final AuthService authService;


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token = getTokenFromRequest((HttpServletRequest) servletRequest);


            try {
                if(token!=null && jwtProvider.validateToken(token)) {
                    String email = jwtProvider.getEmailFromToken(token);
                    CustomAdminDetails customAdminDetails = null;
                    customAdminDetails = authService.loadUserByEmail(email);
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(customAdminDetails,null,customAdminDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }catch (AdminNotExistException e) {
                e.printStackTrace();
            }
            filterChain.doFilter(servletRequest,servletResponse);
        }



    public static String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        if (hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
