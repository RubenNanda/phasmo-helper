@file:OptIn(ExperimentalUnitApi::class)

package components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import data.json.model.Ghost

class GhostList() {
    @Composable
    fun build(
        showGhosts: Boolean,
        showTips: Boolean,
        ghosts: SnapshotStateList<Ghost>,
        availableGhosts: SnapshotStateList<Ghost>
    ) {
        AnimatedVisibility(showGhosts) {
            Column(modifier = Modifier.padding(0.dp, 15.dp)) {
                AnimatedVisibility(availableGhosts.isNotEmpty()){
                    Text(
                        color = Color.White,
                        fontSize = TextUnit(1.0f, TextUnitType.Em),
                        fontWeight = FontWeight.Bold,
                        text = "Ghosts:"
                    )
                }
                LazyColumn {
                    items(ghosts) {
                        AnimatedVisibility(availableGhosts.contains(it)) {
                            Column(modifier = Modifier.padding(0.dp, 2.5.dp)) {
                                Divider(
                                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 2.5.dp),
                                    color = Color.LightGray
                                )
                                Text(
                                    color = Color.White,
                                    fontSize = TextUnit(1.0f, TextUnitType.Em),
                                    fontWeight = FontWeight.Bold,
                                    text = it.displayName
                                )
                                AnimatedVisibility(showTips) {
                                    if (it.tips != "") {
                                        Text(
                                            color = Color.LightGray,
                                            fontSize = TextUnit(1.0f, TextUnitType.Em),
                                            text = "• ${it.tips}"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}