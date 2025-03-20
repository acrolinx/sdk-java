/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.client.sdk.check;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;

public interface Document {
  String getContent() throws AcrolinxException;

  ExternalContent getExternalContent();
}
