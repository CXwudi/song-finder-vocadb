plugins {
  id("my.compose-desktop-app")
  alias(libs.plugins.kotlinSpring)
}

fun DependencyHandlerScope.sbs(dep: String? = null, version: String? = null) =
  "org.springframework.boot:spring-boot-starter${dep?.let { "-$it" } ?: ""}${version?.let { ":$it" } ?: ""}"


dependencies {
  // compose
  implementation(libs.dep.composeFilePicker)

  // kotest
  testImplementation(platform(libs.bom.kotest))
  testImplementation("io.kotest:kotest-runner-junit5")

  // spring boot
  versionConstraints(platform(libs.bom.springBoot))
  implementation(sbs())
  modules {
    module(sbs("logging")) {
      replacedBy(sbs("log4j2"))
    }
  }
  implementation(libs.dep.kotlinJvmInlineLogging)
  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
  implementation(sbs("jdbc"))
  runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
  testImplementation(sbs("test")) {
    exclude(group = "org.mockito")
  }
  testImplementation(libs.dep.kotestExtensionsSpring)
  testImplementation(libs.dep.mockk)
  testImplementation(libs.dep.springMockk)
}

compose.desktop {
  application {
    mainClass = "mikufan.cx.songfinder.EntrypointKt"
    nativeDistributions {
      includeAllModules = true
    }
  }
}

tasks.withType<Test>().configureEach {
  useJUnitPlatform()
}
