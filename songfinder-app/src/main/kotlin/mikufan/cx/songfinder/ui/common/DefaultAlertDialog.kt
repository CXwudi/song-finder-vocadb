package mikufan.cx.songfinder.ui.common

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Button
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
) = AlertDialogMaterial3(
  showDialog = showDialog,
  onDismissRequest = onDismissOrOk,
  title = title,
  text = text,
  confirmButton = {
    Button(onClick = onDismissOrOk) {
      Text(text = "OK")
    }
  },
)
