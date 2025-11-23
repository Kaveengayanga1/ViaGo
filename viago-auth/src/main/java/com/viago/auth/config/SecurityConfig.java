package com.viago.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


//Authenticate and set authorization rules
//provide password encoder instances,
//configure stateless session management,
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private UserDetailsService userDetailsService;
    private JwtFilter jwtFilter;
    private OAuth2UserService<org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest, OAuth2User> oAuth2UserService;
    private OAuth2RoutingSuccessHandler oAuth2RoutingSuccessHandler;
    private OAuth2FailureHandler oAuth2FailureHandler;

    public SecurityConfig(UserDetailsService userDetailsService,
                         JwtFilter jwtFilter,
                         OAuth2UserService<org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest, OAuth2User> oAuth2UserService,
                         OAuth2RoutingSuccessHandler oAuth2RoutingSuccessHandler,
                         OAuth2FailureHandler oAuth2FailureHandler) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
        this.oAuth2UserService = oAuth2UserService;
        this.oAuth2RoutingSuccessHandler = oAuth2RoutingSuccessHandler;
        this.oAuth2FailureHandler = oAuth2FailureHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
//                .cors(cors -> cors
//                        .configurationSource(request -> {
//                            var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
//                            corsConfiguration.setAllowedOrigins(List.of("http://localhost:5500"));
//                            corsConfiguration.setAllowedMethods(List.of("GET","POST","PUT","DELETE"));
//                            corsConfiguration.setAllowedHeaders(List.of("Authorization","Content-Type"));
//                            corsConfiguration.setExposedHeaders(List.of("Authorization","Content-Type"));
//                            return corsConfiguration;
//                        }))
                .csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/auth/**").permitAll() //access for everyone
                        .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll() // OAuth2 endpoints
                        .requestMatchers("/user/**").hasAnyRole("RIDER","ADMIN")
//                        .requestMatchers("/event/get-all-events").hasAnyRole("USER","ADMIN")
//                        .requestMatchers("/event/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService))
                        .successHandler(oAuth2RoutingSuccessHandler)
                        .failureHandler(oAuth2FailureHandler))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    //Total auth security disable option
    /*
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //generated for disable entire auth
        return http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for APIs
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // 🔓 Allow all requests
                )
                .build(); // No HTTP basic, no session
    }
     */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
            return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder());
        provider.setUserDetailsService(userDetailsService);

        return provider;
    }
}

