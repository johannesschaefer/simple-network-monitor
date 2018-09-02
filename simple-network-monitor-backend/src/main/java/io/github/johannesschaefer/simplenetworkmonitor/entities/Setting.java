package io.github.johannesschaefer.simplenetworkmonitor.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
public class Setting {
    @Id
    @NonNull
    private String name;
    private String displayName;
    private String description;
    private String value;
    @NonNull
    private String type;
    private String unit;
    private Long min;
    private Long max;
    private Long maxLength;
}