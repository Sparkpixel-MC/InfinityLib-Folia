plugins {
    java
    id("com.gradleup.shadow") version "9.6.1"
}

group = "io.github.mooy1"
version = "2.0.0-Folia"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
    withSourcesJar()
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
}

dependencies {
    // Folia API (includes the region/entity/global/async schedulers)
    compileOnly("dev.folia:folia-api:1.21.8-R0.1-SNAPSHOT")

    compileOnly("com.github.SlimefunGuguProject:Slimefun4:2025.1.2")

    compileOnly("org.projectlombok:lombok:1.18.46")
    annotationProcessor("org.projectlombok:lombok:1.18.46")

    compileOnly("com.google.code.findbugs:jsr305:3.0.2")

    implementation("org.bstats:bstats-bukkit:2.2.1")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}

tasks.jar {
    archiveClassifier.set("unshaded")
}

tasks.shadowJar {
    archiveClassifier.set("")
    relocate("org.bstats", "io.github.mooy1.infinitylib.metrics")
    exclude("META-INF/*")
}

tasks.test {
    useJUnitPlatform()
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
