package uk.org.ca.stub.simulator.rest.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.org.ca.stub.simulator.rest.api.JwksApi;
import uk.org.ca.stub.simulator.rest.model.JwkSetsResponse;

@RestController
@RequestMapping("${openapi.mergedSpec.base-path:}")
public class JwksApiController implements JwksApi {

    @Override
    public ResponseEntity<JwkSetsResponse> getJwks(HttpServletRequest request) {
        return new ResponseEntity<>(new JwkSetsResponse(), HttpStatus.OK);
    }
}
