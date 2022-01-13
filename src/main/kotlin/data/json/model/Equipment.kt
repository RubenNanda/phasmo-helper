package data.json.model

data class Equipment(
    var displayName: String,
    var sections: MutableList<Pair<String, String>>,
) : JsonModel

