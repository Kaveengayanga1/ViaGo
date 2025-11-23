package com.viago.auth.config;

import com.viago.auth.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class OAuth2RoutingSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final OAuth2LoginSuccessHandler loginHandler;
    private final OAuth2SignupSuccessHandler signupHandler;
    private final UserRepository userRepository;

    public OAuth2RoutingSuccessHandler(OAuth2LoginSuccessHandler loginHandler,
                                       OAuth2SignupSuccessHandler signupHandler,
                                       UserRepository userRepository) {
        this.loginHandler = loginHandler;
        this.signupHandler = signupHandler;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                       HttpServletResponse response,
                                       Authentication authentication) throws IOException, jakarta.servlet.ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        
        // Check referer to determine flow type (from /auth/google/login or /auth/google/signup)
        String referer = request.getHeader("Referer");
        boolean isSignupFlow = referer != null && referer.contains("/auth/google/signup");
        boolean isLoginFlow = referer != null && referer.contains("/auth/google/login");
        
        // Route based on referer or check if user exists
        if (isSignupFlow) {
            log.info("Routing to signup handler based on referer");
            signupHandler.onAuthenticationSuccess(request, response, authentication);
        } else if (isLoginFlow) {
            log.info("Routing to login handler based on referer");
            loginHandler.onAuthenticationSuccess(request, response, authentication);
        } else if (email != null && userRepository.findByEmailAndProvider(email, "google").isPresent()) {
            log.info("User exists with Google provider, routing to login handler");
            loginHandler.onAuthenticationSuccess(request, response, authentication);
        } else {
            log.info("User does not exist with Google provider, routing to signup handler");
            signupHandler.onAuthenticationSuccess(request, response, authentication);
        }
    }
}

