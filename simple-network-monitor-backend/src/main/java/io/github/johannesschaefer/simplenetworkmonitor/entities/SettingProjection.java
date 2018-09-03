package io.github.johannesschaefer.simplenetworkmonitor.entities;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(types = Setting.class)
public interface SettingProjection {
    String getName();

    String getDisplayName();

    String getDescription();

    @Value("#{@settingRepository.getValue(target)}")
    String getValue();
    String getType();

    String getUnit();

    Long getMin();

    Long getMax();

    Long getMaxLength();

    boolean isRequired();
}
