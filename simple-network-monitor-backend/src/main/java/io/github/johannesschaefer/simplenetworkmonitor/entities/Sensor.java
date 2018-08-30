package io.github.johannesschaefer.simplenetworkmonitor.entities;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Component
public class Sensor {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NonNull
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @Column(nullable = false)
    @Builder.Default
    private long interval = 60000;

    //@NonNull
    @ManyToOne//(optional = false)
    private Host host;

    //@NonNull
    @OneToOne//(optional = false)
    private Command command;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sensor")
    @Builder.Default
    private List<Sample> samples = Lists.newArrayList();

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private Map<String, String> properties = Maps.newHashMap();

    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @Builder.Default
    private Map<String, String> secretProperties = Maps.newHashMap();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sensor")
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
