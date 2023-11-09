package mikufan.cx.songfinder.backend.controller

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import mikufan.cx.songfinder.backend.statemodel.ProgressStateModel
import org.springframework.stereotype.Controller

@Controller
class MainScreenController(
  private val progressStateModel: ProgressStateModel
) {

  val isFinishedState: State<Boolean> = derivedStateOf { progressStateModel.currentIndexState.value >= progressStateModel.totalCount }

}