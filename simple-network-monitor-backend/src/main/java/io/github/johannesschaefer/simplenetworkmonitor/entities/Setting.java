package io.github.johannesschaefer.simplenetworkmonitor.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
    private String description;
    @NonNull
    private String value;
}