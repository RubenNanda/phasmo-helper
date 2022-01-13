@file:OptIn(ExperimentalMaterialApi::class, ExperimentalUnitApi::class)

package windows.journal.pages

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import components.ExpandableCard
import data.json.model.Equipment

class JournalPageEquipment(private val equipments: SnapshotStateList<Equipment>) : JournalPage {
    override fun displayName(): String = "Evidences"

    @Composable
    override fun content() {
        var selectedEquipment by mutableStateOf(equipments.first())
        Row {
            Card(
                modifier = Modifier.fillMaxHeight(),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.3f).padding(20.dp)
                ) {
                    for (equipment in equipments) {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                selectedEquipment = equipment
                            }) {
                            Text(equipment.displayName)
                        }
                    }
                }
            }
            Column(
                modifier = Modifier.verticalScroll(ScrollState(0))
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = selectedEquipment.displayName,
                        fontSize = TextUnit(2.0F, TextUnitType.Em),
                        modifier = Modifier.padding(20.dp)
                    )
                }
                for (section in selectedEquipment.sections) {
                    ExpandableCard(
                        modifier = Modifier.fillMaxWidth(),
                        title = section.first,
                        startExpanded = true
                    ) {
                        Text(text = section.second)
                    }
                }
            }
        }
    }
}