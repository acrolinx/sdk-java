# Contributing to This SDK

## Adding New Features and Releasing

1. Check out the project, create, and configure an `.env` file as described above.

2. Please add new features using the `master` branch, or submit a pull request.
   To install dependencies, build, and test the project, run:

   ```bash
    ./gradlew build
   ```

   Also run the same command before pushing your changes to GitHub.

   On Travis the build will be tested against the latest released Acrolinx Platform.
   If your build on Travis was successful, a new snapshot version will be automatically available via the [Maven snapshot repository](https://oss.sonatype.org/content/repositories/snapshots/com/acrolinx/client/sdk/).
3. Once you tested your new features, remove the `-SNAPSHOT` from the `CURRENT_VERSION` property in the [`gradle.properties`](gradle.properties) file.
4. Commit and push your changes. If all goes right, the artifact is released to [Maven Central](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.acrolinx.client%22%20a%3A%22sdk%22%20).
Note that it might take quite a while until the new version shows up in [Maven Central](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.acrolinx.client%22%20a%3A%22sdk%22%20).

   If that build fails, you might have to sign in into [Nexus Repository Manager](https://oss.sonatype.org/#welcome) and drop falsely created repositories, before starting a new release build.

   TravisCI will also automatically create a release tag on GitHub. If that fails run the Gradle Task for creating a release tag and pushing it GitHub:

   ```bash
      ./gradlew createGithubReleaseTag -Dorg.ajoberstar.grgit.auth.username=someone -Dorg.ajoberstar.grgit.auth.password=mysecretpassword
   ```

5. Once the tag is pushed to GitHub, TravisCI will automatically update the [API documentation on the GitHub Pages](https://acrolinx.github.io/sidebar-sdk-java/).
6. Don't forget to commit and push a new SNAPSHOT version.
7. Update referenced versions is [`README.md`](https://github.com/acrolinx/sdk-java/edit/master/README.md).
8. Update referenced versions is [Acrolinx Java SDK Demo Project](https://github.com/acrolinx/sdk-demo-java/edit/master/build.gradle).

## Updating Dependencies

```./gradlew useLatestVersions && ./gradlew useLatestVersionsCheck```
