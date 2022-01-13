package data.json

import com.google.gson.Gson
import data.json.model.*
import java.lang.reflect.Type

class DataManager {
    private val gson = Gson()

    enum class File(val filePath: String, val type: Type) {
        GHOST("assets/json/ghost.json", GhostHelperList::class.java),
        EVIDENCE("assets/json/evidence.json", EvidenceHelperList::class.java),

    }

    enum class Page(val filePath:String, val type: Type){
        EQUIPMENT_PAGE("assets/page/equipment.json", PageItemList::class.java);
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

    private fun loadFile(inputFile: Page): Any? {
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

    fun getPage(): List<PageItem> {
        val file = loadFile(Page.EQUIPMENT_PAGE)

        if (file is PageItemList) {
            return file.pageItemList
        }

        throw Exception("Not sure how you did this")
    }
}