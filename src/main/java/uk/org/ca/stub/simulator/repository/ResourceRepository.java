package uk.org.ca.stub.simulator.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import uk.org.ca.stub.simulator.entity.RegisteredResource;

import java.util.Optional;

public interface ResourceRepository extends JpaRepository<RegisteredResource, Long> {
    Optional<RegisteredResource> findByResourceId(String resourceId);

    Optional<RegisteredResource> findByName(String name);

    Optional<RegisteredResource> findByInboundRequestId(String inboundRequestId);

    Optional<RegisteredResource> findByRpt(String rpt);

    Optional<RegisteredResource> findFirstByPatOrderByCreatedAtDesc(String pat);
}
