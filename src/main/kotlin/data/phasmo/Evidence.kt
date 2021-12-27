package data.phasmo

import androidx.compose.runtime.Composable

enum class Evidence(val displayName: String, var keyBinding: String) {
    EMF_LEVEL_5("EMF level 5", "NumPad 1"),
    FINGERPRINTS("Fingerprints", "NumPad 2"),
    FREEZING_TEMPERATURES("Freezing Temperatures", "NumPad 3"),
    GHOST_ORB("Ghost Orb", "NumPad 4"),
    GHOST_WRITING("Ghost Writing", "NumPad 5"),
    SPIRIT_BOX("Spirit Box", "NumPad 6"),
    DOTS_PROJECTOR("D.O.T.S Projector", "NumPad 7");

    fun getIconPath(): String {
        return "./assets/icons/evidences/" + this.name + ".png"
    }
}