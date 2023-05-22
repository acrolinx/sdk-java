/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.client.sdk.http;

public class AcrolinxResponse
{
    private String result;
    private int status;

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }
}
