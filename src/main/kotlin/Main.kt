@file:OptIn(ExperimentalUnitApi::class)

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import components.EvidenceList
import components.GhostList
import data.json.DataManager
import data.json.model.Evidence
import data.json.model.Ghost
import data.structures.TriState
import logic.Resolver
import org.jnativehook.GlobalScreen
import org.jnativehook.keyboard.NativeKeyEvent
import org.jnativehook.keyboard.NativeKeyListener
import theme.AppTheme
import java.util.logging.Level
import java.util.logging.Logger

private var windowState: TriState by mutableStateOf(TriState.TRUE)
private var showTips: Boolean by mutableStateOf(false)
//private var openDialog: Boolean by mutableStateOf(false)

private var evidenceList = EvidenceList()
private val ghostList = GhostList()
//private val confirmGhostPopup = ConfirmGhostPopup()

private val ghosts = SnapshotStateList<Ghost>()
private val evidences = SnapshotStateList<Evidence>()
private val selectedEvidences = SnapshotStateList<Evidence>()
private var availableGhosts = SnapshotStateList<Ghost>()
private val availableEvidences = SnapshotStateList<Evidence>()

private var dataManager = DataManager()
private val resolver =
    mutableStateOf(Resolver(dataManager, ghosts, evidences, selectedEvidences, availableGhosts, availableEvidences))

@Preview
fun main() = application {
    val icon = BitmapPainter(loadImageBitmap(javaClass.classLoader.getResourceAsStream("assets/icons/program/phasmo-helper.ico")))

    //TODO release focus when window is hidden
    Window(
        onCloseRequest = ::exitApplication,
        title = "Phasmophobia Helper",
        transparent = true,
        undecorated = true,
        alwaysOnTop = true,
        resizable = false,
        icon = icon,
        state = WindowState(WindowPlacement.Floating, false, WindowPosition(0.dp, 0.dp), 400.dp, 600.dp),
    ) {
        Main().App()
    }

    //TODO release focus when window is hidden
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
        AnimatedVisibility(openDialog) {
            confirmGhostPopup.build()
        }
    }
     */
}

class Main : NativeKeyListener {
    private val log: Logger = Logger.getLogger(GlobalScreen::class.java.getPackage().name)

    @Preview
    @Composable
    fun App() {
        content()

        GlobalScreen.registerNativeHook()
        GlobalScreen.addNativeKeyListener(this@Main)

        log.useParentHandlers = false
        log.level = Level.INFO
    }

    @Composable
    fun content() {
        AppTheme(useDarkTheme = true) {

            AnimatedVisibility(windowState != TriState.TRALSE) {
                Surface(
                    modifier = Modifier.padding(15.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.padding(15.dp)) {
                        evidenceList.build(windowState == TriState.TRUE, evidences, selectedEvidences, availableEvidences)
                        ghostList.build(
                            windowState == TriState.TRUE,
                            (showTips && selectedEvidences.size >= 2),
                            ghosts,
                            availableGhosts
                        )
                    }
                }
            }
        }
    }

    override fun nativeKeyPressed(nativeKeyEvent: NativeKeyEvent?) {
        val key = nativeKeyEvent?.paramString()?.let { getKeyFromParamString(it) }

        for (evidence in evidences) {
            if (evidence.keyBinding == key) {
                if (selectedEvidences.contains(evidence)) {
                    selectedEvidences.remove(evidence)
                } else {
                    selectedEvidences.add(evidence)
                }
                resolver.value.update()
            }
        }

        when (key) {
            "NumPad 0" -> {
                windowState = windowState.next()
            }
            "NumPad Separator" -> {
                showTips = !showTips
            }
            else -> println(key)
        }
    }

    override fun nativeKeyReleased(p0: NativeKeyEvent?) {
    }

    override fun nativeKeyTyped(p0: NativeKeyEvent?) {
    }

    private fun getKeyFromParamString(input: String): String {
        val split = input.split(",")

        for (string in split) {
            if (string.contains("keyText")) {
                return string.split("=")[1]
            }
        }
        return "Stijn is dik"
    }
}