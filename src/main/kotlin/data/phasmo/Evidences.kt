package data.phasmo

enum class Evidences(val displayName: String) {
    EMF_LEVEL_5("EMF level 5"),
    FINGERPRINTS("Fingerprints"),
    FREEZING_TEMPERATURES("Freezing Temperatures"),
    GHOST_ORB("Ghost Orb"),
    GHOST_WRITING("Ghost Writing"),
    SPIRIT_BOX("Spirit Box"),
    DOTS_PROJECTOR("D.O.T.S Projector");

    fun getIconPath(): String {
        return "./assets/icons/evidences/" + this.name + ".png"
    }
}