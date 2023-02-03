package eu.zerodaycode.core.result;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The success value of a {@link Result}
 */
public final class Ok<T, E extends Throwable> implements Result<T, E> {
    private final T value;

    public Ok(T value) { this.value = value; }

    @Override
    public T unwrap() { return value; }

    @Override
    public T unwrapOr(T defaultValue) {
        return value;
    }

    @Override
    public T unwrapOrElse(Supplier<T> supplier) {
        return value;
    }

    @Override
    public boolean isOk() {
        return true;
    }

    @Override
    public boolean isErr() {
        return false;
    }

    @Override
    public Optional<T> ok() {
        return Optional.ofNullable(value);
    }

    @Override
    public Optional<E> err() {
        return Optional.empty();
    }

    @Override
    public <U> Result<U, E> map(final Function<T, U> fn) {
        return new Ok<>(fn.apply(value));
    }

    @Override
    public <U> U mapOrElse(Function<T, U> fn, Function<E, U> fallback) {
        return fn.apply(value);
    }

    @Override
    public <U extends Throwable> Result<T, U> or(Result<T, U> other) {
        return new Ok<>(value);
    }

    @Override
    public <U extends Throwable> Result<T, U> orElse(Supplier<U> supplier) {
        return new Ok<>(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ok<?, ?> ok)) return false;
        return Objects.equals(value, ok.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
