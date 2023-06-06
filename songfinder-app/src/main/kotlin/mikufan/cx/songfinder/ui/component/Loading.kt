package mikufan.cx.songfinder.ui.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import mikufan.cx.songfinder.model.IOFiles
import mikufan.cx.songfinder.ui.theme.DefaultModifier
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString

/**
 * @author CX无敌
 * 2023-06-05
 */

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LoadingScreen(
  onReady: (IOFiles) -> Unit,
  modifier: Modifier = DefaultModifier,
) {
  var inputFileChosenModel = remember { FileChosenModel() }

  Column(modifier = modifier) {
    Row(horizontalArrangement = Arrangement.spacedBy(5.dp), verticalAlignment = Alignment.CenterVertically) {
      Text("Input TXT File:")
      if (inputFileChosenModel.file != null) {
        TooltipArea(
          tooltip = {
            Text("File chosen: ${inputFileChosenModel.file!!.absolutePathString()}")
          },
          tooltipPlacement = TooltipPlacement.CursorPoint(
            alignment = Alignment.BottomEnd,
          ),
          delayMillis = 500,
        ) {
          Text(inputFileChosenModel.file!!.fileName.toString())
        }
      }
      Button(onClick = { inputFileChosenModel.showFilePicker = true }) {
        Text(inputFileChosenModel.file?.let { "Re-choose Txt File" } ?: "Choose Input Txt File")
      }
    }
  }

  MyFilePicker(inputFileChosenModel.showFilePicker) {
    inputFileChosenModel.file = it
    inputFileChosenModel.showFilePicker = false
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
