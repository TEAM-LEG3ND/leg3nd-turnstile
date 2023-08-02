val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val kmongo_version: String by project
val koin_version: String by project
val koin_ktor_version: String by project
val koin_ksp_version: String by project

plugins {
    kotlin("jvm") version "1.9.0"
    id("io.ktor.plugin") version "2.3.2"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0"
    id("org.jmailen.kotlinter") version "3.15.0"
    id("com.google.devtools.ksp") version "1.9.0-1.0.11"
}

// Use KSP Plugin
apply(plugin = "com.google.devtools.ksp")

group = "com.leg3nd"
version = "0.0.1"
application {
    mainClass.set("com.leg3nd.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-config-yaml:$ktor_version")

    implementation("io.github.smiley4:ktor-swagger-ui:2.2.3")

    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jvm:2.3.2")
    implementation("io.ktor:ktor-server-core-jvm:2.3.2")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:2.3.2")

    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-encoding:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")

    implementation("org.litote.kmongo:kmongo-coroutine-serialization:$kmongo_version")
    implementation("org.litote.kmongo:kmongo-id-serialization:$kmongo_version")

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")

    implementation("io.insert-koin:koin-core:$koin_version")
    implementation("io.insert-koin:koin-ktor:$koin_ktor_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_ktor_version")
    implementation("io.insert-koin:koin-annotations:$koin_ksp_version")
    ksp("io.insert-koin:koin-ksp-compiler:$koin_ksp_version")
}

// KSP - To use generated sources
sourceSets.main {
    java.srcDirs("build/generated/ksp/main/kotlin")
}

tasks.lintKotlinMain {
    dependsOn("kspKotlin") // without this, it will fail to build with Gradle 8
// I have to convert the type `FileCollection` to `FileTree` here.
    source = (source - fileTree("$buildDir/generated")).asFileTree
}

tasks.formatKotlinMain {
    dependsOn("kspKotlin") // without this, it will fail to build with Gradle 8
// I have to convert the type `FileCollection` to `FileTree` here.
    source = (source - fileTree("$buildDir/generated")).asFileTree
}

tasks.whenTaskAdded {
//    println("adding task: $name")
    if (name == "lintKotlinGeneratedByKspKotlin" ||
        name == "lintKotlinGeneratedByKspTestKotlin" ||
        name == "formatKotlinGeneratedByKspKotlin" ||
        name == "formatKotlinGeneratedByKspTestKotlin"
    ) {
        enabled = false
    }
}

jib {
    from {
        platforms {
            platform {
                architecture = "arm64"
                os = "linux"
            }
        }
    }
}

ktor {
    docker {
        localImageName.set("leg3nd-turnstile")
        imageTag.set("latest")
        jreVersion.set(io.ktor.plugin.features.JreVersion.JRE_17)
        externalRegistry.set(
            io.ktor.plugin.features.DockerImageRegistry.externalRegistry(
                hostname = providers.environmentVariable("REGISTRY_HOST"),
                username = providers.environmentVariable("REGISTRY_ID"),
                password = providers.environmentVariable("REGISTRY_PASSWORD"),
                project = provider { "leg3nd-turnstile" },
            ),
        )
    }
}
