/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.check;

import java.util.List;

import com.google.gson.Gson;

/**
 * Only supported with Acrolinx Platform 2019.10 and newer.
 */
public class ExternalContent
{
    private final List<ExternalContentField> textReplacements;
    private final List<ExternalContentField> entities;
    private final List<ExternalContentField> ditaReferences;

    public ExternalContent(List<ExternalContentField> textReplacements, List<ExternalContentField> entities,
            List<ExternalContentField> ditaReferences)
    {
        this.textReplacements = textReplacements;
        this.entities = entities;
        this.ditaReferences = ditaReferences;
    }

    public List<ExternalContentField> getTextReplacements()
    {
        return textReplacements;
    }

    public List<ExternalContentField> getEntities()
    {
        return entities;
    }

    public List<ExternalContentField> getDitaReferences()
    {
        return ditaReferences;
    }

    @Override
    public String toString()
    {
        return new Gson().toJson(this);
    }
}
