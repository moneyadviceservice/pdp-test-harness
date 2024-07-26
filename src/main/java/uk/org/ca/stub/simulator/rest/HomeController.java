package uk.org.ca.stub.simulator.rest;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
public class HomeController {
    private final String mainTemplate;
    private static final String MAIN_PAGE = "index.html";

    public HomeController() {
        var mainResource = new ClassPathResource(MAIN_PAGE);
        mainTemplate = readTemplate(mainResource);
    }


    @GetMapping(path = {"/", "/index"}, produces = MediaType.TEXT_HTML_VALUE)
    public String index() {
        return mainTemplate.replace("$$WELCOME$$", "Welcome to the C&A Stub. The sections below can be accessed without authentication and without 'x-request-id'")
                .replace("$$SWAG$$", "swagger-ui-custom.html")
                .replace("$$H2-CONSOLE$$", "data");
    }


    private String readTemplate(Resource resource) {
        try (var bis = new BufferedInputStream(resource.getInputStream()); var buf = new ByteArrayOutputStream()) {
            for (int result = bis.read(); result != -1; result = bis.read()) {
                buf.write((byte) result);
            }
            return buf.toString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            return """
                    <html>
                        <body>
                            <h1>The main page C&A Stub cannot be retrieved. Re-build the project and re-run!</h1>
                        </body>
                    </html>
                    """;
        }
    }
}
