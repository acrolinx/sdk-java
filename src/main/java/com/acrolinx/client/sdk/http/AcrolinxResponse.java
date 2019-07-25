package com.acrolinx.client.sdk.http;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.Future;

public class AcrolinxResponse {
    private String result = "";
    private int status = 0;
    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
}
