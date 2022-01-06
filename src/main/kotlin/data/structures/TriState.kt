package data.structures

enum class TriState {
    TRUE,
    FALSE,
    TRALSE;

    fun next(): TriState {
        val state = this
        var index = 1
        for (input in values()) {
            if (state == input) {
                break
            }
            index++
        }

        if (index == values().size) {
            index = 0
        }

        return values()[index]
    }
}