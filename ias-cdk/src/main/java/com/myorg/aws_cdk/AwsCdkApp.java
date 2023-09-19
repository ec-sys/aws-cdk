package com.myorg.aws_cdk;

import com.myorg.aws_cdk.stacks.L1ConstructStack;
import com.myorg.aws_cdk.stacks.L2ConstructStack;
import com.myorg.aws_cdk.utils.EvnUtil;
import software.amazon.awscdk.App;
import software.amazon.awscdk.StackProps;

public class AwsCdkApp {

    public static void main(final String[] args) {
        App app = new App();

        new L1ConstructStack(app, "L1ContructStack",
                StackProps.builder()
                        .stackName("L1Example")
                        .env(EvnUtil.makeEnv(app))
                        .build()
        );

        new L2ConstructStack(app, "L2ContructStack",
                StackProps.builder()
                        .stackName("L2Example")
                        .env(EvnUtil.makeEnv((app)))
                        .build()
        );

        app.synth();
    }
}