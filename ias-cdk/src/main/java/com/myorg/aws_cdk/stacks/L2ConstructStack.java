package com.myorg.aws_cdk.stacks;

import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.s3.BlockPublicAccess;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.BucketProps;
import software.constructs.Construct;

public class L2ConstructStack extends Stack {

    public L2ConstructStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        Bucket bucket = new Bucket(this, "L2Construct",
                BucketProps.builder()
                        .bucketName("tam-tit-90")
                        .versioned(true)
                        .blockPublicAccess(BlockPublicAccess.BLOCK_ALL)
                        .removalPolicy(RemovalPolicy.DESTROY)
                        .build()
        );
    }
}
