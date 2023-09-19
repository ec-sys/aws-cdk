package com.myorg.aws_cdk.utils;

import java.util.Objects;

public class StringUtil {

    private static final String BLANK_STRING = "";

    public static boolean isEmpty(String strVal) {
        if (Objects.isNull(strVal)) return true;
        return strVal.trim().isEmpty();
    }
}
