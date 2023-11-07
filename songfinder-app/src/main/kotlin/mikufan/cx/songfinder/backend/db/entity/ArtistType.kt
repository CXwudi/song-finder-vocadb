package mikufan.cx.songfinder.backend.db.entity

enum class ArtistType {
  Unknown, Circle, Label, Producer, Animator, Illustrator, Lyricist, Vocaloid, UTAU, CeVIO,
  OtherVoiceSynthesizer, OtherVocalist, OtherGroup, OtherIndividual, Utaite,
  Band, Vocalist, Character, SynthesizerV, CoverArtist, NEUTRINO,
  VoiSona, NewType, Voiceroid;

  fun isVirtualSinger() = this in setOf(
    Vocaloid, UTAU,  CeVIO, OtherVoiceSynthesizer, SynthesizerV, NEUTRINO,
    VoiSona, NewType, Voiceroid
  )

  fun isSongProducer() = this in setOf(
    Circle, Producer
  )
}