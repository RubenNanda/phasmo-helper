package data.json.model

data class PageItem(
    var displayName: String,
    var sections: MutableList<Pair<String, String>>,
) : JsonModel

