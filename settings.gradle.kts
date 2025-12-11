rootProject.name = "pokebowl"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://packages.confluent.io/maven/")
        maven(url = "https://jitpack.io")
    }
}
