package mikufan.cx.songfinder.poc

import io.kotest.core.spec.style.ShouldSpec

class JvmPatternPoc : ShouldSpec({
  context("pattern") {
    should("get the string") {
      val pattern = ".*%s.*".format("test")
      val regex = pattern.toRegex()
      println(regex.pattern)
    }
  }
})