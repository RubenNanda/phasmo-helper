package logic

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import data.json.DataManager
import data.json.model.Evidence
import data.json.model.Ghost

class Resolver(
    dataManager: DataManager,
    private val ghosts: SnapshotStateList<Ghost>,
    private val evidences: SnapshotStateList<Evidence>,
    private val selectedEvidences: SnapshotStateMap<Evidence, Boolean>,
    private var availableGhosts: SnapshotStateList<Ghost>,
    private val availableEvidences: SnapshotStateList<Evidence>
) {
    init {
        ghosts.addAll(dataManager.getGhosts())
        evidences.addAll(dataManager.getEvidences())

        for (evidence in evidences) {
            selectedEvidences[evidence] = false
        }
    }

    fun clearEvidences() {
        for (evidence in evidences) {
            selectedEvidences[evidence] = false
        }
        update()
    }

    fun update() {
        val removeGhosts: MutableSet<Ghost> = mutableSetOf()
        var checkAllFalse = true

        for (ghost in ghosts) {
            val ghostEvidence: MutableSet<Evidence> = mutableSetOf()
            ghostEvidence.add(getEvidenceFromString(ghost.evidenceOne))
            ghostEvidence.add(getEvidenceFromString(ghost.evidenceTwo))
            ghostEvidence.add(getEvidenceFromString(ghost.evidenceThree))

            for (evidence in evidences) {
                val isEvidence = selectedEvidences[evidence]
                if (isEvidence == true) checkAllFalse = false
                if (isEvidence == true && !ghostEvidence.contains(evidence)) {
                    removeGhosts.add(ghost)
                    break
                }
            }
        }

        availableGhosts.clear()
        if (!checkAllFalse) {
            availableGhosts.addAll(ghosts)
            availableGhosts.removeAll(removeGhosts)
        }

        val removeEvidences: MutableSet<Evidence> = mutableSetOf()
        for (ghost in availableGhosts) {
            removeEvidences.add(getEvidenceFromString(ghost.evidenceOne))
            removeEvidences.add(getEvidenceFromString(ghost.evidenceTwo))
            removeEvidences.add(getEvidenceFromString(ghost.evidenceThree))
        }

        availableEvidences.clear()
        if (availableGhosts.isNotEmpty()) {
            availableEvidences.addAll(evidences)
            availableEvidences.removeAll(removeEvidences)
        }
    }

    private fun getEvidenceFromString(input: String): Evidence {
        for (evidence in evidences) {
            if (evidence.displayName == input) return evidence
        }

        throw Exception("Evidence $input not found")
    }
}