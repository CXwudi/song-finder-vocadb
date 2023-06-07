package mikufan.cx.songfinder.ui.common

import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

/**
 * @author CX无敌
 * 2023-06-06
 */

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyDefaultAlertDialog(
  showDialog: Boolean,
  title: @Composable () -> Unit,
  text: @Composable () -> Unit,
  onDismissOrOk: () -> Unit,
) {
  if (showDialog) {
    AlertDialog(
      onDismissRequest = onDismissOrOk,
      title = title,
      text = text,
      confirmButton = {
        Button(onClick = onDismissOrOk) {
          Text(text = "OK")
        }
      },
      // the AlertDialog doesn't come from material3, so we need to set the color manually
      backgroundColor = MaterialTheme.colorScheme.background,
      contentColor = MaterialTheme.colorScheme.onBackground,
    )
  }
}
