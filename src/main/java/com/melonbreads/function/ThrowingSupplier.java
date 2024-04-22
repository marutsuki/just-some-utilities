package com.melonbreads.function;

@FunctionalInterface
public interface ThrowingSupplier<V, E extends Throwable> {
    V get() throws E;
}
