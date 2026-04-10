package uk.org.ca.stub.simulator.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cas_registered_resource")
@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class RegisteredResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String resourceId;
    private String name;
    private String description;
    private String pat;

    @Column(length = 1000, unique = true)
    private String rpt;

    @Column(unique = true)
    private String inboundRequestId;

    @ElementCollection
    @NotNull
    @Fetch(value = FetchMode.JOIN)
    List<Scope> resourceScopes;

    @Setter
    private String matchStatus;

    private String friendlyName;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public String toString() {
        return "RegisteredResource" +
                (friendlyName ==null || friendlyName.isBlank() ? "" : "(" + friendlyName + ")") +
                "{" +
                "resourceId='" + resourceId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", pat='" + pat + '\'' +
                ", rpt='" + rpt + '\'' +
                ", inboundRequestId='" + inboundRequestId + '\'' +
                ", resourceScopes='" + '[' + resourceScopes.stream().map(Enum::toString).collect(Collectors.joining(", ")) + ']' + '\'' +
                ", matchStatus='" + matchStatus + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
