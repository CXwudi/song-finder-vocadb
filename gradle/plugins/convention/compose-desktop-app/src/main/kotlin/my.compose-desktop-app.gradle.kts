plugins {
  id("my.root.jvm")
  kotlin("jvm")
  kotlin("plugin.spring")
  id("org.jetbrains.compose")
}

fun DependencyHandlerScope.sbs(dep: String? = null, version: String? = null) =
  "org.springframework.boot:spring-boot-starter${dep?.let { "-$it" } ?: ""}${version?.let { ":$it" } ?: ""}"

dependencies {
  // compose desktop
  implementation(compose.desktop.currentOs)

  // kotlin
  implementation(kotlin("stdlib"))
  implementation(kotlin("reflect"))
  implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom"))
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm")

  // kotest
  testImplementation(platform("io.kotest:kotest-bom"))
  testImplementation("io.kotest:kotest-runner-junit5")

  // spring boot
  versionConstraints(platform("org.springframework.boot:spring-boot-dependencies"))
  implementation(sbs())
  modules {
    module(sbs("logging")) {
      replacedBy(sbs("log4j2"))
    }
  }
  implementation("com.github.CXwudi:kotlin-jvm-inline-logging")
  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
  testImplementation(sbs("test")) {
    exclude(group = "org.mockito")
  }
  testImplementation("io.kotest.extensions:kotest-extensions-spring")
  testImplementation("io.mockk:mockk")
  testImplementation("com.ninja-squad:springmockk")
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

tasks.withType<Test>().configureEach {
  useJUnitPlatform()
}
