package com.github.lppedd.cc.angular2;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertSame;
import static junit.framework.TestCase.fail;

import org.junit.Test;

/**
 * @author Edoardo Luppi
 */
public class CCAngularUtilsTest {
  @Test
  public void shouldConvertCamelToDashCase() {
    assertEquals("example-one-module", CCAngularUtils.toDashCase("example-one-module"));
    assertEquals("example-one-module", CCAngularUtils.toDashCase("exampleOneModule"));
    assertEquals("example-one-module", CCAngularUtils.toDashCase("ExampleOneModule"));
    assertEquals("12-example-two-module", CCAngularUtils.toDashCase("12ExampleTwoModule"));
    assertEquals("12-example12-two-module", CCAngularUtils.toDashCase("12Example12TwoModule"));
    assertEquals("12-example12two-module", CCAngularUtils.toDashCase("12Example12TWOModule"));
  }

  @Test
  public void shouldThrowExceptionForEmptyString() {
    try {
      CCAngularUtils.toDashCase("");
      fail();
    } catch (final Exception e) {
      assertSame(e.getClass(), IllegalArgumentException.class);
      assertEquals("Input string cannot be empty", e.getMessage());
    }
  }
}
