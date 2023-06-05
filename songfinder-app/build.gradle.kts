plugins {
  id("my.compose-desktop-app")
}

compose.desktop {
  application {
    mainClass = "mikufan.cx.songfinder.TestMainKt"
  }
}
