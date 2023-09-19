package com.myorg.aws_cdk.stacks;

import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.s3.CfnBucket;
import software.amazon.awscdk.services.s3.CfnBucketProps;
import software.constructs.Construct;

public class L1ConstructStack extends Stack {

    public L1ConstructStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        CfnBucket bucket = new CfnBucket(this, "L1Construct",
                CfnBucketProps.builder()
                        .bucketName("tam-tit-89")
                        .versioningConfiguration(CfnBucket.VersioningConfigurationProperty.builder()
                                .status("Enabled")
                                .build())
                        .publicAccessBlockConfiguration(CfnBucket.PublicAccessBlockConfigurationProperty.builder()
                                .restrictPublicBuckets(true)
                                .ignorePublicAcls(true)
                                .blockPublicPolicy(true)
                                .build())
                        .build()
        );
        bucket.applyRemovalPolicy(RemovalPolicy.DESTROY);
    }
}
