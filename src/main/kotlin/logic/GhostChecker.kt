package logic

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import data.json.DataManager
import data.json.model.Evidence
import data.json.model.Ghost

fun main() {
    val dataManager = DataManager()
    val ghostChecker = GhostChecker(dataManager)

    ghostChecker.toggleEvidence(ghostChecker.evidences[1])

    for (ghost in ghostChecker.ghosts) {
        println("${ghost.displayName} : ${ghostChecker.availableGhosts.contains(ghost)}")
    }

    for (evidence in ghostChecker.evidences) {
        println("${evidence.displayName} : ${ghostChecker.availableEvidences.contains(evidence)}")
    }
}

class GhostChecker(dataManager: DataManager) {
    val ghosts = SnapshotStateList<Ghost>()
    val evidences = SnapshotStateList<Evidence>()
    val selectedEvidences = SnapshotStateMap<Evidence, Boolean>()
    var availableGhosts = mutableSetOf<Ghost>()
    val availableEvidences = SnapshotStateMap<Evidence, Boolean>()

    init {
        ghosts.addAll(dataManager.getGhosts())
        evidences.addAll(dataManager.getEvidences())

        for (evidence in evidences) {
            selectedEvidences[evidence] = false
        }
    }

    fun toggleEvidence(evidence: Evidence) {
        selectedEvidences[evidence] = !selectedEvidences[evidence]!!
        update()
    }

    fun update() {
        availableGhosts.clear()

        for (ghost in ghosts) {
            for (evidence in evidences) {
                val isEvidence = selectedEvidences[evidence]
                if (isEvidence == true && ghost.hasEvidence(evidence)) {
                    availableGhosts.add(ghost)
                }
            }
        }
    }

    fun check(evidence: Evidence): Boolean {

        return true
    }

    fun check(ghost: Ghost): Boolean {
        val ghostEvidence: MutableList<Evidence> = mutableListOf()
        ghostEvidence.add(getEvidenceFromString(ghost.evidenceOne))
        ghostEvidence.add(getEvidenceFromString(ghost.evidenceTwo))
        ghostEvidence.add(getEvidenceFromString(ghost.evidenceThree))

        var checkAllFalse = true
        for (evidence in selectedEvidences.keys) {
            val isEvidence = selectedEvidences[evidence]
            if (isEvidence == true && !ghostEvidence.contains(evidence)) return false
            if (isEvidence != false) checkAllFalse = false
        }

        if (checkAllFalse) {
            return false
        }

        return true
    }

    private fun getEvidenceFromString(input: String): Evidence {
        for (evidence in selectedEvidences.keys) {
            if (evidence.displayName == input) return evidence
        }

        throw Exception("Evidence $input not found")
    }
}