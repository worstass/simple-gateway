import org.jetbrains.kotlin.cli.jvm.main
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage
import java.net.URI
import java.util.Properties

var properties = Properties()
if (rootProject.file("local.properties").exists()) {
    properties.load(rootProject.file("local.properties").inputStream())
}

plugins {
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.graalvm.buildtools.native") version "0.9.28"
    kotlin("jvm") version "1.9.21"
    kotlin("plugin.spring") version "1.9.21"
}

group = "com.example.gateway"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    maven { url = URI("https://plugins.gradle.org/m2/") }
    maven { url = URI("https://maven.aliyun.com/repository/public") }
    maven { url = URI("https://maven.aliyun.com/repository/google") }
    maven { url = URI("https://maven.aliyun.com/repository/central") }
    maven { url = URI("https://maven.aliyun.com/repository/jcenter") }
    mavenCentral()
    google()
}

extra["springCloudVersion"] = "2023.0.0"
extra["springCloudAlibabaVersion"] = "2022.0.0.0"
dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery")
    implementation("org.springframework.security:spring-security-core")
//    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.rest-assured:kotlin-extensions")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
        mavenBom("com.alibaba.cloud:spring-cloud-alibaba-dependencies:${property("springCloudAlibabaVersion")}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}


graalvmNative {
    agent {
        defaultMode ="standard"
    }
    binaries {
        all {
            buildArgs.add("-Ob")
            resources.autodetect()
//            javaLauncher = javaToolchains.launcherFor {
//                languageVersion = JavaLanguageVersion.of(21)
//                vendor =  JvmVendorSpec.GRAAL_VM
//            }
        }
    }
    metadataRepository {
        enabled = true
    }
}

tasks.withType<BootBuildImage> {
//    builder = "paketobuildpacks/builder:tiny"
    imageName = properties.getProperty("dockerImageName") ?: project.name
    publish = false
    environment = mapOf(
        "BP_NATIVE_IMAGE" to "true",
        "BP_NATIVE_IMAGE_BUILD_ARGUMENTS" to "-march=compatibility"
    )
}
