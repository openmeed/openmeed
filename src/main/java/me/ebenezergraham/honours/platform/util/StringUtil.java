package me.ebenezergraham.honours.platform.util;

import java.util.Arrays;

public class StringUtil {

  public static boolean containReservedWord(String inputStr, String[] items) {
    return Arrays.stream(items).parallel().anyMatch(inputStr::contains);
  }
}
