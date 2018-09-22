package io.github.johannesschaefer.simplenetworkmonitor.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Sensor {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NonNull
    @Column(nullable = false)
    private String name;

    @NonNull
    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;

    @NonNull
    @Builder.Default
    @Column(nullable = false, name = "intval")
    private long interval = 60000;

    @NonNull
    @ManyToOne(optional = false)
    @JsonBackReference
    private Host host;

    @NonNull
    @ManyToOne(optional = false)
    private Command command;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sensor", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Sample> samples = Lists.newArrayList();

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private Map<String, String> properties = Maps.newHashMap();

    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @Builder.Default
    private Map<String, String> secretProperties = Maps.newHashMap();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sensor", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @Builder.Default
    private List<SampleType> sampleTypes = Lists.newArrayList();

    public Sensor(Host host, Command command, boolean active, long interval) {
        this.name = host.getName() + "_" + command.getName();
        this.command = command;
        this.host = host;
        this.active = active;
        this.interval = interval;
    }
}
