import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

plugins {
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.graalvm.buildtools.native") version "0.9.28"
    kotlin("jvm") version "1.9.21"
    kotlin("plugin.spring") version "1.9.21"
}

group = "com.zhmap.gateway"
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
    testImplementation("org.springframework.boot:spring-boot-starter-test")
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
