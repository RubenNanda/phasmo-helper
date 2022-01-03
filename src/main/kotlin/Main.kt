// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import components.ConfirmGhostPopup
import components.EvidenceList
import components.GhostList
import data.json.DataManager
import data.json.model.Evidence
import data.json.model.Ghost
import data.structures.TriState
import logic.GhostChecker
import org.jnativehook.GlobalScreen
import org.jnativehook.keyboard.NativeKeyEvent
import org.jnativehook.keyboard.NativeKeyListener
import java.util.logging.Level
import java.util.logging.Logger

private var windowState: TriState by mutableStateOf(TriState.TRUE)
private var openDialog: Boolean by mutableStateOf(false)

private val ghosts = SnapshotStateList<Ghost>()

private val evidenceMap: SnapshotStateMap<Evidence, Boolean> = SnapshotStateMap()
private val ghostChecker = mutableStateOf(GhostChecker(evidenceMap))

private var evidenceList = EvidenceList(evidenceMap)
private val ghostList = GhostList(ghosts, ghostChecker)
private val confirmGhostPopup = ConfirmGhostPopup(ghosts)

@Preview
@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
fun main() = application {
    //TODO release focus when window is hidden
    Window(
        onCloseRequest = ::exitApplication,
        title = "Phasmophobia Helper",
        transparent = true,
        undecorated = true,
        alwaysOnTop = true,
        state = WindowState(WindowPlacement.Floating, false, WindowPosition(0.dp, 0.dp), 330.dp, 470.dp),
        resizable = false,
    ) {
        val dataManager = DataManager()

        ghosts.addAll(dataManager.getGhosts())

        dataManager.getEvidences().forEach { evidence ->
            evidenceMap[evidence] = false
        }

        Main().App()
    }

    //TODO release focus when window is hidden
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
    @OptIn(
        ExperimentalUnitApi::class, ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class,
        ExperimentalFoundationApi::class
    )
    fun content() {
        MaterialTheme {
            AnimatedVisibility(windowState != TriState.TRALSE) {
                Surface(
                    modifier = Modifier.padding(15.dp).shadow(3.dp, RoundedCornerShape(20.dp)),
                    color = Color(55, 55, 55, 180),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.padding(15.dp)) {
                        evidenceList.build(windowState == TriState.TRUE)
                        ghostList.build(windowState == TriState.TRUE)
                    }
                }
            }
        }
    }

    override fun nativeKeyPressed(nativeKeyEvent: NativeKeyEvent?) {
        val key = nativeKeyEvent?.paramString()?.let { getKeyFromParamString(it) }
        val evidenceMap = evidenceList.evidenceMap

        for(evidence in evidenceMap.keys){
            if(evidence.keyBinding == key){
                evidenceMap[evidence] = !evidenceMap[evidence]!!
            }
        }

        when (key) {
            "NumPad 0" -> {
                windowState = windowState.next()
            }
            "Enter" -> {
                openDialog = !openDialog
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