package com.melonbreads;

import com.melonbreads.function.ThrowingSupplier;

import java.util.Objects;
import java.util.function.Consumer;

public final class Result<V, E> {
    private final V value;

    private final E error;

    private Result(V aValue, E anError) {
        value = aValue;
        error = anError;
    }

    public static <V, E extends Throwable> Result<V, E> ok(V value) {
        return new Result<>(value, null);
    }

    public static <V, E extends Throwable> Result<V, E> nok(E error) {
        return new Result<>(null, error);
    }

    public static <V, E extends Throwable> Result<V, E> ofSupplier(ThrowingSupplier<V, E> supplier) {
        try {
            return Result.ok(supplier.get());
        } catch (Throwable e) {
            @SuppressWarnings("unchecked") E error = (E) e;
            return Result.nok(error);
        }
    }

    public void ifSuccess(Consumer<V> consumer) {
        if (value != null) {
            consumer.accept(value);
        }
    }

    public void ifError(Consumer<E> consumer) {
        if (error != null) {
            consumer.accept(error);
        }
    }

    public V getValue() {
        Objects.requireNonNull(value);
        return value;
    }

    public E getError() {
        Objects.requireNonNull(error);
        return error;
    }
}
