package uk.org.ca.stub.simulator.rest.controller;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

@Configuration
public class CustomRestTemplateConfiguration {

    @Value("${server.ssl.trust-store}")
    private Resource trustStore;

    @Value("${server.ssl.trust-store-password}")
    private String trustStorePassword;

    @Bean(name = "trusted-test-client")
    public RestTemplate trustedRestTemplate(SslBundles sslBundles) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, KeyManagementException {
        SslBundle testClientSslBundle = sslBundles.getBundle("trusted-test-client");
        return buildRestTemplate(testClientSslBundle);
    }

    @Bean(name = "untrusted-test-client")
    public RestTemplate untrustedRestTemplate(SslBundles sslBundles) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, KeyManagementException {
        SslBundle testClientSslBundle = sslBundles.getBundle("untrusted-test-client");
        return buildRestTemplate(testClientSslBundle);
    }

    private RestTemplate buildRestTemplate(SslBundle testClientSslBundle) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, CertificateException, IOException, UnrecoverableKeyException {
        SSLContext sslContext = new SSLContextBuilder()
                .setProtocol("TLS")
                .loadKeyMaterial(
                        testClientSslBundle.getStores().getKeyStore(),
                        testClientSslBundle.getStores().getKeyStorePassword().toCharArray()
                )
                .loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray())
                .build();

        HttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext))
                .build();

        CloseableHttpClient httpClient = HttpClients
                .custom()
                .setConnectionManager(cm)
                .build();

        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setErrorHandler(getDoNothingErrorHandler());
        return restTemplate;
    }

    private static ResponseErrorHandler getDoNothingErrorHandler() {
        return new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false; // prevents the request to fail
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                // do nothing, or something
            }

            @Override
            public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
                // do nothing, or something
            }
        };
    }
}
