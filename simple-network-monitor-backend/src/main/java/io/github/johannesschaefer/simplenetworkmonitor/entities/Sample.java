package io.github.johannesschaefer.simplenetworkmonitor.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Sample {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @NonNull
    @ManyToOne()
    private SampleType type;
    @NonNull
    @Builder.Default
    private Date time = new Date();
    private Double value;
    private Double warn;
    private Double critical;
    private Double min;
    private Double max;
    private String unit;
    @Lob
    private String msg;
    @NonNull
    @Enumerated(EnumType.STRING)
    private Status status;
    @NonNull
    @ManyToOne()
    private Sensor sensor;
}
