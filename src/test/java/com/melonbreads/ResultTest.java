package com.melonbreads;

import com.melonbreads.function.ThrowingFunction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

public final class ResultTest {

    @Test
    public void ofSuccessfulSupplierTest() {
        Result<String, ?> result = Result.ofSupplier(() -> "bored");
        Assertions.assertThrows(NullPointerException.class, result::getError);
        Assertions.assertEquals("bored", result.getValue());
    }

    @Test
    public void ofFailedSupplierTest() {
        Result<String, Exception> result = Result.ofSupplier(() -> {
            throw new Exception("failed");
        });
        Assertions.assertThrows(NullPointerException.class, result::getValue);
        Assertions.assertEquals("failed", result.getError().getMessage());
    }

    @Test
    public void ofSuccessfulFunctionTest() {
        Function<String, Result<Integer, NumberFormatException>> function = Result.forFunction(Integer::parseInt);
        Result<Integer, NumberFormatException> result = function.apply("123");
        Assertions.assertEquals(123, result.getValue());
        Assertions.assertThrows(NullPointerException.class, result::getError);
    }

    @Test
    public void ofFailedFunctionTest() {
        Function<String, Result<Integer, NumberFormatException>> function = Result.forFunction(Integer::parseInt);
        Result<Integer, NumberFormatException> result = function.apply("123a");
        Assertions.assertInstanceOf(NumberFormatException.class, result.getError());
        Assertions.assertThrows(NullPointerException.class, result::getValue);
    }

    @Test
    public void ofFailingFunctionToOptionalTest() {
        ThrowingFunction<String, String, Exception> thrower = (s) -> {
            throw new Exception();
        };
        Optional<String> result = Optional.of("abc 123 ")
                .map(String::trim)
                .map(Result.forFunction(thrower))
                .flatMap(Result::toOptional);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void optionalGetToResult() {
        Result<Object, NoSuchElementException> result = Result.ofSupplier(Optional.empty()::get);
        Assertions.assertInstanceOf(NoSuchElementException.class, result.getError());
        Assertions.assertThrows(NullPointerException.class, result::getValue);
    }
}
