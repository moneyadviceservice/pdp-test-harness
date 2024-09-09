package uk.org.ca.stub.simulator.rest.model;

import java.util.Objects;

/**
 * IntrospectBody
 */

public class IntrospectBody {
    private String token;

    public IntrospectBody() {
        super();
    }

    public IntrospectBody(String token) {
        this.token = token;
    }

    public IntrospectBody token(String token) {
        this.token = token;
        return this;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        var introspectBody = (IntrospectBody) o;
        return Objects.equals(this.token, introspectBody.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("class IntrospectBody {\n");
        sb.append("    token: ").append(toIndentedString(token)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }


}
