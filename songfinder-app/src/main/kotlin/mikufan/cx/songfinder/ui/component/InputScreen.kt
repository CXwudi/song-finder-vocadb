package mikufan.cx.songfinder.ui.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import mikufan.cx.songfinder.backend.model.IOFiles
import mikufan.cx.songfinder.ui.common.ColumnCentralizedWithSpacing
import mikufan.cx.songfinder.ui.common.MyDefaultAlertDialog
import mikufan.cx.songfinder.ui.common.RowCentralizedWithSpacing
import mikufan.cx.songfinder.ui.common.TooltipAreaWithCard
import mikufan.cx.songfinder.ui.theme.MyAppThemeWithSurface
import mikufan.cx.songfinder.ui.theme.spacing
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString

/**
 * @author CX无敌
 * 2023-06-05
 */

@Composable
fun InputScreen(
  onReady: (IOFiles) -> Unit,
  modifier: Modifier = Modifier,
) {
  val inputFileChosenModel = remember { FileChosenModel() }
  val outputFileChosenModel = remember { FileChosenModel() }
  var startingLine by remember { mutableStateOf(0UL) }

  ColumnCentralizedWithSpacing {
    LoadingScreenHeader()
    Divider()
    InputFilePicker(inputFileChosenModel)
    StartingLineInputField { startingLine = it }
    OutputFilePicker(outputFileChosenModel)
  }

  MyFilePicker(
    inputFileChosenModel.showFilePicker,
    listOf(".txt"),
    onShowStatusChanged = { inputFileChosenModel.showFilePicker = it },
  ) {
    inputFileChosenModel.file = it
  }

  MyFilePicker(
    outputFileChosenModel.showFilePicker,
    listOf(".csv"),
    onShowStatusChanged = { outputFileChosenModel.showFilePicker = it },
  ) {
    outputFileChosenModel.file = it
  }

  val isReady by remember { derivedStateOf { (inputFileChosenModel.file != null) && (outputFileChosenModel.file != null) } }
  if (isReady) {
    onReady(IOFiles(inputFileChosenModel.file!!, startingLine, outputFileChosenModel.file!!))
  }
}

@Preview
@Composable
fun previewOfInputWindow() {
  MyAppThemeWithSurface {
    InputScreen(onReady = {})
  }
}

@Composable
private fun LoadingScreenHeader() {
  RowCentralizedWithSpacing {
    Text(
      "Please choose your input and output files.",
      fontSize = MaterialTheme.typography.headlineMedium.fontSize,
    )
  }
  RowCentralizedWithSpacing {
    Text(
      "This window will close as soon as valid input and output files are chosen.",
    )
  }
}

@Composable
private fun InputFilePicker(inputFileChosenModel: FileChosenModel) = RowCentralizedWithSpacing {
  val inputFile = inputFileChosenModel.file
  TooltipAreaWithCard(tip = {
    Text(
      "The input TXT file. \n" +
          "It should only contain a list of song names, \n" +
          "one song name per line.\n" +
          "The file must be UTF-8.",
    )
  }) {
    Text("Input TXT File:")
  }
  if (inputFile != null) {
    TooltipAreaWithCard(tip = { Text("Full Path: ${inputFile.absolutePathString()}") }) {
      Text(inputFile.fileName.toString())
    }
  }
  Spacer(modifier = Modifier.weight(1f))
  Button(onClick = { inputFileChosenModel.showFilePicker = true }) {
    Text(inputFile?.let { "Re-choose Txt File" } ?: "Choose Input Txt File")
  }
}

@Composable
private fun StartingLineInputField(onStartingLineValueChange: (ULong) -> Unit) = RowCentralizedWithSpacing {
  TooltipAreaWithCard(tip = {
    Text(
      "When reading the input TXT file, \n" +
          "skip a certain number of lines before reading.\n" +
          "This is typically useful if to continue where you left from.\n" +
          "By default it is 0, which means no skipping.",
    )
  }) {
    Text("Read input file from")
  }
  Spacer(modifier = Modifier.weight(1f))

  var inputValue by remember { mutableStateOf(0L) }
  TextField(
    value = inputValue.toString(),
    leadingIcon = { Text("Line:") },
    onValueChange = {
      inputValue = if (it.isBlank()) 0L else it.toLongOrNull() ?: return@TextField
      val toSetValue = if (inputValue >= 0L) inputValue.toULong() else 0UL
      onStartingLineValueChange(toSetValue)
    },
    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
  )
  val showNegativeNumberDialog by remember { derivedStateOf { inputValue < 0L } }
  if (showNegativeNumberDialog) {
    MyDefaultAlertDialog(
      title = {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spacing),
        ) {
          Icon(Icons.Default.Warning, contentDescription = "Warning")
          Text(
            "Invalid Number",
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            color = MaterialTheme.colorScheme.error
          )
        }
      },
      text = { Text("The number of lines to skip cannot be negative.") },
    ) {
      inputValue = 0L
    }
  }

}

@Composable
private fun OutputFilePicker(
  outputFileChosenModel: FileChosenModel,
) = RowCentralizedWithSpacing {
  val outputFile = outputFileChosenModel.file
  TooltipAreaWithCard(tip = {
    Text(
      "The output file. \n" +
          "It will be a CSV file with a format \n" +
          "that is readable by VocaDB CSV Import Feature.",
    )
  }) {
    Text("Output CSV File:")
  }
  if (outputFile != null) {
    TooltipAreaWithCard(tip = { Text("Full Path: ${outputFile.absolutePathString()}") }) {
      Text(outputFile.fileName.toString())
    }
  }
  Spacer(modifier = Modifier.weight(1f))
  Button(onClick = { outputFileChosenModel.showFilePicker = true }) {
    Text(outputFile?.let { "Re-choose CSV File" } ?: "Choose Output CSV File")
  }
}

@Composable
fun MyFilePicker(
  showFilePicker: Boolean,
  fileExtensions: List<String> = listOf("."),
  onShowStatusChanged: (Boolean) -> Unit = {},
  onFilePicked: (Path) -> Unit,
) {
  FilePicker(
    showFilePicker,
    // something wrong here causing the file picker to not show up
//    initialDirectory = Path(".").toAbsolutePath().toString(),
//    fileExtensions = fileExtensions,
  ) { file ->
    onShowStatusChanged(false)
    file?.path?.let { onFilePicked(Path(it)) }
  }
}

class FileChosenModel {
  var file: Path? by mutableStateOf(null)
  var showFilePicker: Boolean by mutableStateOf(false)
}
