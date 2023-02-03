package eu.zerodaycode.core.result;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * An interface for dealing with falible API's in a modern way, like, for example,
 * the Rust programming language.
 *
 * This type provides several API's that make the workflow of dealing with errors much nicer
 * and simpler, focusing on a functional approach
 *
 * @param <T> the type that holds the `Ok` value
 * @param <E> the type that holds the `Err` value
 */
public interface Result<T, E extends Throwable> {
    /**
     * Constructs a new {@link Ok} type, representing a success {@link Result}
     * @return an {@link Ok} wrapping the T value
     */
    static <T, E extends Throwable> Result<T, E> Ok(final T ok) {
        return new Ok<>(Objects.requireNonNull(ok));
    }

    /**
     * Constructs a new {@link Err} type, representing a failure {@link Result}
     * @return an {@link Err} wrapping the E value
     */
    static <T, E extends Throwable> Result<T, E> Err(final E err) {
        return new Err<>(Objects.requireNonNull(err));
    }

    /**
     * Constructs a new Result<T, E> based on the value
     * provided. If value is an instance of {@link Throwable},
     * an {@link Err} is created in another overload of this method.
     *
     * For pure values, this overload is selected, returning a {@link Result}
     * specifically, an {@link Ok}
     */
    static <V> Result<V, ?> from(V value) {
        return Result.Ok(value);
    }

    /**
     * Overload for wrapping error values in {@link Err}
     */
    static <E extends Throwable> Result<?, E> from(E throwable) {
        return Result.Err(throwable);
    }

    /**
     * Constructs a new {@link Result} from a {@link Supplier}.
     * The supplier will be safety executed inside this stack
     * call, constructing in a polymorphic way an @{link Ok} value
     * if the provided operation doesn't provoques an unchecked
     * exception, otherwise, the exception is caught and a new
     * {@link Err} is created and returned
     */
    @SuppressWarnings("unchecked")
    static <V, E extends Throwable> Result<V, E> fromOp(Supplier<V> operation) {
        try {
            return new Ok<>(operation.get());
        } catch (Exception ex) {
            return new Err<>((E) ex);
        }
    }


    /**
     * @return unchecked way of return the wrapped value in case that this
     * method call will be performed over an Ok implementor,
     * otherwise throws the contained error
     */
    T unwrap() throws Throwable;

    /**
     * @return The ok value if the method call is over an {@link Ok} implementor,
     * otherwise, returns the defaulted value
     */
    T unwrapOr(final T defaultValue);

    /**
     * @return Makes a computation to return a default value if this is called over an {@link Err}
     * implementor. Otherwise, returns the T value
     */
    T unwrapOrElse(final Supplier<T> supplier);

    /**
     * @return true if the value is an {@link Ok}, false otherwise
     */
    boolean isOk();

    /**
     * @return true if the value is an {@link Err}, false otherwise
     */
    boolean isErr();

    /**
     * @return Converts self into an Option<T>, consuming this,
     * and discarding the error, if any
     */
    Optional<T> ok();

    /**
     * @return Converts self into an Option<E>, consuming this,
     * and discarding the success value, if any
     */
    Optional<E> err();

    /**
     * Applies a function to the wrapped inner value, returning a
     * new {@link Result<U>} after being transformed from {@link Result<T>}
     */
    <U> Result<U, E> map(final Function<T, U> fn);

    /**
     * Applies a function to the {@link Ok} implementor, or performs a computation
     * over the {@link Err} type.
     *
     * @return The wrapped value transmuted and computed as U if this was called over
     * the {@link Ok} implementor, a default value after being processed by the
     * fallback parameter if this method is called over an {@link Ok} implementor
     */
    <U> U mapOrElse(final Function<T, U> fn, final Function<E, U> fallback);

    /**
     * @ return is `this` is an {@link Err}, returns the new provided {@link Result}
     * via parameter. Otherwise, returns an {@link Ok} being a {@link Result}
     * implementor with the same wrapped value
     */
    <U extends Throwable> Result<T, U> or(final Result<T, U> other);

    /**
     * Applies a transformation to the {@link Err} implementors, in case of this is a supertype
     * of {@link Err}, transmuting E -> U, otherwise, returns a {@link Result} as supertype for
     * of an {@link Ok},
     * leaving the method reference untouched
     */
    <U extends Throwable> Result<T, U> orElse(final Supplier<U> supplier);
}
