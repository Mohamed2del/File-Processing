package com.example.orange.configurations.authentication.config;

import com.example.orange.entites.AuditTrail;
import com.example.orange.repositories.AuditTrailRepository;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@WebFilter("/*")
public class AuditTrailFilter implements Filter {

    private final AuditTrailRepository auditTrailRepository;

    public AuditTrailFilter(AuditTrailRepository auditTrailRepository) {
        this.auditTrailRepository = auditTrailRepository;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        // Get query parameters
        String queryString = httpServletRequest.getQueryString();
        if (queryString == null) {
            queryString = "";
        }

        // Get request parameters from the body (if form data is submitted)
        Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
        String requestBodyParams = parameterMap.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + String.join(",", entry.getValue()))
                .collect(Collectors.joining("&"));

        // Concatenate query params and request body parameters into one string
        String requestData = "Query Params: " + queryString + " | Body Params: " + requestBodyParams;


        // Continue with the request processing (allow the next filter, like the JWT filter)
        chain.doFilter(request, response);

        // After request processing, log details
        String userName = (httpServletRequest.getUserPrincipal() != null) ?
                httpServletRequest.getUserPrincipal().getName() : "Anonymous";
        String actionType = httpServletRequest.getMethod();
        String resource = httpServletRequest.getRequestURI();
        int responseStatus = httpServletResponse.getStatus();

        // Create the AuditTrail entity and save it
        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setUserName(userName);
        auditTrail.setActionType(actionType);
        auditTrail.setRequestData(requestData);
        auditTrail.setResource(resource);
        auditTrail.setEndpointUri(resource);
        auditTrail.setActionTimestamp(LocalDateTime.now());
        auditTrail.setResponseStatus(responseStatus == 200 ? "SUCCESS" : "FAILED");

        auditTrailRepository.save(auditTrail);
    }

    @Override
    public void destroy() {
    }
}