# Acrolinx Java SDK Alpha Version [![Build Status](https://travis-ci.org/acrolinx/sdk-java.svg?branch=master)](https://travis-ci.org/acrolinx/sdk-java)

This library is in heavy development and shouldn’t yet be used for production.

This library is meant to be used to interact with the Acrolinx Platform API in Java integrations. 
It does NOT offer an interface to work with the Acrolinx Sidebar (see [Sidebar Java SDK](https://github.com/acrolinx/sidebar-sdk-java)).

## Get Started with your Integration

### Prerequisites

Please contact [Acrolinx SDK support](https://github.com/acrolinx/acrolinx-coding-guidance/blob/master/topics/sdk-support.md)
for consulting and getting your integration certified.
The tests in this SDK work with a test license on an internal Acrolinx URL.
This license is only meant for demonstration and developing purposes.
Once you finished your integration, you'll have to get a license for your integration from Acrolinx.
  
Acrolinx offers different other SDKs, and examples for developing integrations.

Before you start developing your own integration, you might benefit from looking into:

* [Getting Started with Custom Integrations](https://docs.acrolinx.com/customintegrations),
* the [Guidance for the Development of Acrolinx Integrations](https://github.com/acrolinx/acrolinx-coding-guidance),
* the [Acrolinx Platform API](https://github.com/acrolinx/platform-api)
* the [Rendered Version of Acrolinx Platform API](https://acrolinxapi.docs.apiary.io/#)
* the [Acrolinx SDKs](https://github.com/acrolinx?q=sdk), and
* the [Acrolinx Demo Projects](https://github.com/acrolinx?q=demo).

### Start Developing

Have a look at [AcrolinxEndpoint](./src/main/java/com/acrolinx/client/sdk/AcrolinxEndpoint.java) and use it.


## Contributing to this SDK 

### Integration Tests

In order to run the integration tests:
* You need access to the Acrolinx Platform
* You need to create an .env file similar to the [.env.template](.env.template) file. Alternately you can set the corresponding environment variables in a different way.


### Adding new Features and Releasing

0. Checkout the project, create and configure a .env file as described above.

1. Please add new features using the master branch, or submit a pull request.
   Run 
   ```bash
    ./gradlew build
   ```
    to install dependencies, build and test the project. 
    Also run the same command before pushing your changes to GitHub.
    
    If your build on Travis was successful, a new snapshot version will be automatically available via [Maven snapshot repository](https://oss.sonatype.org/content/repositories/snapshots/com/acrolinx/client/sdk/).

2. Once you tested your new features, remove the '-SNAPSHOT' from the `CURRENT_VERSION` property in the `gradle.properties` file.

3. Commit and push your changes. If all goes right, the artifact is released to [Maven Central](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.acrolinx.client%22%20a%3A%22sdk%22%20).
Note that it might take quite a while until the new version shows up in [Maven Central](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.acrolinx.client%22%20a%3A%22sdk%22%20), but it will be immediately available in the [oss staging repository](https://oss.sonatype.org/content/groups/staging/com/acrolinx/client/sdk/).

If that build fails, you might have to login into [Nexus Repository Manager](https://oss.sonatype.org/#welcome) and drop falsely created repositories, before triggering a new release build.

TravisCI will also automatically create a release tag on GitHub. If that fails run the Gradle Task for creating a release tag and pushing it GitHub:

   ```bash
   ./gradlew createGithubReleaseTag
   ```

4. Once the tag is pushed to GitHub, TravisCI will automatically update the [API documentation on the GitHub Pages](https://acrolinx.github.io/sidebar-sdk-java/).

5. Don't forget to commit and push a new SNAPSHOT version.

## License

Copyright 2019-present Acrolinx GmbH

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at:

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
