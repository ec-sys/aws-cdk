package com.myorg;


import com.myorg.constructs.DockerRepository;
import com.myorg.utils.EvnUtil;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

import static com.myorg.utils.Validations.requireNonEmpty;

public class DockerRepositoryApp {

    public static void main(final String[] args) {
        App app = new App();

        String applicationName = (String) app.getNode().tryGetContext("applicationName");
        requireNonEmpty(applicationName, "context variable 'applicationName' must not be null");

        Environment awsEnvironment = EvnUtil.makeEnv(app);
        String accountId = awsEnvironment.getAccount();

        Stack dockerRepositoryStack = new Stack(app, "DockerRepositoryStack", StackProps.builder()
                .stackName(applicationName + "-create-ecr")
                .env(awsEnvironment)
                .description("This stack for creating (ASW ECR)repository of to-do app")
                .build());

        DockerRepository dockerRepository = new DockerRepository(
                dockerRepositoryStack,
                "DockerRepository",
                awsEnvironment,
                new DockerRepository.DockerRepositoryInputParameters(applicationName, accountId, 10, false));

        app.synth();
    }

    static Environment makeEnv(String account, String region) {
        return Environment.builder()
                .account(account)
                .region(region)
                .build();
    }

}