package io.github.johannesschaefer.simplenetworkmonitor.dto;

import io.github.johannesschaefer.simplenetworkmonitor.entities.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandResult {
    private Status status;
    private String message;
}
