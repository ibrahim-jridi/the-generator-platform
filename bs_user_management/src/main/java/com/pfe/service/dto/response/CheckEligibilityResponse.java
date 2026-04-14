package com.pfe.service.dto.response;

import org.springframework.http.HttpHeaders;

import java.io.Serializable;
import java.util.Objects;

public class CheckEligibilityResponse implements Serializable {

    private static final long serialVersionUID = 3436148915424641172L;

    boolean status ;
    HttpHeaders headers;

    public CheckEligibilityResponse(boolean status, HttpHeaders headers) {
        this.status = status;
        this.headers = headers;
    }

    public CheckEligibilityResponse() {
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckEligibilityResponse that = (CheckEligibilityResponse) o;
        return status == that.status && Objects.equals(headers, that.headers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, headers);
    }

    @Override
    public String toString() {
        return "CheckEligibilityResponse{" +
            "status=" + status +
            ", headers=" + headers +
            '}';
    }

}
