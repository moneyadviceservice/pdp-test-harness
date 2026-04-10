package uk.org.ca.stub.simulator.rest.api;

import java.io.IOException;

import org.springframework.web.context.request.NativeWebRequest;

import jakarta.servlet.http.HttpServletResponse;

public class ApiUtil {
    private ApiUtil() {}

    public static void setExampleResponse(NativeWebRequest req, String contentType, String example) {
        try {
            HttpServletResponse res = req.getNativeResponse(HttpServletResponse.class);
            if (res == null) return;
            res.setCharacterEncoding("UTF-8");
            res.addHeader("Content-Type", contentType);
            res.getWriter().print(example);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
