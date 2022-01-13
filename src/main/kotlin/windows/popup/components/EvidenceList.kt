@file:OptIn(ExperimentalUnitApi::class)

package windows.popup.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import components.AsyncImage
import data.json.model.Evidence

class EvidenceList {
    @OptIn(ExperimentalUnitApi::class)
    @Composable
    fun build(
        showName: Boolean,
        evidences: SnapshotStateList<Evidence>,
        selectedEvidences: SnapshotStateList<Evidence>,
        availableEvidences: SnapshotStateList<Evidence>
    ) {
        LazyColumn {
            items(evidences.sortedBy { evidence ->
                evidence.keyBinding.removePrefix("NumPad ").toInt()
            }) { evidence ->
                Row {
                    Text(
                        color = MaterialTheme.colors.onSurface,
                        fontSize = TextUnit(1.0f, TextUnitType.Em),
                        text = evidence.keyBinding.removePrefix("NumPad ")
                    )

                    selectedEvidences.contains(evidence).let {
                        Checkbox(
                            colors = CheckboxDefaults.colors(
                                uncheckedColor = if (availableEvidences.contains(evidence)) MaterialTheme.colors.error else MaterialTheme.colors.onSurface,
                                checkmarkColor = MaterialTheme.colors.onSurface
                            ),
                            checked = it,
                            onCheckedChange = null
                        )
                    }

                    AsyncImage(
                        load = { loadImageBitmap(javaClass.classLoader.getResourceAsStream(evidence.iconPath)) },
                        painterFor = { remember { BitmapPainter(it) } },
                        contentDescription = "Evidence Icon",
                    )

                    AnimatedVisibility(showName) {
                        Text(
                            color = MaterialTheme.colors.onSurface,
                            fontSize = TextUnit(1.0f, TextUnitType.Em),
                            text = evidence.displayName
                        )
                    }
                }
            }
        }
    }
}