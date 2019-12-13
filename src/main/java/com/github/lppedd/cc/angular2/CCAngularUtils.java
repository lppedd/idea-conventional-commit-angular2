package com.github.lppedd.cc.angular2;

import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;

/**
 * @author Edoardo Luppi
 */
final class CCAngularUtils {
  private static final Pattern CAMEL_CASE_PATTERN = Pattern.compile("[^>](?=[A-Z][a-z])");

  @NotNull
  static String toDashCase(@NotNull final String str) {
    if (str.isEmpty()) {
      throw new IllegalArgumentException("Input string cannot be empty");
    }

    return CAMEL_CASE_PATTERN.matcher(str)
        .replaceAll("$0-")
        .toLowerCase();
  }
}
