package com.myorg.aws_cdk.utils;

public class Validations {

    private Validations() {
    }

    public static void requireNonEmpty(String strVal, String message) {
        if (strVal == null || strVal.trim() == "") {
            throw new IllegalArgumentException(message);
        }
    }
}