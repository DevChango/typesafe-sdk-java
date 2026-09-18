description = "Community Java client for the TypeSafe System One API"

dependencies {
    api("com.fasterxml.jackson.core:jackson-databind:2.15.4")
    api("org.jspecify:jspecify:1.0.1")

    testImplementation(platform("org.junit:junit-bom:5.14.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

mavenPublishing {
    coordinates(group.toString(), "typesafe-sdk", version.toString())

    pom {
        name.set("TypeSafe SDK for Java (community)")
        description.set(project.description)
    }
}
