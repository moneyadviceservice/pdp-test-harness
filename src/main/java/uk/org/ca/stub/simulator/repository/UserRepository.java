package uk.org.ca.stub.simulator.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import uk.org.ca.stub.simulator.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUat(String uat);

    Boolean existsByPat(String pat);
}
