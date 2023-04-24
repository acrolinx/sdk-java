/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.internal;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.acrolinx.client.sdk.check.CheckPollResponse;
import com.google.gson.Gson;

import io.gsonfire.GsonFireBuilder;

public class JsonUtils
{
    private JsonUtils()
    {
    }

    private static final GsonFireBuilder builder = new GsonFireBuilder().registerTypeSelector(CheckPollResponse.class,
            CheckPollResponse.TYPE_SELECTOR).registerTypeSelector(SignInResponse.class,
                    SignInResponse.TYPE_SELECTOR).registerTypeSelector(SignInPollResponse.class,
                            SignInPollResponse.TYPE_SELECTOR);

    private static final Gson gson = builder.createGson();

    public static Type getType(final Class<?> rawClass, final Class<?> parameter)
    {
        return new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments()
            {
                return new Type[]{parameter};
            }

            @Override
            public Type getRawType()
            {
                return rawClass;
            }

            @Override
            public Type getOwnerType()
            {
                return null;
            }
        };
    }

    public static <T> T parseJson(String json, final Class<T> rawClass)
    {
        return gson.fromJson(json, rawClass);
    }

    public static <T> T parseJson(String json, final Class<?> rawClass, final Class<?> parameter)
    {
        return gson.fromJson(json, getType(rawClass, parameter));
    }

    public static <T> String toJson(T object)
    {
        return gson.toJson(object);
    }

    public static <T> JsonDeserializer<T> getSerializer(final Class<T> rawClass)
    {
        return jsonString -> parseJson(jsonString, rawClass);
    }

    public static <T> JsonDeserializer<T> getSerializer(final Class<T> rawClass, final Class<?> parameter)
    {
        return jsonString -> parseJson(jsonString, rawClass, parameter);
    }
}
