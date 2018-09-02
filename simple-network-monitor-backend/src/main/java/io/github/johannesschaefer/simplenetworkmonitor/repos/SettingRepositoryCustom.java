package io.github.johannesschaefer.simplenetworkmonitor.repos;

import io.github.johannesschaefer.simplenetworkmonitor.entities.Setting;

public interface SettingRepositoryCustom {
    String getValue(Setting s);
}
