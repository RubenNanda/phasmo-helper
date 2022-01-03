package data.json.model

data class Ghost(
    var displayName: String,
    var evidenceOne: String,
    var evidenceTwo: String,
    var evidenceThree: String,
    var description: String,
    var strength: String,
    var weakness: String,
) {
    fun hasEvidence(evidence: Evidence) : Boolean {
        var isEvidence = false
        if(evidenceOne == evidence.displayName) isEvidence = true
        if(evidenceTwo == evidence.displayName) isEvidence = true
        if(evidenceThree == evidence.displayName) isEvidence = true
        return isEvidence
    }
}
