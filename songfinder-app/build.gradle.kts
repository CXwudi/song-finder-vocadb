plugins {
  id("my.compose-desktop-app")
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-jdbc")
  runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
}

compose.desktop {
  application {
    mainClass = "mikufan.cx.songfinder.EntrypointKt"
  }
}
