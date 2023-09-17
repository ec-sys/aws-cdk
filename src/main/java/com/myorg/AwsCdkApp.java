package com.myorg;

import com.myorg.stacks.L1ConstructStack;
import com.myorg.stacks.L2ConstructStack;
import com.myorg.utils.EvnUtil;
import com.myorg.utils.StringUtil;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

import static com.myorg.utils.Validations.requireNonEmpty;

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