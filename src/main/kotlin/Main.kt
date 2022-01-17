@file:OptIn(ExperimentalUnitApi::class, DelicateCoroutinesApi::class)

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import data.json.DataManager
import data.json.model.Evidence
import data.json.model.Ghost
import data.json.model.Settings
import kotlinx.coroutines.DelicateCoroutinesApi
import logic.ActionTypes
import logic.KeyListener
import logic.Resolver
import windows.journal.Journal
import windows.popup.Popup

private var dataManager = DataManager()

private val settings: Settings = dataManager.getSettings()
private val ghosts = SnapshotStateList<Ghost>()
private val evidences = SnapshotStateList<Evidence>()
private val selectedEvidences = SnapshotStateList<Evidence>()
private var availableGhosts = SnapshotStateList<Ghost>()

private val availableEvidences = SnapshotStateList<Evidence>()
private val resolver = Resolver(dataManager, ghosts, evidences, selectedEvidences, availableGhosts, availableEvidences)
private var popup = Popup(resolver, ghosts, evidences, selectedEvidences, availableGhosts, availableEvidences)
private val journal = Journal(dataManager)

//Needs to be declared here even though value is never accessed.
//Initializing in main causes multiple instances to be created.
@Suppress("Unused")
val keyListener = KeyListener(settings, resolver, popup, journal,  evidences, selectedEvidences)

@Preview
fun main() = application {


    for(actionType in ActionTypes.values()){
        if(!settings.keybindings.containsKey(actionType.name)){
            settings.keybindings[actionType.name] = actionType.standardKeybinding
        }
    }

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
            WindowPlacement.Floating, popup.isMinimized, WindowPosition(0.dp, 0.dp), 400.dp, 700.dp
        ),
    ) {
        popup.content()
    }

    Window(
        onCloseRequest = { exitApplication() },
        title = "Phasmophobia Journal",
        transparent = true,
        undecorated = true,
        alwaysOnTop = true,
        resizable = false,
        icon = icon,
        state = WindowState(WindowPlacement.Floating,
            journal.isMinimized,
            WindowPosition(Alignment.Center),
            1000.dp,
            1000.dp),
    ) {
        journal.content()
    }
}