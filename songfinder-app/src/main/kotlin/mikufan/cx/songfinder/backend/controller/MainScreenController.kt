package mikufan.cx.songfinder.backend.controller

import mikufan.cx.songfinder.backend.statemodel.ProgressStateModel
import org.springframework.stereotype.Controller

@Controller
class MainScreenController(
  private val progressStateModel: ProgressStateModel
) {

  val isFinished get() = progressStateModel.currentIndexState.value >= progressStateModel.totalCount
}