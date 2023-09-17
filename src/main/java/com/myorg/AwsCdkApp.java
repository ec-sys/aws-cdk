package com.myorg;

import com.myorg.stacks.L1ConstructStack;
import com.myorg.stacks.L2ConstructStack;
import com.myorg.utils.StringUtil;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

import static com.myorg.Validations.requireNonEmpty;

public class AwsCdkApp {

    public static void main(final String[] args) {
        App app = new App();

        new L1ConstructStack(app, "L1ContructStack",
                StackProps.builder()
                        .stackName("L1Example")
                        .env(makeEnv(app))
                        .build()
        );

        new L2ConstructStack(app, "L2ContructStack",
                StackProps.builder()
                        .stackName("L2Example")
                        .env(makeEnv(app))
                        .build()
        );

        app.synth();
    }

    static Environment makeEnv(App app) {
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