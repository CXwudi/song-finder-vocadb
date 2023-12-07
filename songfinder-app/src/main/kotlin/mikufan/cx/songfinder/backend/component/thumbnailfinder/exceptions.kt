package mikufan.cx.songfinder.backend.component.thumbnailfinder

abstract class ThumbnailException(message: String) : RuntimeException(message)
class ThumbnailNotAvailableException(message: String) : ThumbnailException(message)