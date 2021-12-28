package components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.json.model.Ghost
import logic.GhostChecker

class GhostList(private val ghostList: SnapshotStateList<Ghost>, private val ghostChecker: MutableState<GhostChecker>) {
    @Composable
    fun build(visible: Boolean) {
        AnimatedVisibility(visible) {
            Column(modifier = Modifier.padding(0.dp, 15.dp)) {
                Text(text = "Ghosts:")
                LazyColumn {
                    items(ghostList) {
                        AnimatedVisibility(ghostChecker.component1().check(it)) {
                            Text(text = it.displayName)
                        }
                    }
                }
            }
        }
    }
}