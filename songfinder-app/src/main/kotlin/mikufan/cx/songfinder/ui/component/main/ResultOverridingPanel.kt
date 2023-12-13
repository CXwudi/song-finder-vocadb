package mikufan.cx.songfinder.ui.component.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.coroutines.CoroutineScope
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
  val scope = rememberCoroutineScope()
  val model = ResultOverridingPanelModel(
    scope,
    controller::overrideResultAndContinue,
    controller.currentInputState,
    controller.inputIdState,
    controller::updateInputId,
    controller.buttonEnabledState,
    controller.shouldShowAlertState,
  )
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
    OverrideResultRow(model)
  }
}

@Composable
fun TitleRow() = RowCentralizedWithSpacing {
  Text("No result found?", style = MaterialTheme.typography.titleMedium)
}

@Composable
fun DoubleCheckHint(currentInputState: State<String>) = RowCentralizedWithSpacing {
  val uriHandler = LocalUriHandler.current
  Text("Please check again on the official VocaDB site")
  OutlinedButton(onClick = {
    uriHandler.openUri("https://vocadb.net/Search?searchType=Song&filter=${currentInputState.value}")
  }) {
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
fun OverrideResultRow(model: ResultOverridingPanelModel) = RowCentralizedWithSpacing {
//  var inputValueState by remember { mutableStateOf(0uL) }
//  val shouldBeDisabled by remember { derivedStateOf { inputValueState == 0uL } }
//  val onOverrideCallback = {
//    if (!shouldBeDisabled) {
//      model.onOverride(inputValueState)
//      inputValueState = 0uL
//    }
//
//  }
  Text("And override the result with any VocaDB Song ID")
  VocaDbIdOverridingTextField(model)
  Button(onClick = { model.scope.launch { model.onOverride() } }, enabled = model.buttonEnabledState.value) {
    Text("Override and continue")
  }
}

@Composable
fun VocaDbIdOverridingTextField(
  model: ResultOverridingPanelModel,
) {
  val (scope, onOverride, _, inputIdState, onInputIdChange, _, shouldShowAlertState) = model
  OutlinedTextField(
    value = inputIdState.value.toString(),
    onValueChange = {
      val inputValue = if (it.isBlank()) 0L else it.toLongOrNull() ?: return@OutlinedTextField
      onInputIdChange(inputValue)
    },
    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
    keyboardActions = KeyboardActions(onDone = {
      scope.launch {
        onOverride()
      }
    }),
    modifier = Modifier.onKeyEvent {
      if (it.key != Key.Enter) return@onKeyEvent false
      scope.launch {
        onOverride()
      }
      true
    }
  )

  if (shouldShowAlertState.value) {
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
      onInputIdChange(0L)
    }
  }
}


data class ResultOverridingPanelModel(
  val scope: CoroutineScope,
  val onOverride: suspend () -> Unit,
  val currentInputState: State<String>,
  val inputIdState: State<Long>,
  val onInputIdChange: (Long) -> Unit,
  val buttonEnabledState: State<Boolean>,
  val shouldShowAlertState: State<Boolean>,
)