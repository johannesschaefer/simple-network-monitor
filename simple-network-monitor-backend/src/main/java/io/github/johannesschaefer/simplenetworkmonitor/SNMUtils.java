package io.github.johannesschaefer.simplenetworkmonitor;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import io.github.johannesschaefer.simplenetworkmonitor.entities.Host;
import org.apache.commons.text.StringSubstitutor;

import java.util.Map;

public class SNMUtils {
    private SNMUtils() {}

    public static String[] prepareCmd(String cmd, Host host, Map<String, String> additionalProps, Map<String, String> secretProps) {
        Map<String, String> paras = Maps.newHashMap();
        paras.put("hostname", Strings.emptyToNull(host.getHostname()));
        paras.put("ip_v4", Strings.emptyToNull(host.getIpv4()));
        paras.put("ip_v6", Strings.emptyToNull(host.getIpv6()));
        paras.put("host_name", Strings.emptyToNull(host.getName()));
        paras.put("host_description", Strings.emptyToNull(host.getDescription()));
        paras.putAll(host.getProperties());
        paras.putAll(additionalProps);
        paras.putAll(deobfuscate(host.getSecretProperties()));
        paras.putAll(deobfuscate(secretProps));
        StringSubstitutor ss = new StringSubstitutor(paras);
        ss.setValueDelimiter(":");
        ss.setEnableSubstitutionInVariables(true);
        return ss.replace(cmd).split(" ");
    }

    private static Map<String, String> deobfuscate(Map<String, String> props) {
        return props;
    }
}
