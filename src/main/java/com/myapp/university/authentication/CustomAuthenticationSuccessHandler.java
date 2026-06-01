package com.myapp.university.authentication;

import com.myapp.university.model.User;
import com.myapp.university.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final RequestCache requestCache = new HttpSessionRequestCache();
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final UserRepository userRepository;

    @Autowired
    public CustomAuthenticationSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

//        SavedRequest savedRequest = requestCache.getRequest(request, response);
//
//        if (savedRequest != null) {
//            redirectStrategy.sendRedirect(request, response, savedRequest.getRedirectUrl());
//            return;
//        }

        Set<String> authorities = new HashSet<>();
        for(GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            authorities.add(grantedAuthority.getAuthority());
        }

        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();


        //redirect logged-in user to their respective dashboard
        if (authorities.contains("ROLE_ADMIN")) {
            redirectStrategy.sendRedirect(request, response, "/admin");
        } else if (authorities.contains("ROLE_TEACHER")) {
            if (user.getTeacher() == null) {
                redirectStrategy.sendRedirect(request, response, "/teachers/create");
                return;
            }
            redirectStrategy.sendRedirect(request, response, "/teachers");
        } else if (authorities.contains("ROLE_STUDENT")) {
            if (user.getStudent() == null) {
                redirectStrategy.sendRedirect(request, response, "/students/create");
                return;
            }
            redirectStrategy.sendRedirect(request, response, "/students");
        }
//        } else {
//            redirectStrategy.sendRedirect(request, response, "/error");
//        }


    }
}
