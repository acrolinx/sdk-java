/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.check;

import javax.annotation.Nullable;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;

public interface Document
{
    String getContent() throws AcrolinxException;

    @Nullable
    ExternalContent getExternalContent();
}
