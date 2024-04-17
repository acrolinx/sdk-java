# Acrolinx Java SDK

[![Build](https://github.com/acrolinx/sdk-java/actions/workflows/build.yml/badge.svg)](https://github.com/acrolinx/sdk-java/actions/workflows/build.yml)

This is a Java library to interact with the Acrolinx Platform API.
It does not offer a graphical user interface (GUI) to work with the Acrolinx Sidebar (see [Sidebar Java SDK](https://github.com/acrolinx/sidebar-sdk-java)).

## Get Started with Your Integration

### Prerequisites

Please contact [Acrolinx SDK support](https://github.com/acrolinx/acrolinx-coding-guidance/blob/main/topics/sdk-support.md)
for consulting and getting your integration certified.

Some integration tests in this SDK work with a test license on an internal Acrolinx URL.
This license is only meant for demonstration and developing purposes.

Before you start developing your own integration, you might benefit from looking into:

* [Build With Acrolinx](https://support.acrolinx.com/hc/en-us/categories/10209837818770-Build-With-Acrolinx),
* the [Guidance for the Development of Acrolinx Integrations](https://github.com/acrolinx/acrolinx-coding-guidance),
* the [Acrolinx Platform API](https://github.com/acrolinx/platform-api)
* the [Rendered Version of the Acrolinx Platform API](https://acrolinxapi.docs.apiary.io/#)
* the [Acrolinx SDKs](https://github.com/acrolinx?q=sdk), and
* the [Acrolinx Demo Projects](https://github.com/acrolinx?q=demo).

## Getting Started

### Build the Project

1. You need Java 11 to build this project.
2. This project uses [Gradle](https://gradle.org/).
To build this project with the [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html#sec:using_wrapper), execute the following command:

```bash
./gradlew build
```

on a UNIX system, or

```batch
gradlew build
```

on a Windows computer.

### Using the SDK

Reference the Maven artifact `com.acrolinx.client:sdk` which is available on
[Maven Central](https://central.sonatype.com/artifact/com.acrolinx.client/sdk).
Have a look at the [`build.gradle`](build.gradle) file if you use Gradle.

## References

* The [DEMO Java](https://github.com/acrolinx/sdk-demo-java) is built based on this SDK.
* The API documentation is published on the [GitHub Pages](https://acrolinx.github.io/sdk-java/).
