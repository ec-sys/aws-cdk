package com.myorg.aws_cdk.utils;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;

import static com.myorg.aws_cdk.utils.Validations.requireNonEmpty;

public class EvnUtil {
    public static Environment makeEnv(App app) {
        String accountId = (String) app.getNode().tryGetContext("accountId");
        if (StringUtil.isEmpty(accountId)) {
            accountId = System.getenv("CDK_DEFAULT_REGION");
        }
        requireNonEmpty(accountId, "context variable 'accountId' must not be null");

        String region = (String) app.getNode().tryGetContext("region");
        if (StringUtil.isEmpty(accountId)) {
            accountId = System.getenv("CDK_DEFAULT_ACCOUNT");
        }
        requireNonEmpty(region, "context variable 'region' must not be null");

        return Environment.builder()
                .account(accountId)
                .region(region)
                .build();
    }
}
