repositories {
    maven(url = "https://nexus.velocitypowered.com/repository/maven-public/")
}

dependencies {
    val velocityVersion = "3.1.0"

    compileOnly("com.velocitypowered:velocity-api:$velocityVersion")
    annotationProcessor("com.velocitypowered:velocity-api:$velocityVersion")

    testImplementation("com.velocitypowered:velocity-api:$velocityVersion")
    testAnnotationProcessor("com.velocitypowered:velocity-api:$velocityVersion")
}
