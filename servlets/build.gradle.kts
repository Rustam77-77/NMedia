plugins {
    kotlin("jvm")
    war
}
group = "ru.netology"
version = "1.0-SNAPSHOT"
dependencies {
    implementation(kotlin("stdlib"))
    compileOnly("javax.servlet:javax.servlet-api:4.0.1")
    implementation("com.google.code.gson:gson:2.10.1")

    testImplementation(kotlin("test"))
}
tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
}
tasks.war {
    archiveBaseName.set("servlets")
    archiveVersion.set("1.0")
}