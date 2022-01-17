package logic

enum class ActionTypes(val displayName: String, val standardKeybinding: String) {
    TOGGLE_POPUP("Toggle Popup", "NumPad 0"),
    TOGGLE_JOURNAL("Toggle Journal", "NumPad Add"),
    TOGGLE_TIPS("Toggle Tips", "NumPad Separator"),
    UNSELECT_EVIDENCES("Unselect Evidences", "NumPad Subtract")
}