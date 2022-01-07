@file:OptIn(ExperimentalUnitApi::class)

package components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
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
import logic.Resolver

class GhostList {
    @Composable
    fun build(
        resolver: Resolver,
        showGhosts: Boolean,
        showTips: Boolean,
        ghosts: SnapshotStateList<Ghost>,
        availableGhosts: SnapshotStateList<Ghost>
    ) {
        AnimatedVisibility(showGhosts) {
            Column(modifier = Modifier.padding(0.dp, 15.dp)) {
                AnimatedVisibility(availableGhosts.isNotEmpty()) {
                    Text(
                        color = MaterialTheme.colors.onSurface,
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
                                    color = MaterialTheme.colors.onSurface
                                )
                                Text(
                                    color = MaterialTheme.colors.onSurface,
                                    fontSize = TextUnit(1.0f, TextUnitType.Em),
                                    fontWeight = FontWeight.Bold,
                                    text = it.displayName
                                )
                                AnimatedVisibility(showTips) {
                                    var remainingEvidences = "•"

                                    for (evidence in resolver.getRemainingGhostEvidences(it)) {
                                        remainingEvidences += " ${evidence.displayName} and"
                                    }

                                    remainingEvidences = remainingEvidences.removeSuffix("and")

                                    var text = it.tips
                                    if (text == "") {
                                        text = it.weakness
                                    }

                                    Column {
                                        Text(
                                            color = Color.LightGray,
                                            fontSize = TextUnit(1.0f, TextUnitType.Em),
                                            text = remainingEvidences
                                        )

                                        Text(
                                            color = Color.LightGray,
                                            fontSize = TextUnit(1.0f, TextUnitType.Em),
                                            text = "• $text"
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