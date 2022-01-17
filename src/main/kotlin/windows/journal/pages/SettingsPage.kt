@file:OptIn(ExperimentalMaterialApi::class)

package windows.journal.pages

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.json.model.Settings

class SettingsPage(settings: Settings) : Page {
    private val settingSectionMap: MutableMap<String, SettingSection> = mutableMapOf()

    init {
        settingSectionMap["Keybindings"] = SettingSectionKeybindings(settings)
    }

    var selectedSection by mutableStateOf(settingSectionMap.keys.first())

    override fun displayName(): String = "Settings"

    @Composable
    override fun build() {
        Row {
            // Item select button column
            Surface(
                modifier = Modifier.fillMaxHeight(),
                color = MaterialTheme.colors.surface,
                shape = RoundedCornerShape(0.dp, 0.dp, 0.dp, 20.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.3f).padding(20.dp).verticalScroll(ScrollState(0))
                ) {
                    for (section in settingSectionMap) {
                        Surface(
                            modifier = Modifier.fillMaxWidth().height(60.dp).padding(0.dp, 0.dp, 0.dp, 20.dp),
                            color = if (selectedSection == section.key) MaterialTheme.colors.primary else MaterialTheme.colors.secondary,
                            shape = RoundedCornerShape(20.dp),
                            onClick = {
                                selectedSection = section.key
                            }) {
                            Text(
                                modifier = Modifier.padding(20.dp, 10.dp, 0.dp, 0.dp),
                                text = section.key,
                                color = MaterialTheme.colors.onPrimary,
                            )
                        }
                    }
                }
            }

            // Selected item content
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background,
                shape = RoundedCornerShape(0.dp, 20.dp, 20.dp, 0.dp)
            ) {
                settingSectionMap[selectedSection]?.build()
            }
        }
    }

    override fun resetSelected() {

    }
}