package mikufan.cx.songfinder.backend.db.entity

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder

class ArtistRoleTest : ShouldSpec({

  context("ArtistRole") {
    should("return illustrator on int 16") {
      ArtistRole.rolesFromBitString(16) shouldContain ArtistRole.Illustrator
    }
    should("return Default on int 0") {
      ArtistRole.rolesFromBitString(0) shouldContain ArtistRole.Default
    }
    should("return Others and Animator on int 2049") {
      ArtistRole.rolesFromBitString(2049) shouldContainExactlyInAnyOrder listOf(ArtistRole.Other, ArtistRole.Animator)
    }
  }
})
