package mikufan.cx.songfinder.backend.controller.mainpage

import androidx.compose.runtime.State
import mikufan.cx.songfinder.backend.statemodel.SearchInputStateModel
import org.springframework.stereotype.Controller

@Controller
class ResultOverridingController(
  private val resultSelectedIntermediateController: ResultSelectedIntermediateController,
  inputStateModel: SearchInputStateModel,
) {

  val currentInputState: State<String> = inputStateModel.currentInputState

  suspend fun overrideResultAndContinue(id: ULong) {
    resultSelectedIntermediateController.writeRecordAndMoveOn(id)
  }
}