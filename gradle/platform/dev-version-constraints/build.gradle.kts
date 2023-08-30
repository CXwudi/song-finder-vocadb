// this platform is shared in the main build
plugins {
  id("java-platform")
}

group = "mikufan.cx"

javaPlatform {
  allowDependencies()
}

// add bom, gradle platforms, and dependencies to the shared libs.version.toml first
// then add them here using the version catalog
dependencies {
  // maven bom or gradle platform should have been added here with api(platform())
  // but since this platform is imported to every other project by being imported at root jvm plugin,
  // we don't want version conflict to happen from multiple api(platform()).

  // add your api() here to manage dependencies not defined in a bom or gradle platform
  constraints {
    // maven bom or gradle platform
    api(libs.bom.coroutines)
  }
}
