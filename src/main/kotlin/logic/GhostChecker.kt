package logic

import androidx.compose.runtime.snapshots.SnapshotStateMap
import data.json.model.Evidence
import data.json.model.Ghost

class GhostChecker(private val evidenceMap: SnapshotStateMap<Evidence, Boolean>) {

    fun check(ghost: Ghost): Boolean {
        val ghostEvidence: MutableList<Evidence> = mutableListOf()
        ghostEvidence.add(getEvidenceFromString(ghost.evidenceOne))
        ghostEvidence.add(getEvidenceFromString(ghost.evidenceTwo))
        ghostEvidence.add(getEvidenceFromString(ghost.evidenceThree))

        var checkAllFalse = true
        for (evidence in evidenceMap.keys) {
            val isEvidence = evidenceMap[evidence]

            if (isEvidence == true && !ghostEvidence.contains(evidence)) return false
            if (isEvidence != false) checkAllFalse = false
        }

        if (checkAllFalse) {
            return false
        }

        return true
    }

    private fun getEvidenceFromString(input: String): Evidence {
        for (evidence in evidenceMap.keys) {
            if (evidence.displayName == input) return evidence
        }

        throw Exception("Evidence $input not found")
    }
}