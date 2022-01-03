@file:OptIn(ExperimentalUnitApi::class)

package components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.res.loadXmlImageVector
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import data.json.model.Evidence
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xml.sax.InputSource
import java.io.File
import java.io.IOException
import java.net.URL

class EvidenceList(val evidenceMap: SnapshotStateMap<Evidence, Boolean>) {
    @Composable
    fun build(showName: Boolean) {
        LazyColumn {
            items(evidenceMap.keys.toList().sortedBy { evidence -> evidence.keyBinding.removePrefix("NumPad ").toInt() }) { evidence ->
                Row {
                    Text(
                        color = Color.White,
                        fontSize = TextUnit(1.0f, TextUnitType.Em),
                        text = evidence.keyBinding.removePrefix("NumPad ")
                    )
                    evidenceMap[evidence]?.let {
                        Checkbox(
                            checked = it,
                            onCheckedChange = null
                        )
                    }

                    AsyncImage(
                        load = { loadImageBitmap(javaClass.classLoader.getResourceAsStream(evidence.iconPath)) },
                        painterFor = { remember { BitmapPainter(it) } },
                        contentDescription = "Evidence Icon",
                    )

                    AnimatedVisibility(showName) {
                        Text(
                            color = Color.White,
                            fontSize = TextUnit(1.0f, TextUnitType.Em),
                            text = evidence.displayName
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun <T> AsyncImage(
        load: suspend () -> T,
        painterFor: @Composable (T) -> Painter,
        contentDescription: String,
        modifier: Modifier = Modifier,
        contentScale: ContentScale = ContentScale.Fit,
    ) {
        val image: T? by produceState<T?>(null) {
            value = withContext(Dispatchers.IO) {
                try {
                    load()
                } catch (e: IOException) {
                    // instead of printing to console, you can also write this to log,
                    // or show some error placeholder
                    e.printStackTrace()
                    null
                }
            }
        }

        if (image != null) {
            Image(
                painter = painterFor(image!!),
                contentDescription = contentDescription,
                contentScale = contentScale,
                modifier = modifier
            )
        }
    }

/* Loading from file with java.io API */

    fun loadImageBitmap(file: File): ImageBitmap =
        file.inputStream().buffered().use(::loadImageBitmap)

    fun loadSvgPainter(file: File, density: Density): Painter =
        file.inputStream().buffered().use { loadSvgPainter(it, density) }

    fun loadXmlImageVector(file: File, density: Density): ImageVector =
        file.inputStream().buffered().use { loadXmlImageVector(InputSource(it), density) }

/* Loading from network with java.net API */

    fun loadImageBitmap(url: String): ImageBitmap =
        URL(url).openStream().buffered().use(::loadImageBitmap)

    fun loadSvgPainter(url: String, density: Density): Painter =
        URL(url).openStream().buffered().use { loadSvgPainter(it, density) }

    fun loadXmlImageVector(url: String, density: Density): ImageVector =
        URL(url).openStream().buffered().use { loadXmlImageVector(InputSource(it), density) }
}