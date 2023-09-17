package com.myorg;

import com.myorg.constructs.Network;
import com.myorg.utils.EvnUtil;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

import static com.myorg.utils.Validations.requireNonEmpty;

public class NetworkCdkApp {
    public static void main(final String[] args) {
        App app = new App();

        String environmentName = (String) app.getNode().tryGetContext("environmentName");
        requireNonEmpty(environmentName, "context variable 'environmentName' must not be null");

        String sslCertificateArn = (String) app.getNode().tryGetContext("sslCertificateArn");
        requireNonEmpty(sslCertificateArn, "context variable 'sslCertificateArn' must not be null");

        Environment awsEnvironment = EvnUtil.makeEnv(app);

        Stack networkStack = new Stack(app, "NetworkStack", StackProps.builder()
                .stackName(environmentName + "-Network")
                .env(awsEnvironment)
                .build());

        Network network = new Network(
                networkStack,
                "Network",
                awsEnvironment,
                environmentName,
                new Network.NetworkInputParameters(sslCertificateArn));

        app.synth();
    }
}
