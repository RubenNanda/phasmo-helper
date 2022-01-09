@file:OptIn(ExperimentalUnitApi::class, DelicateCoroutinesApi::class)

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import data.json.DataManager
import data.json.model.Evidence
import data.json.model.Ghost
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import logic.KeyListener
import logic.Resolver
import windows.popup.Popup
import java.io.IOException

private val ghosts = SnapshotStateList<Ghost>()
private val evidences = SnapshotStateList<Evidence>()
private val selectedEvidences = SnapshotStateList<Evidence>()
private var availableGhosts = SnapshotStateList<Ghost>()
private val availableEvidences = SnapshotStateList<Evidence>()

private var dataManager = DataManager()
private val resolver = Resolver(dataManager, ghosts, evidences, selectedEvidences, availableGhosts, availableEvidences)
private var popup = Popup(resolver, ghosts, evidences, selectedEvidences, availableGhosts, availableEvidences)

//Needs to be declared here even though value is never accessed.
//Initializing in main causes multiple instances to be created.
@Suppress("Unused")
val keyListener = KeyListener(resolver, popup, evidences, selectedEvidences)

@Preview
fun main() = application {
    val icon =
        BitmapPainter(loadImageBitmap(javaClass.classLoader.getResourceAsStream("assets/icons/program/phasmo-helper.ico")))

    Window(
        onCloseRequest = ::exitApplication,
        title = "Phasmophobia Helper",
        transparent = true,
        undecorated = true,
        alwaysOnTop = true,
        resizable = false,
        icon = icon,
        state = WindowState(
            WindowPlacement.Floating, popup.isMinimized, WindowPosition(0.dp, 0.dp), 400.dp, 600.dp
        ),
    ) {
        popup.content()
    }

    /*
    Window(
        onCloseRequest = { exitApplication() },
        title = "Phasmophobia Helper",
        transparent = true,
        undecorated = true,
        alwaysOnTop = true,
        state = WindowState(WindowPlacement.Floating, false, WindowPosition(Alignment.Center), 450.dp, 450.dp),
        resizable = false,
    ) {

    }
     */
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