package logic

import androidx.compose.runtime.snapshots.SnapshotStateList
import data.json.DataManager
import data.json.model.Evidence
import data.json.model.Ghost

class Resolver(
    dataManager: DataManager,
    private val ghosts: SnapshotStateList<Ghost>,
    private val evidences: SnapshotStateList<Evidence>,
    private val selectedEvidences: SnapshotStateList<Evidence>,
    private var availableGhosts: SnapshotStateList<Ghost>,
    private val availableEvidences: SnapshotStateList<Evidence>
) {
    init {
        ghosts.addAll(dataManager.getGhosts())
        evidences.addAll(dataManager.getEvidences())
    }

    fun clearEvidences() {
        selectedEvidences.clear()
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
                val isEvidence = selectedEvidences.contains(evidence)
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

    fun getEvidenceFromString(input: String): Evidence {
        for (evidence in evidences) {
            if (evidence.displayName == input) return evidence
        }

        throw Exception("Evidence $input not found")
    }

    fun getGhostEvidences(ghost: Ghost): Set<Evidence> {
        val evidences = mutableSetOf<Evidence>()
        evidences.add(getEvidenceFromString(ghost.evidenceOne))
        evidences.add(getEvidenceFromString(ghost.evidenceTwo))
        evidences.add(getEvidenceFromString(ghost.evidenceThree))
        return evidences
    }

    fun getRemainingGhostEvidences(ghost: Ghost): Set<Evidence> {
        val evidences = getGhostEvidences(ghost).toMutableSet()
        for (evidence in selectedEvidences) {
            evidences.remove(evidence)
        }
        return evidences
    }
}