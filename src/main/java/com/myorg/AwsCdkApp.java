package com.myorg;

import com.myorg.stacks.StaticSiteStack;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

import java.util.HashMap;
import java.util.Map;

public class AwsCdkApp extends Stack {

    /**
     * This stack relies on getting the domain name from CDK context. Use 'cdk synth -c
     * domain=mystaticsite.com -c subdomain=www' Or add the following to cdk.json: { "context": {
     * "domain": "mystaticsite.com", "subdomain": "www" } }
     */
//    public AwsCdkApp(App scope, String id, StackProps props) {
//        super(scope, id, props);
//
//        // Getting domain and subdomain values from Context
//
//        Map<String, Object> domainValues = new HashMap<String, Object>();
//        domainValues.put("domainName", this.getNode().tryGetContext("domain"));
//        domainValues.put("siteSubDomain", this.getNode().tryGetContext("subdomain"));
////        System.out.println(this.getNode().tryGetContext("domain"));
////        System.out.println(this.getNode().tryGetContext("subdomain"));
//
//        // Call StaticSiteStack to create the stack
//        new StaticSiteStack(this, "MyStaticSite", props, domainValues);
//    }

    public static void main(final String argv[]) {
        /*
        App app = new App();

        // Stack must be in us-east-1, because the ACM certificate for a
        // global CloudFront distribution must be requested in us-east-1.
        StackProps pr = new StackProps.Builder().env(buildEnvironment()).build();

        new AwsCdkApp(app, "MyStaticSite", pr);
        app.synth();
         */

        App app = new App();

        Map<String, Object> domainValues = new HashMap<String, Object>();
        domainValues.put("domainName", "dangtit90.top");
        domainValues.put("siteSubDomain", "cdk");

        new StaticSiteStack(app, "MyStaticSite", StackProps.builder()
                .env(buildEnvironment())
                .build(), domainValues);

        app.synth();
    }

    private static Environment buildEnvironment() {
//        System.out.println(System.getenv("CDK_DEFAULT_REGION "));
//        System.out.println(System.getenv("CDK_DEFAULT_ACCOUNT"));

        return Environment.builder()
                .region("us-east-1")
                .account(System.getenv("CDK_DEFAULT_ACCOUNT"))
                .build();
    }
}