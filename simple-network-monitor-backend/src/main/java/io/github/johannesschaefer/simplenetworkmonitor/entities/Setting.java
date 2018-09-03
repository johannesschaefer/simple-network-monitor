package io.github.johannesschaefer.simplenetworkmonitor.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Builder
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
}