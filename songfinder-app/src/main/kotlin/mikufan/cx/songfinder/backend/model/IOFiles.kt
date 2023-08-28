package mikufan.cx.songfinder.backend.model

import java.nio.file.Path

/**
 * @author CX无敌
 * 2023-06-05
 */
data class IOFiles(
  val inputTxt: Path,
  val startLine: ULong,
  val outputCSV: Path,
)
