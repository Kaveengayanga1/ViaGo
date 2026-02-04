//package com.viago.adminservices.config;
//
//import jakarta.servlet.Filter;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.FilterConfig;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.Ordered;
//
//import java.io.IOException;
//
///**
// * CORS configuration using FilterRegistrationBean for guaranteed priority.
// */
//@Configuration
//public class CorsConfig {
//
//    @Bean
//    public FilterRegistrationBean<Filter> corsFilterRegistration() {
//        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new CorsServletFilter());
//        registrationBean.addUrlPatterns("/*");
//        registrationBean.setName("corsFilter");
//        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
//        return registrationBean;
//    }
//
//    public static class CorsServletFilter implements Filter {
//
//        @Override
//        public void init(FilterConfig filterConfig) throws ServletException {
//            // No initialization needed
//        }
//
//        @Override
//        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//                throws IOException, ServletException {
//
//            HttpServletResponse httpResponse = (HttpServletResponse) response;
//            HttpServletRequest httpRequest = (HttpServletRequest) request;
//
//            // Set CORS headers
//            httpResponse.setHeader("Access-Control-Allow-Origin", "*");
//            httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS");
//            httpResponse.setHeader("Access-Control-Allow-Headers", "*");
//            httpResponse.setHeader("Access-Control-Max-Age", "3600");
//
//            // Handle preflight requests
//            if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
//                httpResponse.setStatus(HttpServletResponse.SC_OK);
//                return;
//            }
//
//            chain.doFilter(request, response);
//        }
//
//        @Override
//        public void destroy() {
//            // No cleanup needed
//        }
//    }
//}


package com.viago.adminservices.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.io.IOException;

@Configuration
public class CorsConfig {

    @Bean
    public FilterRegistrationBean<Filter> globalCorsFilter() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();

        // Register the inner class filter defined below
        registrationBean.setFilter(new CorsServletFilter());

        registrationBean.addUrlPatterns("/*");
        // We use a unique name here to avoid collision with any other default "corsFilter"
        registrationBean.setName("globalCorsFilter");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }

    public static class CorsServletFilter implements Filter {

        @Override
        public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
                throws IOException, ServletException {

            HttpServletResponse response = (HttpServletResponse) res;
            HttpServletRequest request = (HttpServletRequest) req;

            // Logic adapted from your second file (better support for Credentials)
            String origin = request.getHeader("Origin");
            if (origin != null && !origin.isEmpty()) {
                response.setHeader("Access-Control-Allow-Origin", origin);
            } else {
                response.setHeader("Access-Control-Allow-Origin", "*");
            }

            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept, X-Requested-With");
            response.setHeader("Access-Control-Expose-Headers", "Authorization");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Max-Age", "3600");

            // Handle preflight OPTIONS request
            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                response.setStatus(HttpServletResponse.SC_OK);
                return;
            }

            chain.doFilter(req, res);
        }
    }
}