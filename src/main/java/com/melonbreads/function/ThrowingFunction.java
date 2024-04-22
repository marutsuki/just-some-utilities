package com.melonbreads.function;

@FunctionalInterface
public interface ThrowingFunction<I, V, E extends Throwable> {
    V apply(I input) throws E;
}
