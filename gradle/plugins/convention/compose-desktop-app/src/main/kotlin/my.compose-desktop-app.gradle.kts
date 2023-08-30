plugins {
  id("my.root.jvm")
  kotlin("jvm")
  id("org.jetbrains.compose")
}


dependencies {
  // compose desktop
  implementation(compose.desktop.currentOs)
  implementation(compose.material3)
  implementation("com.darkrockstudios:mpfilepicker")

  // kotlin
  implementation(kotlin("stdlib"))
  implementation(kotlin("reflect"))
  implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom"))
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm")

}

kotlin {
  target {
    compilations.all {
      kotlinOptions {
        javaParameters = true // see the same reason in jvm-root mixin
        freeCompilerArgs = freeCompilerArgs + listOf("-Xjsr305=strict") // enable strict null check
        // kotlin will use a java tool chain version, so no need to specify a java version here
        // see https://kotlinlang.org/docs/gradle-configure-project.html#gradle-java-toolchains-support
      }
    }
  }
}
