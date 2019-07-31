/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk;

import com.acrolinx.client.sdk.platform.Link;

import java.util.List;

public class ContentAnalysisDashboard {

    private List<Link> links = null;

    public ContentAnalysisDashboard(List<Link> links) {
        this.links = links;
    }


    public List<Link> getLinks() {
        return links;
    }
}
