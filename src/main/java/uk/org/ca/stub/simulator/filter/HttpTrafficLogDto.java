package uk.org.ca.stub.simulator.filter;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Builder
@Setter
@Getter
public class HttpTrafficLogDto {
    // request
    String requestId;
    String requestUri;
    String requestMethod;
    String pathInfo;
    String buildVersion;
    Map<String, String> headers;
    Map<String, String[]> requestParameters;
    Map<String, Object> requestBodyParameters;
    MtlsInfoDto mtlsInfo;

    //response

    int responseStatus;
    String responseContentType;
    String responseContentEncoding;
    int responseContentLength;
    String responseText;
    Map<String, String> responseJson;


    @Override
    public String toString() {
        var indentedKeyValue = "       %s: %s%n";
        ArrayList<String> res = new ArrayList<>();
        if(this.buildVersion != null && !this.buildVersion.isEmpty()){
            res.add(String.format("C&A Stub v%s. ", this.buildVersion));
        }
        res.add(String.format("Processing %s request to %s with x-request-id: %s%n", requestMethod, requestUri, requestId));
        res.add("REQUEST DETAILS \n");
        res.add(String.format("   Transport information: %s%n", mtlsInfo));
        res.add(String.format("   Request ID: %s%n", requestId));
        res.add(String.format("   Request URI: %s%n", requestUri));
        res.add(String.format("   Request Method: %s%n", requestMethod));
        if (pathInfo != null) {
            res.add(String.format("   Request Path Info: %s%n", pathInfo));
        }
        if (headers != null && !headers.isEmpty()) {
            res.add("    * Headers: \n");
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                res.add(String.format(indentedKeyValue, entry.getKey(), entry.getValue()));
            }
        } else {
            res.add("    * Headers: NONE\n");
        }

        if (requestParameters != null && !requestParameters.isEmpty()) {
            res.add("    * Request Parameters: \n");
            for (Map.Entry<String, String[]> entry : requestParameters.entrySet()) {
                res.add(String.format(indentedKeyValue, entry.getKey(), entry.getValue()[0]));
            }
        }

        if (requestBodyParameters != null && !requestBodyParameters.isEmpty()) {
            res.add("    * Request Body Parameters: \n");
            for (Map.Entry<String, Object> entry : requestBodyParameters.entrySet()) {
                var value = entry.getValue();
                if(value == null ){
                    value = "NULL";
                }
                res.add(String.format(indentedKeyValue, entry.getKey(), value));
            }
        }

        if ((requestParameters == null || requestParameters.isEmpty()) && (requestBodyParameters == null || requestBodyParameters.isEmpty())) {
            res.add("    * Input Parameters: NONE\n");
        }

        res.add("\nRESPONSE DETAILS \n");
        res.add(String.format("   Response Status: %s%n", responseStatus));
        res.add(String.format("   Response Content Type: %s%n", responseContentType));
        res.add(String.format("   Response Content Encoding: %s%n", responseContentEncoding));
        res.add(String.format("   Response Content Length: %s%n", responseContentLength));
        if (responseText != null) {
            res.add(String.format("   Response Text: %s%n", responseText));
        }
        if (responseJson != null && !responseJson.isEmpty()) {
            res.add("   * Response JSON:\n");
            for (Map.Entry<String, String> entry : responseJson.entrySet()) {
                res.add(String.format(indentedKeyValue, entry.getKey(), entry.getValue()));
            }
        }

        return String.join("", res);
    }

}

class MtlsInfoDto {
    final String clientAuthConfig;
    final List<String> clientCn;
    final boolean isSecure;
    final String protocol;
    final String schema;

    public MtlsInfoDto(HttpServletRequest request, String clientAuthConfig) {
        this.clientAuthConfig = clientAuthConfig;
        X509Certificate[] certChain = (X509Certificate[]) request.getAttribute("jakarta.servlet.request.X509Certificate");
        if (certChain != null && certChain.length > 0) {
            clientCn = Arrays.stream(certChain).map(c -> c.getSubjectX500Principal().getName()).toList();
        } else {
            clientCn = List.of();
        }
        this.isSecure = request.isSecure();
        Object maybeSecureProtocolVersion = request.getAttribute("org.apache.tomcat.util.net.secure_protocol_version");
        if (maybeSecureProtocolVersion != null) {
            this.protocol = maybeSecureProtocolVersion.toString();
        } else {
            this.protocol = "NO SECURE PROTOCOL";
        }
        this.schema = request.getScheme();
    }

    @Override
    public String toString() {
        return (isSecure ? "secured" : "not secured") + " (" +
                schema + " " +
                protocol + "). " +
                "Client authentication: " + clientAuthConfig.toUpperCase() + ". " +
                "Client CNs: " + (clientCn.isEmpty() ? "EMPTY" : clientCn);
    }
}
