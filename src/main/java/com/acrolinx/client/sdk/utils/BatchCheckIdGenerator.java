package com.acrolinx.client.sdk.utils;

import java.util.UUID;

public class BatchCheckIdGenerator {

    private BatchCheckIdGenerator(String integrationShortName) {

    }

    public static String getId(String integrationShortName) {
        String uuid = UUID.randomUUID().toString();
        String name = "javaSDK";

        if (integrationShortName != null && !integrationShortName.isEmpty()) {
            name = integrationShortName.trim().replaceAll(" ", "-");
        }

        return "gen." + name + "." + uuid;
    }
}
