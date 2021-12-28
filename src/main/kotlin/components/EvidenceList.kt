package components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.res.painterResource
import data.json.model.Evidence

class EvidenceList(val evidenceMap: SnapshotStateMap<Evidence, Boolean>) {
    @Composable
    fun build(showName: Boolean) {
        LazyColumn {
            items(evidenceMap.keys.toList()) { evidence ->
                Row {
                    Text(text = evidence.keyBinding.removePrefix("NumPad "))
                    evidenceMap[evidence]?.let {
                        Checkbox(
                            checked = it,
                            onCheckedChange = null
                        )
                    }

                    Image(
                        painter = painterResource(evidence.iconPath),
                        contentDescription = "item icon",
                    )
                    AnimatedVisibility(showName) {
                        Text(text = evidence.displayName)
                    }
                }
            }
        }
    }
}