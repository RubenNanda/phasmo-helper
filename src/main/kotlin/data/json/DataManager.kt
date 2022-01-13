package data.json

import com.google.gson.Gson
import data.json.model.*
import java.lang.reflect.Type

class DataManager {
    private val gson = Gson()

    enum class File(val filePath: String, val type: Type) {
        GHOST("assets/json/ghost.json", GhostHelperList::class.java),
        EVIDENCE("assets/json/evidence.json", EvidenceHelperList::class.java),
        EQUIPMENT("assets/json/equipment.json", PageItemList::class.java);
    }

    /*
    fun saveFile(outputFile: File, any: Any) {
        val file = File(this::class.java.classLoader.getResource(outputFile.filePath).path)

        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.createNewFile()
        }
        println(file.absolutePath)

        println(gson.toJson(any))
        file.bufferedWriter().use { out -> out.write(gson.toJson(any)) }
    }
     */

    private fun loadFile(inputFile: File): Any? {
        return gson.fromJson(javaClass.classLoader.getResource(inputFile.filePath).readText(), inputFile.type)
    }

    fun getGhosts(): List<Ghost> {
        val file = loadFile(File.GHOST)

        if (file is GhostHelperList) {
            return file.ghostList
        }

        throw Exception("Not sure how you did this")
    }

    fun getEvidences(): List<Evidence> {
        val file = loadFile(File.EVIDENCE)

        if (file is EvidenceHelperList) {
            return file.evidenceList
        }

        throw Exception("Not sure how you did this")
    }

    fun getEquipment(): List<PageItem> {
        val file = loadFile(File.EQUIPMENT)

        if (file is PageItemList) {
            return file.pageItemList
        }

        throw Exception("Not sure how you did this")
    }
}