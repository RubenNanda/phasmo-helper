package windows.journal.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

class SettingsPage : Page {
    override fun displayName(): String = "Settings"

    @Composable
    override fun build() {
        Row {
            Column(
                modifier = Modifier.fillMaxWidth(0.3F)
            ) {
                Text("Test")
                Text("Test")
                Text("Test")

            }
        }
    }

    override fun resetSelected() {

    }
}