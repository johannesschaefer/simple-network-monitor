package io.github.johannesschaefer.simplenetworkmonitor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HostOverview {
    private Long total;
    private Long ok;
    private Long warn;
    private Long critical;
    private Long unknown;
}
