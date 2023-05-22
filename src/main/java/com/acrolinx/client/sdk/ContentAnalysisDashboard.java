/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.client.sdk;

import java.util.List;

import com.acrolinx.client.sdk.platform.Link;

public class ContentAnalysisDashboard
{
    private final List<Link> links;

    public ContentAnalysisDashboard(List<Link> links)
    {
        this.links = links;
    }

    public List<Link> getLinks()
    {
        return links;
    }
}
