package mikufan.cx.songfinder.backend.statemodel

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import org.springframework.stereotype.Component

@Component
class OverridingResultStateModel {

  val inputIdState = mutableStateOf(0L)
  val buttonEnabledState = derivedStateOf { inputIdState.value > 0L }
  val shouldShowAlertState = derivedStateOf { inputIdState.value < 0L }
}