# Acrolinx Java SDK

[![Maven Central](https://img.shields.io/maven-central/v/com.acrolinx.client/sdk)](https://search.maven.org/artifact/com.acrolinx.client/sdk)
![Build Status](https://github.com/acrolinx/sdk-java/actions/workflows/build-deploy.yml/badge.svg)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=acrolinx_sdk-java&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=acrolinx_sdk-java)


This library is meant to be used to interact with the Acrolinx Platform API in automated integrations.
It does NOT offer an interface to work with the Acrolinx Sidebar (see [Sidebar Java SDK](https://github.com/acrolinx/sidebar-sdk-java)).

## Get Started with Your Integration

### Prerequisites

Please contact [Acrolinx SDK support](https://github.com/acrolinx/acrolinx-coding-guidance/blob/main/topics/sdk-support.md)
for consulting and getting your integration certified.
The tests in this SDK work with a test license on an internal Acrolinx URL.
This license is only meant for demonstration and developing purposes.
Once you finished your integration, you'll have to get a license for your integration from Acrolinx.

Acrolinx offers different other SDKs, and examples for developing integrations.

Before you start developing your own integration, you might benefit from looking into:

* [Build With Acrolinx](https://support.acrolinx.com/hc/en-us/categories/10209837818770-Build-With-Acrolinx),
* the [Guidance for the Development of Acrolinx Integrations](https://github.com/acrolinx/acrolinx-coding-guidance),
* the [Acrolinx Platform API](https://github.com/acrolinx/platform-api)
* the [Rendered Version of the Acrolinx Platform API](https://acrolinxapi.docs.apiary.io/#)
* the [Acrolinx SDKs](https://github.com/acrolinx?q=sdk), and
* the [Acrolinx Demo Projects](https://github.com/acrolinx?q=demo).

### Start Developing

#### Installation

##### Maven

```xml
<dependency>
    <groupId>com.acrolinx.client</groupId>
    <artifactId>sdk</artifactId>
    <version>1.0.21</version>
</dependency>
```

##### Gradle

```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation 'com.acrolinx.client:sdk:1.0.21'
}
```

### Usage

**See the [Acrolinx Java SDK demo](https://github.com/acrolinx/sdk-demo-java/blob/main/src/main/java/com/acrolinx/client/demo/SdkDemo.java)
for a quickstart example.**

Create instance of `AcrolinxEndpoint` to begin.

`AcrolinxEndpoint` offers a single entry point to the avail features provided by the SDK.

See [`CheckTest.java`](src/test/java/com/acrolinx/client/sdk/integration/CheckTest.java) for more examples.

See the [Java SDK Documentation](https://acrolinx.github.io/sdk-java/).

### Integration Tests

In order to run the integration tests locally:

* You need access to the Acrolinx Platform
* You need to create an `.env`-file similar to the [`.env.template`](.env.template) file.
Alternately you can set the corresponding environment variables in a different way.

## License

Copyright 2019-present Acrolinx GmbH

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at:

[https://www.apache.org/licenses/LICENSE-2.0](https://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
