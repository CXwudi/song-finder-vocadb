package mikufan.cx.songfinder.backend.controller.mainpage

import mikufan.cx.inlinelogging.KInlineLogging
import mikufan.cx.songfinder.backend.model.PVInfo
import mikufan.cx.songfinder.backend.model.SongSearchResult
import mikufan.cx.songfinder.backend.model.ThumbnailInfo
import mikufan.cx.songfinder.backend.service.FindThumbnailService
import mikufan.cx.songfinder.backend.service.InputFileLineReader
import mikufan.cx.songfinder.backend.service.OutputCsvLineWriter
import mikufan.cx.songfinder.backend.statemodel.*
import org.springframework.stereotype.Controller

@Controller
class ResultCellController(
  private val csvLineWriter: OutputCsvLineWriter,
  private val progressStateModel: ProgressStateModel,
  private val inputFileLineReader: InputFileLineReader,
  private val inputStateModel: SearchInputStateModel,
  private val searchOptionsStateModel: SearchOptionsStateModel,
  private val searchResultStateModel: SearchResultStateModel,
  private val songSearchIntermediateController: SongSearchIntermediateController,
  private val findThumbnailService: FindThumbnailService,
) {

  suspend fun handleRecord(chosenSong: SongSearchResult) {
    // First, write the chosen song to csv
    csvLineWriter.writeSongId(chosenSong.id.toULong())
    progressStateModel.increment()

    // Then, read the next song title from the input file. If no more song, then we are done
    val nextSongTitle = inputFileLineReader.readNext()
    if (nextSongTitle == null) {
      log.info { "No more song to handle, the app should display a congratulation screen soon" }
    } else {
      inputStateModel.update(nextSongTitle)
      searchOptionsStateModel.searchRegexOptionState.value = SearchRegexOption.Contains
      searchResultStateModel.resultState.clear()
      songSearchIntermediateController.triggerSearch(0)
    }
  }

  suspend fun tryGetThumbnail(pv: PVInfo): Result<ThumbnailInfo> {
    return findThumbnailService.tryGetThumbnail(pv)
  }
}

private val log = KInlineLogging.logger()