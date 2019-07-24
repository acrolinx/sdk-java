package com.acrolinx.client.sdk.internal;

import com.google.gson.Gson;
import io.gsonfire.GsonFireBuilder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class JsonUtils {
    private JsonUtils() {
    }

    private static GsonFireBuilder builder = new GsonFireBuilder()
            .registerTypeSelector(SignInResponse.class, SignInResponse.TYPE_SELECTOR)
            .registerTypeSelector(SignInPollResponse.class, SignInPollResponse.TYPE_SELECTOR);

    private static Gson gson = builder.createGson();

    public static Type getType(final Class<?> rawClass, final Class<?> parameter) {
        return new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{parameter};
            }

            @Override
            public Type getRawType() {
                return rawClass;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
    }

    public static <T> T parseJson(String json, final Class<T> rawClass) {
        return gson.fromJson(json, rawClass);
    }

    public static <T> T parseJson(String json, final Class<?> rawClass, final Class<?> parameter) {
        return gson.fromJson(json, getType(rawClass, parameter));
    }

    public static <T> JsonDeserializer<T> getSerializer(final Class<T> rawClass) {
        return new JsonDeserializer<T>() {
            @Override
            public T deserialize(String jsonString) {
                return parseJson(jsonString, rawClass);
            }
        };
    }

    public static <T> JsonDeserializer<T> getSerializer(final Class<T> rawClass, final Class<?> parameter) {
        return new JsonDeserializer<T>() {
            @Override
            public T deserialize(String jsonString) {
                return parseJson(jsonString, rawClass, parameter);
            }
        };
    }
}
