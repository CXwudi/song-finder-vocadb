package mikufan.cx.songfinder.backend.controller.mainpage

import mikufan.cx.inlinelogging.KInlineLogging
import mikufan.cx.songfinder.backend.service.FindThumbnailService
import mikufan.cx.songfinder.backend.service.InputFileLineReader
import mikufan.cx.songfinder.backend.service.OutputCsvLineWriter
import mikufan.cx.songfinder.backend.statemodel.*
import org.springframework.stereotype.Controller

/**
 * This class is responsible for handling the
 * intermediate steps of selecting and processing results. It interacts with various models and services
 * to perform its tasks.
 *
 * @property csvLineWriter The writer used to write song IDs to a CSV file.
 * @property progressStateModel The model used to track the progress of the selection process.
 * @property inputFileLineReader The reader used to read lines from an input file.
 * @property inputStateModel The model used to store and update the current input from the input file.
 * @property searchOptionsStateModel The model used to manage search options.
 * @property searchResultStateModel The model used to manage search results.
 * @property songSearchIntermediateController The intermediate controller for song search functionality.
 * @property findThumbnailService The service used to find thumbnails and also cache them.
 */
@Controller
class ResultSelectedIntermediateController(
  private val csvLineWriter: OutputCsvLineWriter,
  private val progressStateModel: ProgressStateModel,
  private val inputFileLineReader: InputFileLineReader,
  private val inputStateModel: SearchInputStateModel,
  private val searchOptionsStateModel: SearchOptionsStateModel,
  private val searchResultStateModel: SearchResultStateModel,
  private val songSearchIntermediateController: SongSearchIntermediateController,
  private val findThumbnailService: FindThumbnailService,
) {

  suspend fun writeRecordAndMoveOn(id: ULong) {
    // First, write the chosen song to csv
    csvLineWriter.writeSongId(id)
    progressStateModel.increment()
    findThumbnailService.evictCache()
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
}

private val log = KInlineLogging.logger()