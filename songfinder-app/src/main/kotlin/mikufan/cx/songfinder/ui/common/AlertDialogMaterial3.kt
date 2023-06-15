package mikufan.cx.songfinder.ui.common

import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

/**
 * Displays an AlertDialog in Material Design 3 Style from the Material Design library.
 *
 * @param showDialog Whether to show the dialog or not.
 * @param onDismissRequest Callback to be invoked when the dialog is dismissed.
 * @param confirmButton Composable function that displays the confirm-button.
 * @param modifier Modifier to be applied to the dialog.
 * @param dismissButton Composable function that displays the dismiss-button, if any.
 * @param title Composable function that displays the dialog title, if any.
 * @param text Composable function that displays the dialog text, if any.
 * @param shape Shape of the dialog.
 * @param backgroundColor Background color of the dialog.
 * @param contentColor Content color of the dialog.
 * @param dialogProvider Provider of the AlertDialog.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AlertDialogMaterial3(
  showDialog: Boolean,
  onDismissRequest: () -> Unit,
  confirmButton: @Composable () -> Unit,
  modifier: Modifier = Modifier,
  dismissButton: @Composable (() -> Unit)? = null,
  title: @Composable (() -> Unit)?,
  text: @Composable (() -> Unit)?,
  shape: Shape = MaterialTheme.shapes.medium,
  backgroundColor: Color = MaterialTheme.colorScheme.background,
  contentColor: Color = contentColorFor(backgroundColor),
  dialogProvider: AlertDialogProvider = PopupAlertDialogProvider,
) {
  if (showDialog) {
    AlertDialog(
      onDismissRequest = onDismissRequest,
      confirmButton = confirmButton,
      modifier = modifier,
      dismissButton = dismissButton,
      title = title,
      text = text,
      shape = shape,
      backgroundColor = backgroundColor,
      contentColor = contentColor,
      dialogProvider = dialogProvider,
    )
  }
}
