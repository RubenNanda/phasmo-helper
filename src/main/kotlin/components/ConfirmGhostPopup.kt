@file:OptIn(ExperimentalUnitApi::class)

package components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import data.json.model.Ghost
import kotlin.math.roundToInt

class ConfirmGhostPopup() {
    @Composable
    fun build(ghosts: SnapshotStateList<Ghost>) {
        Card(
            modifier = Modifier.fillMaxWidth().shadow(3.dp, RoundedCornerShape(20.dp)),
            backgroundColor = Color(55, 55, 55, 180),
        ) {
            val rowSize = 3
            val itemCount = ghosts.size
            val columnCount = (itemCount.toDouble() / rowSize).roundToInt()
            Column(modifier = Modifier.padding(15.dp)) {
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = Color.White,
                    fontSize = TextUnit(1.0f, TextUnitType.Em),
                    text = "What was the actual ghost?"
                )

                for (y in 0 until columnCount) {
                    Row() {
                        for (x in 0 until rowSize) {
                            val index = y * rowSize + x

                            Button(
                                modifier = Modifier.width(145.dp).padding(2.5.dp),
                                onClick = {

                                }) {
                                Text(ghosts.toMutableList()[index].displayName)
                            }
                        }
                    }
                }
            }
        }
    }
}