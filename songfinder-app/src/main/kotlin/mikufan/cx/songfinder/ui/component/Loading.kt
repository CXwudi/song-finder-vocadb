package mikufan.cx.songfinder.ui.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import mikufan.cx.songfinder.model.IOFiles
import mikufan.cx.songfinder.ui.common.TooltipAreaWithCard
import mikufan.cx.songfinder.ui.theme.MyDefaultModifier
import mikufan.cx.songfinder.ui.theme.MyDefaultPadding
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString

/**
 * @author CX无敌
 * 2023-06-05
 */

@Composable
fun LoadingScreen(
  onReady: (IOFiles) -> Unit,
  modifier: Modifier = MyDefaultModifier,
) {
  val inputFileChosenModel = remember { FileChosenModel() }
  val inputFile = inputFileChosenModel.file

  Column(modifier = modifier) {
    LoadingScreenRow {
      Text("Input TXT File:")
      if (inputFile != null) {
        TooltipAreaWithCard(tip = { Text("Full Path: ${inputFile.absolutePathString()}") }) {
          Text(inputFile.fileName.toString())
        }
      }
      Button(onClick = { inputFileChosenModel.showFilePicker = true }) {
        Text(inputFile?.let { "Re-choose Txt File" } ?: "Choose Input Txt File")
      }
    }
    LoadingScreenRow {
      Text("Starting from line:")
    }
  }

  MyFilePicker(inputFileChosenModel.showFilePicker) {
    inputFileChosenModel.file = it
    inputFileChosenModel.showFilePicker = false
  }
}

@Composable
fun LoadingScreenRow(content: @Composable () -> Unit) {
  Row(
    horizontalArrangement = Arrangement.spacedBy(MyDefaultPadding),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    content()
  }
}

@Composable
fun MyFilePicker(
  showFilePicker: Boolean,
  fileExtensions: List<String> = listOf("."),
  onFilePicked: (Path) -> Unit,
) {
  FilePicker(
    showFilePicker,
    initialDirectory = Path(".").toAbsolutePath().toString(),
    fileExtensions = fileExtensions,
  ) { file ->
    file?.path?.let { onFilePicked(Path(it)) }
  }
}

class FileChosenModel {
  var file: Path? by mutableStateOf(null)
  var showFilePicker: Boolean by mutableStateOf(false)
}

@Preview
@Composable
fun previewOfLoadingScreen() {
  LoadingScreen(onReady = {})
}
