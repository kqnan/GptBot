plugins {
    `java-library`
    `maven-publish`
    id("io.izzel.taboolib") version "1.53"
    id("org.jetbrains.kotlin.jvm") version "1.5.10"
}

taboolib {
    install("common")
    install("common-5")
    install("module-chat")
    install("module-configuration")
    install("module-ui")
    install("module-nms")
    install("module-nms-util")
    install("platform-bukkit")
    install("module-kether")
    install("expansion-command-helper")
    install("module-database")
    install("expansion-player-database")
    classifier = null
    version = "6.0.10-42"
}

repositories {
    maven("https://jitpack.io")
    maven("https://maven.aliyun.com/repository/public")
    maven( "https://maven.aliyun.com/repository/central" )
    maven ( "https://maven.aliyun.com/repository/google")
    maven ( "https://maven.aliyun.com/repository/public" )
    maven(  "https://maven.aliyun.com/repository/gradle-plugin" )
    mavenCentral()
}

dependencies {
    implementation("org.apache.httpcomponents:httpclient:4.5.13")
    compileOnly("ink.ptms:nms-all:1.0.0")
    compileOnly("ink.ptms.core:v11902:11902-minimize:mapped")
    compileOnly("ink.ptms.core:v11902:11902-minimize:universal")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

publishing {
    repositories {
        maven {
            url = uri("https://repo.tabooproject.org/repository/releases")
            credentials {
                username = project.findProperty("taboolibUsername").toString()
                password = project.findProperty("taboolibPassword").toString()
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
            groupId = project.group.toString()
        }
    }
}