/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.client.sdk.check;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import javax.annotation.Nullable;

public interface Document {
  String getContent() throws AcrolinxException;

  @Nullable
  ExternalContent getExternalContent();
}
