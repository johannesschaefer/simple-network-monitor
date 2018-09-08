package io.github.johannesschaefer.simplenetworkmonitor.entities;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Setting {
    @Id
    @NonNull
    private String name;
    @NonNull
    @Column(nullable = false)
    private String displayName;
    private String description;
    private String value;
    @NonNull
    @Column(nullable = false)
    private String type;
    private String unit;
    private Long min;
    private Long max;
    private Long maxLength;
    @NonNull
    @Builder.Default
    @Column(nullable = false)
    private boolean required = false;

    @Version
    private Long version;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    protected Date creationDate;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    protected Date lastModifiedDate;
}