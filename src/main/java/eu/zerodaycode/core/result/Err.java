package eu.zerodaycode.core.result;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The type for contain an error of a {@link Result}
 */
public class Err<T, E extends Throwable> implements Result<T, E> {
    private final E err;

    public Err(E err) { this.err = err; }

    @Override
    public T unwrap() throws Throwable { throw err; }

    @Override
    public T unwrapOr(T defaultValue) {
        return defaultValue;
    }

    @Override
    public T unwrapOrElse(Supplier<T> supplier) {
        return supplier.get();
    }

    @Override
    public boolean isOk() {
        return false;
    }

    @Override
    public boolean isErr() {
        return true;
    }

    @Override
    public Optional<T> ok() {
        return Optional.empty();
    }

    @Override
    public Optional<E> err() {
        return Optional.ofNullable(err);
    }

    @Override
    public <U> Result<U, E> map(final Function<T, U> fn) {
        return new Err<>(err);
    }

    @Override
    public <U> U mapOrElse(Function<T, U> fn, Function<E, U> fallback) {
        return fallback.apply(err);
    }

    @Override
    public <U extends Throwable> Result<T, U> or(Result<T, U> other) {
        return other;
    }

    @Override
    public <U extends Throwable> Result<T, U> orElse(Supplier<U> supplier) {
        return new Err<>(supplier.get());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Err<?, ?> err1)) return false;
        return Objects.equals(err, err1.err);
    }

    @Override
    public int hashCode() {
        return Objects.hash(err);
    }
}
