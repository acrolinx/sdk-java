package com.acrolinx.client.sdk.http;

import java.util.concurrent.Callable;

public class JsonResponse implements Callable<String> {

    private String jsonString;

    public JsonResponse(String jsonString) {
        this.jsonString = jsonString;
    }

    @Override
    public String call() throws Exception {
        return jsonString;
    }
}
