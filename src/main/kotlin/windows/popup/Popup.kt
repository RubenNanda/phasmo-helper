package windows.popup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.json.model.Evidence
import data.json.model.Ghost
import data.structures.TriState
import logic.Resolver
import theme.AppTheme
import windows.popup.components.EvidenceList
import windows.popup.components.GhostList

class Popup(
    private val resolver: Resolver,
    private val ghosts: SnapshotStateList<Ghost>,
    private val evidences: SnapshotStateList<Evidence>,
    private val selectedEvidences: SnapshotStateList<Evidence>,
    private var availableGhosts: SnapshotStateList<Ghost>,
    private val availableEvidences: SnapshotStateList<Evidence>,
) {
    var windowState: TriState by mutableStateOf(TriState.TRUE)
    var isMinimized: Boolean by mutableStateOf(false)
    var showTips: Boolean by mutableStateOf(true)

    private val evidenceList = EvidenceList()
    private val ghostList = GhostList()

    @Composable
    fun content() {
        AppTheme(useDarkTheme = true) {
            AnimatedVisibility(windowState != TriState.TRALSE) {
                Surface(
                    modifier = Modifier.padding(15.dp),
                    color = MaterialTheme.colors.background,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(text = "")

                    Column(modifier = Modifier.padding(15.dp)) {
                        evidenceList.build(
                            windowState == TriState.TRUE,
                            evidences,
                            selectedEvidences,
                            availableEvidences
                        )
                        ghostList.build(
                            resolver,
                            windowState == TriState.TRUE,
                            (showTips && selectedEvidences.size >= 2),
                            ghosts,
                            availableGhosts
                        )
                    }
                }
            }
        }
    }
}
