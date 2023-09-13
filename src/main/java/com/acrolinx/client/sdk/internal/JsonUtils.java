/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.client.sdk.internal;

import com.acrolinx.client.sdk.check.CheckPollResponse;
import com.google.gson.Gson;
import io.gsonfire.GsonFireBuilder;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class JsonUtils
{
    private JsonUtils()
    {
        throw new IllegalStateException();
    }

    private static final Gson gson = createGson();

    private static Gson createGson()
    {
        return new GsonFireBuilder().registerTypeSelector(CheckPollResponse.class,
                CheckPollResponse.TYPE_SELECTOR).registerTypeSelector(SignInResponse.class,
                        SignInResponse.TYPE_SELECTOR).registerTypeSelector(SignInPollResponse.class,
                                SignInPollResponse.TYPE_SELECTOR).createGson();
    }

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

    public static <T> T parseJson(String jsonString, final Class<T> rawClass)
    {
        return gson.fromJson(jsonString, rawClass);
    }

    public static <T> T parseJson(String jsonString, final Class<?> rawClass, final Class<?> parameter)
    {
        return gson.fromJson(jsonString, getType(rawClass, parameter));
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
