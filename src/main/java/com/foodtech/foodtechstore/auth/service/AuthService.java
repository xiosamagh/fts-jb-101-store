package com.foodtech.foodtechstore.auth.service;

import com.foodtech.foodtechstore.admin.exception.AdminNotExistException;
import com.foodtech.foodtechstore.admin.model.AdminDoc;
import com.foodtech.foodtechstore.admin.repository.AdminRepository;
import com.foodtech.foodtechstore.auth.api.request.AuthRequest;
import com.foodtech.foodtechstore.auth.entity.CustomAdminDetails;
import com.foodtech.foodtechstore.auth.exceptions.AuthException;
import com.foodtech.foodtechstore.base.service.EmailSenderService;
import com.foodtech.foodtechstore.security.JwtFilter;
import com.foodtech.foodtechstore.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import javax.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
public class AuthService {
    private final AdminRepository adminRepository;
    private final JwtProvider jwtProvider;
    private final EmailSenderService emailSenderService;

    public CustomAdminDetails loadUserByEmail(String email) throws AdminNotExistException {
        AdminDoc adminDoc = adminRepository.findByEmail(email).orElseThrow(AdminNotExistException::new);
        return CustomAdminDetails.fromAdminEntityToCustomUserDetails(adminDoc);
    }

    public String auth(AuthRequest authRequest) throws AdminNotExistException, AuthException {
        AdminDoc adminDoc = adminRepository.findByEmail(authRequest.getEmail()).orElseThrow(AdminNotExistException::new);
        if (adminDoc.getPassword().equals(AdminDoc.hexPassword(authRequest.getPassword())) == false) {
            adminDoc.setFailLogin(adminDoc.getFailLogin()+1);
            adminRepository.save(adminDoc);

            if (adminDoc.getFailLogin() >= 5) {
                emailSenderService.sendEmailAlert(adminDoc.getEmail());
            }

            throw new AuthException();
        }

        if (adminDoc.getFailLogin() > 0) {
            adminDoc.setFailLogin(0);
            adminRepository.save(adminDoc);
        }


        String token = jwtProvider.generateToken(authRequest.getEmail());
        return token;
    }

    public static HttpServletRequest getCurrentHttpRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            return request;
        }
        return null;
    }

    public AdminDoc currentAdmin() throws AuthException {
        try {
            String email = jwtProvider.getEmailFromToken(JwtFilter.getTokenFromRequest(getCurrentHttpRequest()));
            AdminDoc adminDoc = adminRepository.findByEmail(email).orElseThrow(AdminNotExistException::new);
            return adminDoc;
        }
        catch (Exception e) {
            throw new AuthException();
        }

    }
}
