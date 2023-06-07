package mikufan.cx.songfinder.ui.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import mikufan.cx.songfinder.model.IOFiles
import mikufan.cx.songfinder.ui.common.FillInSpacer
import mikufan.cx.songfinder.ui.common.MyDefaultAlertDialog
import mikufan.cx.songfinder.ui.common.TooltipAreaWithCard
import mikufan.cx.songfinder.ui.theme.MyAppTheme
import mikufan.cx.songfinder.ui.theme.MyDefaultSpacing
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

  Column(
    modifier = modifier.padding(vertical = MyDefaultSpacing),
    verticalArrangement = Arrangement.spacedBy(MyDefaultSpacing),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    LoadingScreenHeader()
    Divider()
    InputFilePicker(inputFileChosenModel)
    StartingLineInputField { startingLine = it }
    OutputFilePicker(outputFileChosenModel)
    FillInSpacer()
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
fun previewOfLoadingScreen() {
  MyAppTheme {
    LoadingScreen(onReady = {})
  }
}

@Composable
private fun LoadingScreenHeader() {
  LoadingScreenRow {
    Text(
      "Please choose your input and output files.",
      fontSize = MaterialTheme.typography.headlineMedium.fontSize,
    )
  }
  LoadingScreenRow {
    Text(
      "This window will close as soon as valid input and output files are chosen.",
    )
  }
}

@Composable
private fun InputFilePicker(inputFileChosenModel: FileChosenModel) = LoadingScreenRow {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StartingLineInputField(onStartingLineValueChange: (ULong) -> Unit) = LoadingScreenRow {
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
  MyDefaultAlertDialog(
    showNegativeNumberDialog,
    title = { Text("Invalid Number") },
    text = { Text("The number of lines to skip cannot be negative.") },
  ) {
    inputValue = 0L
  }
}

@Composable
private fun OutputFilePicker(
  outputFileChosenModel: FileChosenModel,
) = LoadingScreenRow {
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
fun LoadingScreenRow(content: @Composable RowScope.() -> Unit) {
  Row(
    modifier = Modifier.padding(horizontal = MyDefaultSpacing),
    horizontalArrangement = Arrangement.spacedBy(MyDefaultSpacing),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    content()
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
    initialDirectory = Path(".").toAbsolutePath().toString(),
    fileExtensions = fileExtensions,
  ) { file ->
    onShowStatusChanged(false)
    file?.path?.let { onFilePicked(Path(it)) }
  }
}

class FileChosenModel {
  var file: Path? by mutableStateOf(null)
  var showFilePicker: Boolean by mutableStateOf(false)
}
