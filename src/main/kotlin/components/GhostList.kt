@file:OptIn(ExperimentalUnitApi::class)

package components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import data.json.model.Ghost

class GhostList() {
    @Composable
    fun build(visible: Boolean, ghosts: SnapshotStateList<Ghost>, availableGhosts: SnapshotStateList<Ghost>) {
        AnimatedVisibility(visible) {
            Column(modifier = Modifier.padding(0.dp, 15.dp)) {
                Text(
                    color = Color.White,
                    fontSize = TextUnit(1.0f, TextUnitType.Em),
                    text = "Ghosts:"
                )

                LazyColumn {
                    items(ghosts) {
                        AnimatedVisibility(availableGhosts.contains(it)) {
                            Text(
                                color = Color.White,
                                fontSize = TextUnit(1.0f, TextUnitType.Em),
                                text = it.displayName
                            )
                        }
                    }
                }
            }
        }
    }
}