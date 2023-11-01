package mikufan.cx.songfinder.backend.controller.mainpage

import androidx.compose.runtime.State
import mikufan.cx.songfinder.backend.statemodel.ProgressStateModel
import org.springframework.stereotype.Controller

@Controller
class ProgressBarController(
  private val progressModel: ProgressStateModel,
) {

  val currentIndexState: State<ULong> get() = progressModel.currentIndexState
  val totalCount = progressModel.totalCount
}