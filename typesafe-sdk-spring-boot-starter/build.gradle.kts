description = "Spring Boot auto-configuration for the community TypeSafe SDK for Java"

val springBootVersion = "3.1.2"

dependencies {
    api(project(":typesafe-sdk"))
    implementation("org.springframework.boot:spring-boot-autoconfigure:$springBootVersion")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:$springBootVersion")

    testImplementation(platform("org.junit:junit-bom:5.14.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.springframework.boot:spring-boot-test:$springBootVersion")
    testImplementation("org.springframework:spring-test:6.0.11")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

mavenPublishing {
    coordinates(group.toString(), "typesafe-sdk-spring-boot-starter", version.toString())

    pom {
        name.set("TypeSafe SDK for Java Spring Boot Starter (community)")
        description.set(project.description)
    }
}
