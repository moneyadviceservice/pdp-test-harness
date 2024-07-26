package uk.org.ca.stub.simulator.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    @Size(min = 1)
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
        return "User" +
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
