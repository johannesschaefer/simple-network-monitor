package io.github.johannesschaefer.simplenetworkmonitor.repos;

import com.google.common.base.Strings;
import io.github.johannesschaefer.simplenetworkmonitor.entities.Setting;

public class SettingRepositoryImpl implements SettingRepositoryCustom {
    @Override
    public String getValue(Setting s) {
        return s.getType().equals("password") && !Strings.isNullOrEmpty(s.getValue()) ? HostRepositoryImpl.SECRET_STRING : s.getValue();
    }
}
