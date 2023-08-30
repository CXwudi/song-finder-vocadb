plugins {
  id("my.compose-desktop-app")
  alias(libs.plugins.kotlinSpring)
}

fun DependencyHandlerScope.sbs(dep: String? = null, version: String? = null) =
  "org.springframework.boot:spring-boot-starter${dep?.let { "-$it" } ?: ""}${version?.let { ":$it" } ?: ""}"


dependencies {
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
  implementation(sbs("jdbc"))
  runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
  testImplementation(sbs("test")) {
    exclude(group = "org.mockito")
  }
  testImplementation("io.kotest.extensions:kotest-extensions-spring")
  testImplementation("io.mockk:mockk")
  testImplementation("com.ninja-squad:springmockk")
}

compose.desktop {
  application {
    mainClass = "mikufan.cx.songfinder.EntrypointKt"
  }
}

tasks.withType<Test>().configureEach {
  useJUnitPlatform()
}
