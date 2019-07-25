package com.acrolinx.client.sdk.http;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.Future;

public interface AcrolinxResponse {
    String result;
    int status;
}
