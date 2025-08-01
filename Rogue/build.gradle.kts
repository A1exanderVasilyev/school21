import java.util.Arrays

plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("application")
}

group = "s21.main"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.googlecode.lanterna:lanterna:3.2.0-alpha1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.17.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.0")
    implementation("com.fasterxml.jackson.module:jackson-module-parameter-names:2.17.0")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.shadowJar {
    archiveBaseName.set("Rogue")
    archiveClassifier.set("")
    archiveVersion.set("")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass.set("org.example.Main")
    applicationDefaultJvmArgs = listOf("-Djava.awt.headless=true")
}

//gnome-terminal -- java -jar build/libs/Rogue.jar