// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import data.phasmo.Evidence
import data.structures.TriState
import org.jnativehook.GlobalScreen
import org.jnativehook.keyboard.NativeKeyEvent
import org.jnativehook.keyboard.NativeKeyListener
import java.util.logging.Level
import java.util.logging.Logger

private var windowState: TriState by mutableStateOf(TriState.TRUE)
private var evidenceMap = SnapshotStateMap<Evidence, Boolean>()
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Phasmophobia Helper",
        transparent = true,
        undecorated = true,
        alwaysOnTop = true,
        state = WindowState(WindowPlacement.Floating, false, WindowPosition(0.dp, 0.dp)),
        resizable = false,
    ) {
        Main().App()
    }

}

class Main : NativeKeyListener {
    private val log: Logger = Logger.getLogger(GlobalScreen::class.java.getPackage().name)

    @Preview
    @Composable
    fun App() {
        Evidence.values().forEach { evidence ->
            evidenceMap[evidence] = false
        }

        content()

        GlobalScreen.registerNativeHook()
        GlobalScreen.addNativeKeyListener(this@Main)

        log.useParentHandlers = false;
        log.level = Level.INFO;
    }

    @Composable
    @OptIn(ExperimentalUnitApi::class, ExperimentalComposeUiApi::class)
    fun content() {
        MaterialTheme {
            when (windowState) {
                TriState.TRUE -> { // full size
                    Surface(
                        modifier = Modifier.padding(15.dp).shadow(3.dp, RoundedCornerShape(20.dp)),
                        color = Color(55, 55, 55, 180),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(modifier = Modifier.padding(15.dp)) {
                            Text(text = "Phasmophobia Helper 3.0", fontSize = TextUnit(1.5f, TextUnitType.Em))
                            Text(text = "Evidences:")

                            LazyColumn {
                                items(Evidence.values()) { evidence ->
                                    Row {
                                        Text(text = evidence.keyBinding.removePrefix("NumPad "))
                                        evidenceMap[evidence]?.let {
                                            Checkbox(
                                                checked = it,
                                                onCheckedChange = null
                                            )
                                        }

                                        Image(
                                            painter = painterResource(evidence.getIconPath()),
                                            contentDescription = "item icon",
                                        )
                                        Text(text = evidence.displayName)
                                    }
                                }
                            }
                        }
                    }
                }
                TriState.FALSE -> { // half size
                    Surface(
                        modifier = Modifier.padding(15.dp).shadow(3.dp, RoundedCornerShape(20.dp)),
                        color = Color(55, 55, 55, 180),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        LazyColumn(modifier = Modifier.padding(15.dp)) {
                            items(Evidence.values()) { evidence ->
                                Row {
                                    evidenceMap[evidence]?.let {
                                        Checkbox(
                                            checked = it,
                                            onCheckedChange = null
                                        )
                                    }
                                    Image(
                                        painter = painterResource(evidence.getIconPath()),
                                        contentDescription = "item icon",
                                    )
                                }
                            }
                        }
                    }
                }
                TriState.TRALSE -> { // closed

                }
            }
        }
    }

    override fun nativeKeyPressed(nativeKeyEvent: NativeKeyEvent?) {
        val key = nativeKeyEvent?.paramString()?.let { getKeyFromParamString(it) }

        for(evidence in Evidence.values()){
            if(evidence.keyBinding == key){
                evidenceMap[evidence] = !evidenceMap[evidence]!!
            }
        }

        when (key) {
            "NumPad 0" -> {
                windowState = windowState.next()
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