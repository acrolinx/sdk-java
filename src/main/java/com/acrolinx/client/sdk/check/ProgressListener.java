/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.check;

import com.acrolinx.client.sdk.Progress;

public interface ProgressListener
{
    void onProgress(Progress progress);
}
