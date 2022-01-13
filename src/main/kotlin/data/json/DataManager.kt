package data.json

import com.google.gson.Gson
import com.google.gson.JsonParser
import data.json.model.*
import java.lang.reflect.Type

class DataManager {
    private val gson = Gson()

    enum class DataFile(val filePath: String, val type: Type) {
        GHOST("assets/json/ghost.json", GhostHelperList::class.java),
        EVIDENCE("assets/json/evidence.json", EvidenceHelperList::class.java),

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

    private fun loadFile(inputFile: DataFile): Any? {
        return gson.fromJson(javaClass.classLoader.getResource(inputFile.filePath).readText(), inputFile.type)
    }

    private fun loadPage(inputPath: String): Any? {
        return gson.fromJson(javaClass.classLoader.getResource(inputPath).readText(), Page::class.java)
    }

    fun getGhosts(): List<Ghost> {
        val file = loadFile(DataFile.GHOST)

        if (file is GhostHelperList) {
            return file.ghostList
        }

        throw Exception("Not sure how you did this")
    }

    fun getEvidences(): List<Evidence> {
        val file = loadFile(DataFile.EVIDENCE)

        if (file is EvidenceHelperList) {
            return file.evidenceList
        }

        throw Exception("Not sure how you did this")
    }

    fun getPages(): List<Page> {
        val pageList = mutableListOf<Page>()
        val config =
            JsonParser.parseString(javaClass.classLoader.getResource("assets/page/config.json").readText()).asJsonObject

        for (path in config.get("pages").asJsonArray) {
            val page = loadPage(path.asJsonObject.get("path").asString)

            if (page is Page) {
                pageList.add(page)
            }
        }

        return pageList
    }
}