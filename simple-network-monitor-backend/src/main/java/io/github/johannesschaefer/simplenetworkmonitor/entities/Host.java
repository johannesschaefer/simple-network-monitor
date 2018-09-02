package io.github.johannesschaefer.simplenetworkmonitor.entities;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Host {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NonNull
    @Column(unique=true)
    private String name;

    private String description;

    private String hostname;

    private String ipv4;

    private String ipv6;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "host")
    //@JoinColumn(name = "HOST_ID")
    @Builder.Default
    private List<Sensor> sensors = Lists.newArrayList();

    @ManyToMany()
    @Builder.Default
    private Set<Command> commands = Sets.newHashSet();

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private Map<String, String> properties = Maps.newHashMap();

    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @Builder.Default
    private Map<String, String> secretProperties = Maps.newHashMap();
}
