package uk.org.ca.stub.simulator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uk.org.ca.stub.simulator.entity.User;
import uk.org.ca.stub.simulator.repository.UserRepository;
import uk.org.ca.stub.simulator.rest.exception.NotFoundException;


@Service
public class TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    private final UserRepository userRepository;

    public TokenService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User retrievePAT(String uat) {
        logger.debug(">>> Retrieving token for user {}", uat);
        var userFound = userRepository.findByUat(uat)
                .orElseThrow(() -> new NotFoundException("User not found"));
        logger.trace(">>> Found user {}", userFound);
        return userFound;
    }
}
