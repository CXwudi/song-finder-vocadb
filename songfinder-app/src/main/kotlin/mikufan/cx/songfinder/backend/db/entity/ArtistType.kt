package mikufan.cx.songfinder.backend.db.entity

enum class ArtistType {
  Unknown, Circle, Label, Producer, Animator, Illustrator, Lyricist, Vocaloid, UTAU, CeVIO,
  OtherVoiceSynthesizer, OtherVocalist, OtherGroup, OtherIndividual, Utaite,
  Band, Vocalist, Character, SynthesizerV, CoverArtist, NEUTRINO,
  VoiSona, NewType, Voiceroid, Instrumentalist, Designer,
  VOICEVOX, ACEVirtualSinger, AIVOICE;

  fun isVirtualSinger() = this in setOf(
    Vocaloid, UTAU, Utaite, CeVIO, OtherVoiceSynthesizer, SynthesizerV, NEUTRINO,
    VoiSona, NewType, Voiceroid, VOICEVOX, ACEVirtualSinger, AIVOICE
  )

  fun isVocalist() = this in setOf(
    Vocalist, OtherVocalist
  )

  fun isSongProducer() = this in setOf(
    Circle, Producer, CoverArtist, Instrumentalist
  )
}