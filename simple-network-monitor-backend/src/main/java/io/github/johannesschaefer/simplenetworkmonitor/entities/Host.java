package io.github.johannesschaefer.simplenetworkmonitor.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Host {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NonNull
    @Column(unique=true, nullable = false)
    private String name;

    private String description;

    private String hostname;

    private String ipv4;

    private String ipv6;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "host", orphanRemoval = true)
    @Builder.Default
    @JsonManagedReference
    private List<Sensor> sensors = Lists.newArrayList();

    @ManyToMany
    @Builder.Default
    private List<Command> commands = Lists.newArrayList();

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private Map<String, String> properties = Maps.newHashMap();

    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @Builder.Default
    private Map<String, String> secretProperties = Maps.newHashMap();

    @Version
    private Long version;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    protected Date creationDate;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    protected Date lastModifiedDate;
}
