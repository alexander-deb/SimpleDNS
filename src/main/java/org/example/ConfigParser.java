package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class ConfigParser {
    public static String readResourceFileAsString(String fileName) throws IOException {
        try (InputStream inputStream = ConfigParser.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IOException("Resource file not found: " + fileName);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        }
    }

    public static Map<String, List<String>> parseDNSRecords(String dnsRecords) {
        Map<String, List<String>> domainToIps = new HashMap<>();

        Scanner scanner = new Scanner(dnsRecords);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                String[] parts = line.split("\\s+");
                if (parts.length >= 4 && "IN".equals(parts[1]) && "A".equals(parts[2])) {
                    String domainName = parts[0];
                    String ipAddress = parts[3];

                    domainToIps.computeIfAbsent(domainName, k -> new ArrayList<>()).add(ipAddress);
                }
            }
        }
        scanner.close();

        return domainToIps;
    }

    public static Map<String, List<String>> getConfigMap(String filename) throws IOException {
        String dnsRecords = readResourceFileAsString(filename);
        Map<String, List<String>> parsedRecords = parseDNSRecords(dnsRecords);
        return parsedRecords;
    }

}
