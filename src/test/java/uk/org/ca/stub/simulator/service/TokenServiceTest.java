package uk.org.ca.stub.simulator.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.org.ca.stub.simulator.configuration.dbinitializer.UserDbInitializer;
import uk.org.ca.stub.simulator.rest.exception.NotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class TokenServiceTest {

    @Autowired
    TokenService cut;

    @Test
    void testsAll() {
        var first = UserDbInitializer.users.getFirst();
        assertAll("initialization worked",
                () -> assertEquals(first.getPat(), cut.retrievePAT(first.getUat()).getPat()),
                () -> assertThrows(NotFoundException.class, () -> cut.retrievePAT("nonexistent").getPat())
        );
    }
}
