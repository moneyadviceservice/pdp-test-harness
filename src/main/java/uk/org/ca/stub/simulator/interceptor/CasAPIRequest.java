package uk.org.ca.stub.simulator.interceptor;

import org.springframework.http.HttpMethod;

import java.util.Objects;

public record CasAPIRequest(
        /**
         * The URI excluded from requiring an `x-request-id` header
         */
        String path,
        /**
         * The HTTP Method excluded from requiring an `x-request-id` header
         */
        HttpMethod method) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CasAPIRequest that = (CasAPIRequest) o;
        return (Objects.equals(path, that.path) || path.startsWith(that.path)) && Objects.equals(method, that.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, method);
    }
}
