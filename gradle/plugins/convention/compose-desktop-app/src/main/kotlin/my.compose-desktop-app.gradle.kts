plugins {
  id("my.root.jvm")
  kotlin("jvm")
  id("org.jetbrains.compose")
  kotlin("plugin.compose")
}


dependencies {
  // compose desktop
  implementation(compose.desktop.currentOs)
  implementation(compose.material3)

  // kotlin
  implementation(kotlin("stdlib"))
  implementation(kotlin("reflect"))
  implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom"))
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm")

}

kotlin {
  compilerOptions {
    javaParameters = true // see the same reason in my.root.jvm root plugin
    freeCompilerArgs.set(
      freeCompilerArgs.get() + listOf(
        "-Xjsr305=strict",
        "-Xjvm-default=all"
      )
    ) // enable strict null check + jvm default
    // kotlin will use a java tool chain version, so no need to specify a java version here
    // see https://kotlinlang.org/docs/gradle-configure-project.html#gradle-java-toolchains-support
  }
}
