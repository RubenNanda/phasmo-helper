package data.json

import Main
import com.google.gson.Gson
import data.json.model.Evidence
import data.json.model.EvidenceHelperList
import data.json.model.Ghost
import data.json.model.GhostHelperList
import java.io.File
import java.lang.reflect.Type

class DataManager {

    enum class File(val filePath: String, val type: Type) {
        GHOST("assets/json/ghost.json", GhostHelperList::class.java),
        EVIDENCE("assets/json/evidence.json", EvidenceHelperList::class.java);
    }

    private val gson = Gson()

    /*
    fun saveFile(outputFile: File, any: Any) {
        val file = File(Main::class.java.classLoader.getResource(outputFile.filePath).path)

        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.createNewFile()
        }
        println(file.absolutePath)

        file.bufferedWriter().use { out -> out.write(gson.toJson(any)) }
    }
     */

    private fun loadFile(inputFile: File): Any? {
        return gson.fromJson(javaClass.classLoader.getResource(inputFile.filePath).readText(), inputFile.type)
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