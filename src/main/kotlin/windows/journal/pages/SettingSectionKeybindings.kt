@file:OptIn(ExperimentalUnitApi::class)

package windows.journal.pages

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import data.json.model.Settings
import logic.ActionTypes

class SettingSectionKeybindings(private val settings: Settings) : SettingSection {
    @Composable
    override fun build() {
        val keybindings = SnapshotStateMap<String, String>()
        keybindings.putAll(settings.keybindings)

        Column(
            modifier = Modifier.verticalScroll(ScrollState(0))
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colors.background
            ) {
                Text(
                    modifier = Modifier.padding(20.dp),
                    fontSize = TextUnit(2.0F, TextUnitType.Em),
                    color = MaterialTheme.colors.onPrimary,
                    text = "Keybindings"
                )
            }

            Column() {
                for (actionType in ActionTypes.values()) {
                    Row(
                    ) {
                        Surface(
                            shape = RoundedCornerShape(20.dp, 0.dp, 0.dp, 20.dp),
                            color = MaterialTheme.colors.surface,
                            modifier = Modifier.padding(20.dp, 0.dp, 0.dp, 20.dp).fillMaxWidth(0.3F)
                        ) {
                            Text(
                                color = MaterialTheme.colors.onPrimary,
                                text = actionType.displayName,
                                modifier = Modifier.padding(20.dp)
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(0.dp, 20.dp, 20.dp, 0.dp),
                            color = MaterialTheme.colors.surface,
                            modifier = Modifier.padding(0.dp, 0.dp, 20.dp, 20.dp).fillMaxWidth(0.5F).clickable {

                            }
                        ) {
                            Text(
                                color = MaterialTheme.colors.onPrimary,
                                text = "${if (keybindings.containsKey(actionType.name)) keybindings[actionType.name] else actionType.standardKeybinding}",
                                modifier = Modifier.padding(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}