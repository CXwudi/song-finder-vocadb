package mikufan.cx.songfinder.ui.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import mikufan.cx.songfinder.model.IOFiles
import mikufan.cx.songfinder.ui.common.MyDefaultAlertDialog
import mikufan.cx.songfinder.ui.common.TooltipAreaWithCard
import mikufan.cx.songfinder.ui.theme.MyAppTheme
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
  modifier: Modifier = Modifier.fillMaxSize(),
) {
  val inputFileChosenModel = remember { FileChosenModel() }
  val outputFileChosenModel = remember { FileChosenModel() }
  var startingLine by remember { mutableStateOf(0UL) }

  val inputFile = inputFileChosenModel.file
  val outputFile = outputFileChosenModel.file

  Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(MyDefaultPadding)) {
    Text(
      "Please choose your input and output files.",
      fontSize = MaterialTheme.typography.h4.fontSize,
      modifier = Modifier.align(Alignment.CenterHorizontally),
    )
    Text(
      "This window will close as soon as a valid input and output files are chosen.",
      modifier = Modifier.align(Alignment.CenterHorizontally),
    )
    Divider()
    LoadingScreenRow {
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
      Button(onClick = { inputFileChosenModel.showFilePicker = true }) {
        Text(inputFile?.let { "Re-choose Txt File" } ?: "Choose Input Txt File")
      }
    }
    LoadingScreenRow {
      TooltipAreaWithCard(tip = {
        Text(
          "When reading the input TXT file, \nskip a certain number of lines before reading.\n" +
              "This is typically useful if you want to continue where you left from.\n" +
              "By default it is 0, which means no skipping and read from the first line.",
        )
      }) {
        Text("Read input file from")
      }
      TextField(
        value = startingLine.toString(),
        leadingIcon = { Text("Line:") },
        onValueChange = { startingLine = it.toULongOrNull() ?: 0UL },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
      )
      val showNegativeNumberDialog by remember { derivedStateOf { startingLine < 0UL } }
      MyDefaultAlertDialog(
        showNegativeNumberDialog,
        title = { Text("Invalid Number") },
        text = { Text("The number of lines to skip cannot be negative.") },
      ) {
        startingLine = 0UL
      }
    }
    LoadingScreenRow {
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
      Button(onClick = { outputFileChosenModel.showFilePicker = true }) {
        Text(outputFile?.let { "Re-choose CSV File" } ?: "Choose Output CSV File")
      }
    }
  }
  MyFilePicker(inputFileChosenModel.showFilePicker, listOf(".txt")) {
    inputFileChosenModel.file = it
    inputFileChosenModel.showFilePicker = false
  }
  MyFilePicker(outputFileChosenModel.showFilePicker, listOf(".csv")) {
    outputFileChosenModel.file = it
    outputFileChosenModel.showFilePicker = false
  }
  val isReady by remember { derivedStateOf { inputFileChosenModel.file != null && outputFileChosenModel.file != null } }
  if (isReady) {
    onReady(IOFiles(inputFileChosenModel.file!!, startingLine, outputFileChosenModel.file!!))
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
  MyAppTheme {
    LoadingScreen(onReady = {})
  }
}
