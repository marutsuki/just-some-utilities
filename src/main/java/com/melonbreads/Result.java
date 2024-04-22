package com.melonbreads;

import com.melonbreads.function.ThrowingFunction;
import com.melonbreads.function.ThrowingSupplier;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public final class Result<V, E extends Throwable> {

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

    public static <I, V, E extends Throwable> Function<I, Result<V, E>> forFunction(ThrowingFunction<I, V, E> function) {
        return (i -> {
            try {
                return Result.ok(function.apply(i));
            } catch (Throwable e) {
                @SuppressWarnings("unchecked") E error = (E) e;
                return Result.nok(error);
            }
        });
    }

    public <O> Result<O, E> map(Function<V, O> function) {
        if (value != null) {
            return Result.ok(function.apply(value));
        }
        return Result.nok(error);
    }

    public <O extends Throwable> Result<V, O> orElseThrow(Function<E, O> function) {
        if (error != null) {
            return Result.nok(function.apply(error));
        }
        return Result.ok(value);
    }

    public Optional<V> toOptional() {
        if (value != null) {
            return Optional.of(value);
        }
        return Optional.empty();
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

    public void ifSuccessOrElse(Consumer<V> onSuccess, Consumer<E> onError) {
        if (value != null) {
            onSuccess.accept(value);
        } else {
            onError.accept(error);
        }
    }

    public V otherwise(V alternative) {
        if (value == null) {
            return alternative;
        }
        return value;
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
