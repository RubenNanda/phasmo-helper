package data.json.model

import logic.ActionTypes

class Settings(
    var keybindings: MutableMap<String, String>,
) : JsonModel {

    fun getKeybinding(actionType: ActionTypes) = keybindings[actionType.name]
}
