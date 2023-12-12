package mikufan.cx.songfinder.backend.controller.mainpage

import org.springframework.stereotype.Controller

@Controller
class ResultOverridingController(
  private val resultSelectedIntermediateController: ResultSelectedIntermediateController,
) {

  suspend fun overrideResultAndContinue(id: ULong) {
    resultSelectedIntermediateController.writeRecordAndMoveOn(id)
  }
}