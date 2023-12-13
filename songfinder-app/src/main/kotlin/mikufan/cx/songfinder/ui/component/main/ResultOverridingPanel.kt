package mikufan.cx.songfinder.ui.component.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.coroutines.launch
import mikufan.cx.songfinder.backend.controller.mainpage.ResultOverridingController
import mikufan.cx.songfinder.getSpringBean
import mikufan.cx.songfinder.ui.common.MyDefaultAlertDialog
import mikufan.cx.songfinder.ui.common.RowCentralizedWithSpacing
import mikufan.cx.songfinder.ui.theme.spacing

@Composable
fun ResultOverridingPanel(
  controller: ResultOverridingController = getSpringBean(),
) {
  val model = ResultOverridingPanelModel(controller::overrideResultAndContinue, controller.currentInputState)
  RealResultOverridingPanel(model)
}

@Composable
fun RealResultOverridingPanel(model: ResultOverridingPanelModel) {
  Column(
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spacing),
    modifier = Modifier
  ) {
    TitleRow()
    DoubleCheckHint(model.currentInputState)
    GuideToCreateNewSong()
    OverrideResultRow(model.onOverride)
  }
}

@Composable
fun TitleRow() = RowCentralizedWithSpacing {
  Text("No result found?", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
}

@Composable
fun DoubleCheckHint(currentInputState: State<String>) = RowCentralizedWithSpacing {
  val uriHandler = LocalUriHandler.current
  Text("Please check again on the official VocaDB site")
  OutlinedButton(onClick = { uriHandler.openUri("https://vocadb.net/Search?searchType=Song&filter=${currentInputState.value}") }) {
    Text("Search on VocaDB")
  }
}

@Composable
fun GuideToCreateNewSong() = RowCentralizedWithSpacing {
  Text("If you are 100% sure this song is not on VocaDB, then")
  val uriHandler = LocalUriHandler.current
  OutlinedButton(onClick = { uriHandler.openUri("https://vocadb.net/Song/Create") }) {
    Text("Create new song on VocaDB")
  }
}

@Composable
fun OverrideResultRow(onOverride: suspend (ULong) -> Unit) = RowCentralizedWithSpacing {
  var inputValueState by remember { mutableStateOf(0uL) }
  val scope = rememberCoroutineScope()
  Text("And override the result with any VocaDB Song ID")
  VocaDbIdOverridingTextField(inputValueState) { inputValueState = it }
  OutlinedButton(onClick = {
    scope.launch { onOverride(inputValueState) }
    inputValueState = 0uL
  }) {
    Text("Override and continue")
  }
}

@Composable
fun VocaDbIdOverridingTextField(
  initialInputValue: ULong,
  onValueChange: (ULong) -> Unit,
) {
  var inputValue by mutableStateOf(initialInputValue.toLong())

  OutlinedTextField(
    value = inputValue.toString(),
    onValueChange = {
      inputValue = if (it.isBlank()) 0L else it.toLongOrNull() ?: return@OutlinedTextField
      val toSetValue = if (inputValue >= 0L) inputValue.toULong() else 0UL
      onValueChange(toSetValue)
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
            "Invalid Song ID",
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            color = MaterialTheme.colorScheme.error
          )
        }
      },
      text = { Text("The VocaDB Song ID cannot be negative.") },
    ) {
      inputValue = 0L
    }
  }
}


data class ResultOverridingPanelModel(
  val onOverride: suspend (ULong) -> Unit,
  val currentInputState: State<String>,
)