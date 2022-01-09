@file:OptIn(DelicateCoroutinesApi::class)

package logic

import androidx.compose.runtime.snapshots.SnapshotStateList
import data.json.model.Evidence
import data.structures.TriState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import org.jnativehook.GlobalScreen
import org.jnativehook.keyboard.NativeKeyEvent
import org.jnativehook.keyboard.NativeKeyListener
import windows.popup.Popup
import java.util.logging.Level
import java.util.logging.Logger

class KeyListener(
    private val resolver: Resolver,
    private val popup: Popup,
    private val evidences: SnapshotStateList<Evidence>,
    private val selectedEvidences: SnapshotStateList<Evidence>,
) : NativeKeyListener {
    init {
        val log: Logger = Logger.getLogger(GlobalScreen::class.java.getPackage().name)
        GlobalScreen.registerNativeHook()
        GlobalScreen.addNativeKeyListener(this@KeyListener)

        log.useParentHandlers = false
        log.level = Level.INFO
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
                resolver.update()
            }
        }

        when (key) {
            "NumPad 0" -> {
                popup.windowState = popup.windowState.next()

                if (popup.windowState == TriState.TRALSE) {
                    GlobalScope.async {
                        delay(250)
                        popup.isMinimized = true
                    }
                } else if (popup.windowState == TriState.TRUE) {
                    popup.isMinimized = false
                }
            }
            "NumPad Separator" -> {
                popup.showTips = !popup.showTips
            }
            "NumPad Subtract" -> {
                resolver.clearEvidences()
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
