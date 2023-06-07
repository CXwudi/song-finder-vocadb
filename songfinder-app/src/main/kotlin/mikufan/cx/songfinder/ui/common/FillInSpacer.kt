package mikufan.cx.songfinder.ui.common

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * @author CX无敌
 * 2023-06-07
 */

@Composable
fun ColumnScope.FillInSpacer(furtherModifier: Modifier = Modifier) = Spacer(modifier = Modifier.weight(1f).then(furtherModifier))

@Composable
fun RowScope.FillInSpacer(furtherModifier: Modifier = Modifier) = Spacer(modifier = Modifier.weight(1f).then(furtherModifier))
