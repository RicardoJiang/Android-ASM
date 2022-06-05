
plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.6.21"
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(gradleApi())
    implementation("com.android.tools.build:gradle:7.2.0")
    compileOnly("commons-io:commons-io:2.6")
    compileOnly("commons-codec:commons-codec:1.15")
    compileOnly("org.ow2.asm:asm-commons:9.2")
    compileOnly("org.ow2.asm:asm-tree:9.2")
}