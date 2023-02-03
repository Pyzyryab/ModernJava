package result;

import eu.zerodaycode.core.result.Err;
import eu.zerodaycode.core.result.Ok;
import eu.zerodaycode.core.result.Result;
import org.codehaus.commons.compiler.InternalCompilerException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.IllformedLocaleException;
import java.util.Optional;

/**
 * Unit tests for the Result<T, E> types
 */
public class ResultTest {
    // Shared states
    Result<Integer, Exception> okVariant = Result.Ok(1);
    Result<Integer, Exception> errVariant = Result.Err(new NullPointerException());

    @Test
    void testConstruct_AResultOkVariant_fromAnOperation() throws Throwable {
        Assertions.assertEquals(
            2, Result.fromOp(() -> 1 + 1).unwrap()
        );
    }

    @Test
    void testConstruct_AResultErrVariant_fromAnOperation() {
        Assertions.assertThrows(
            ArithmeticException.class,
            () -> Result.fromOp(() -> 1 / 0).unwrap()
        );
    }

    @Test
    void test_OkType_IsCorrectBuilt() {
        Assertions.assertEquals(Ok.class, okVariant.getClass());
    }

    @Test
    void test_ErrType_IsCorrectBuilt() {
        Assertions.assertEquals(Err.class, errVariant.getClass());
    }

    @Test
    void test_isOk_onOkType_isTrue() {
        Assertions.assertTrue(okVariant.isOk());
    }

    @Test
    void test_isErr_onOkType_isFalse() {
        Assertions.assertFalse(okVariant.isErr());
    }

    @Test
    void test_isOk_onErrType_isFalse() {
        Assertions.assertFalse(errVariant.isOk());
    }

    @Test
    void test_isErr_onErrType_isTrue() {
        Assertions.assertTrue(errVariant.isErr());
    }

    @Test
    void test_OkType_HasValue_AfterOkMethodCall() {
        Assertions.assertTrue(okVariant.ok().isPresent());
    }

    @Test
    void test_OkType_HasNotValue_AfterErrMethodCall() {
        Assertions.assertTrue(okVariant.ok().isPresent());
    }

    @Test
    void test_ErrType_IsCorrectConvertedToOptionalE() {
        Assertions.assertEquals(Optional.class, errVariant.ok().getClass());
        // TODO make the same test that the Ok conversiont to Optional<T>
    }

    @Test
    void test_ErrType_HasValue_AfterErrMethodCall() {
        Assertions.assertTrue(errVariant.err().isPresent());
    }

    @Test
    void test_ErrType_HasNotValue_AfterOkMethodCall() {
        Assertions.assertFalse(errVariant.ok().isPresent());
    }

    @Test
    void test_From_conversion() throws Throwable {
        var someThrowable = new NullPointerException();
        Assertions.assertEquals(1, Result.from(1).unwrap());
        Assertions.assertThrows(
            NullPointerException.class,
            () -> Result.from(someThrowable).unwrap()
        );
    }

    @Test
    void test_functional_map_transformation() throws Throwable {
        Assertions.assertEquals(
            okVariant.map(v -> v * 2).unwrap(), Result.Ok(2).unwrap()
        );
    }

    @Test
    void test_functional_map_transformation_orErrVariant_throws_exception() {
        Assertions.assertThrowsExactly(
            NullPointerException.class, () -> errVariant.map(v -> v * 2).unwrap()
        );
    }

    @Test
    void test_unwrap_onOkType() throws Throwable {
        Assertions.assertEquals(
            okVariant.unwrap(), Result.Ok(1).unwrap()
        );
    }

    @Test
    void test_unwrap_onErrType() {
        Assertions.assertThrowsExactly(
            NullPointerException.class, () -> errVariant.unwrap()
        );
    }

    @Test
    void test_unwrapOr_onOkType() {
        Assertions.assertEquals(
            1, okVariant.unwrapOr(7)
        );
    }

    @Test
    void test_unwrapOr_onErrType() {
        Assertions.assertEquals(
            7, errVariant.unwrapOr(7)
        );
    }

    @Test
    void test_unwrapOrElse_onOkType() {
        Assertions.assertEquals(
            1, okVariant.unwrapOrElse(null)
        );
    }

    @Test
    void test_unwrapOrElse_onErrType() {
        Assertions.assertEquals(
            777, errVariant.unwrapOrElse(() -> 777)
        );
    }

    @Test
    void test_mapOrElse_onOkType() {
        Assertions.assertEquals(
            2, (Integer) okVariant.mapOrElse(v -> v * 2, e -> null)
        );
    }

    @Test
    void test_mapOrElse_onErrType() {
        Assertions.assertEquals(
            0, (Integer) errVariant.mapOrElse(v -> v * 2, e -> 0)
        );
    }

    @Test
    void test_orMethodCall_onOkType() throws Throwable {
        Assertions.assertEquals(
            1, okVariant.or(new Ok<>(7)).unwrap()
        );
    }

    @Test
    void test_orMethodCall_onErrType() throws Throwable {
        Assertions.assertEquals(
            7, errVariant.or(new Ok<>(7)).unwrap()
        );
    }

    @Test
    void test_orElseMethodCall_onOkType() throws Throwable {
        Assertions.assertEquals(
            1, okVariant.orElse(IllformedLocaleException::new).unwrap()
        );
    }

    @Test
    void test_orElseMethodCall_onErrType() {
        Assertions.assertThrows(
            InternalCompilerException.class,
            () -> errVariant.orElse(
                InternalCompilerException::new
            ).unwrap()
        );
    }
}
