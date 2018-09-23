package io.github.johannesschaefer.simplenetworkmonitor;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import io.github.johannesschaefer.simplenetworkmonitor.entities.Host;
import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SNMUtils {
    private static Logger log = LoggerFactory.getLogger(SNMUtils.class);

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

// from https://stackoverflow.com/a/40347087
    public static String getMacAddrHost(String host) {
        try {
            boolean ok = ping(host);

            if (ok) {
                InetAddress address = InetAddress.getByName(host);
                String ip = address.getHostAddress();
                return arp(ip);
            }
        }
        catch(InterruptedException | IOException e) {
            log.warn("Can't get the MAC address for host {}.", host);
        }

        return "";
    }

    private static boolean ping(String host) throws IOException, InterruptedException {
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");

        ProcessBuilder processBuilder = new ProcessBuilder("ping", isWindows ? "-n" : "-c", "1", host);
        Process proc = processBuilder.start();

        int returnVal = proc.waitFor();
        return returnVal == 0;
    }

    private static String arp(String ip) throws IOException {
        Process p = Runtime.getRuntime().exec("arp -a " + ip);
        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = input.readLine()) != null) {
            String mac = getMacAddr(line);
            if (!Strings.isNullOrEmpty(mac)) {
                return mac;
            }
        }
        return "";
    }

    static String getMacAddr(String mac) {
        log.warn("searching mac in string '{}'", mac);

        if (mac == null) {
            return "";
        }

        Pattern p = Pattern.compile("(?:[0-9a-fA-F]:?){12}");
        Matcher m = p.matcher(mac);
        if (m.find()) {
            return m.group();
        }
        else {
            return "";
        }
    }
// end https://stackoverflow.com/a/40347087

}
