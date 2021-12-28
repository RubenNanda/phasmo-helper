package data.json

import com.google.gson.Gson
import data.json.model.Evidence
import data.json.model.EvidenceHelperList
import data.json.model.Ghost
import data.json.model.GhostHelperList
import java.io.File
import java.lang.reflect.Type

class DataManager {
    enum class File(val filePath: String, val type: Type) {
        GHOST("src/main/resources/assets/json/ghost.json", GhostHelperList::class.java),
        EVIDENCE("src/main/resources/assets/json/evidence.json", EvidenceHelperList::class.java);
    }

    private val gson = Gson()

    fun saveFile(outputFile: File, any: Any) {
        val file = File(outputFile.filePath)

        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.createNewFile()
        }
        println(file.absolutePath)

        file.bufferedWriter().use { out -> out.write(gson.toJson(any)) }
    }

    private fun loadFile(inputFile: File): Any? {
        val file = File(inputFile.filePath)

        if (!file.exists()) return null

        var jsonString = ""
        file.bufferedReader().forEachLine { jsonString += it }
        return gson.fromJson(file.bufferedReader(), inputFile.type)
    }

    fun getGhosts(): List<Ghost> {
        val file = loadFile(File.GHOST)

        if(file is GhostHelperList){
            return file.ghostList
        }

        throw Exception("Not sure how you did this")
    }

    fun getEvidences(): List<Evidence> {
        val file = loadFile(File.EVIDENCE)

        if(file is EvidenceHelperList){
            return file.evidenceList
        }

        throw Exception("Not sure how you did this")
    }
}