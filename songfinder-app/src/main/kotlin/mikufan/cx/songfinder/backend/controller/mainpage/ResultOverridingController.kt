package mikufan.cx.songfinder.backend.controller.mainpage

import androidx.compose.runtime.State
import mikufan.cx.songfinder.backend.statemodel.OverridingResultStateModel
import mikufan.cx.songfinder.backend.statemodel.SearchInputStateModel
import org.springframework.stereotype.Controller

@Controller
class ResultOverridingController(
  private val resultSelectedIntermediateController: ResultSelectedIntermediateController,
  inputStateModel: SearchInputStateModel,
  private val overridingResultStateModel: OverridingResultStateModel,
) {

  val currentInputState: State<String> = inputStateModel.currentInputState
  val inputIdState: State<Long> = overridingResultStateModel.inputIdState
  val buttonEnabledState: State<Boolean> = overridingResultStateModel.buttonEnabledState
  val shouldShowAlertState: State<Boolean> = overridingResultStateModel.shouldShowAlertState

  suspend fun overrideResultAndContinue() {
    check(buttonEnabledState.value) { "button should be enabled when calling this function" }
    resultSelectedIntermediateController.writeRecordAndMoveOn(inputIdState.value.toULong())
  }

  fun updateInputId(id: Long) {
    overridingResultStateModel.inputIdState.value = id
  }
}