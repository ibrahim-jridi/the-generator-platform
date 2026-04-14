package com.pfe.config;

import com.pfe.domain.enumeration.ErrorStatus;
import com.pfe.domain.enumeration.ErrorType;
import com.pfe.dto.ErrorAuditDTO;
import com.pfe.services.KafkaProducerService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Component
public class ErrorAuditFilter implements Filter {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        ContentCachingResponseWrapper responseCacheWrapperObject = new ContentCachingResponseWrapper((HttpServletResponse) response);
        chain.doFilter(request, responseCacheWrapperObject);
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String url = ((HttpServletRequest) request).getRequestURI();
        ErrorAuditDTO errorAudit = new ErrorAuditDTO();
        errorAudit.setUrl(url);
        errorAudit.setMethod(((HttpServletRequest) request).getMethod());
        errorAudit.setErrorStatus(ErrorStatus.PENDING);
        errorAudit.setDescription(httpServletResponse.getStatus() + ((HttpServletRequest) request).getRequestURI());
        if(((HttpServletResponse) response).getStatus() == 500){
            errorAudit.setErrorType(ErrorType.SYSTEM);
            this.kafkaProducerService.sendKafkaEventErrorAudit(errorAudit);
        }
        else if(((HttpServletResponse) response).getStatus() == 404){
            errorAudit.setErrorType(ErrorType.NOT_FOUND);
            this.kafkaProducerService.sendKafkaEventErrorAudit(errorAudit);
        }
        else if(((HttpServletResponse) response).getStatus() == 403){
            errorAudit.setErrorType(ErrorType.SECURITY);
            this.kafkaProducerService.sendKafkaEventErrorAudit(errorAudit);
        }
        responseCacheWrapperObject.copyBodyToResponse();
    }

    @Override
    public void destroy() {
        // Destruction du filtre
    }
}

