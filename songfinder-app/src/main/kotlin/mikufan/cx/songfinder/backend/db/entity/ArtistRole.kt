package mikufan.cx.songfinder.backend.db.entity

enum class ArtistRole(val bit: Int) {
  Default(0),
  Animator(1 shl 0),
  Arranger(1 shl 1),
  Composer(1 shl 2),
  Distributor(1 shl 3),
  Illustrator(1 shl 4),
  Instrumentalist(1 shl 5),
  Lyricist(1 shl 6),
  Mastering(1 shl 7),
  Publisher(1 shl 8),
  Vocalist(1 shl 9),
  VoiceManipulator(1 shl 10),
  Other(1 shl 11),
  Mixer(1 shl 12),
  Chorus(1 shl 13),
  Encoder(1 shl 14),
  VocalDataProvider(1 shl 15);

  companion object {
    fun rolesFromBitString(bitString: Int): List<ArtistRole> = if (bitString == 0) {
      listOf(Default)
    } else {
      entries.filter { role ->
        role != Default && role.bit and bitString != 0
      }
    }
  }
}