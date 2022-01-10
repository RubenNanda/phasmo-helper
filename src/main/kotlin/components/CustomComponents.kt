package components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import theme.AppTheme
import java.io.IOException

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

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Phasmophobia Helper",
        transparent = true,
        undecorated = true,
        alwaysOnTop = true,
        resizable = false,
        state = WindowState(
            WindowPlacement.Floating, false, WindowPosition(Alignment.Center), 400.dp, 600.dp
        ),
    ) {
        AppTheme(useDarkTheme = true) {
            ExpandableCard(
                title = "Strategy",
                enabled = false,
                modifier = Modifier.fillMaxSize(0.5f),
            ) {
                Text("A banshee is a fucking horrible ghost and i do not like it")
            }
        }
    }
}

@Composable
@Preview
fun ExpandableCard(
    title: String,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.surface,
    textColor: Color = Color.White,
    content: @Composable () -> Unit,
) {
    var expand by mutableStateOf(true)

    Card(
        //backgroundColor = backgroundColor,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column {
            Row {
                IconButton(

                    onClick = {
                        expand = !expand
                    },

                    ) {
                    Icon(

                        Icons.Filled.ArrowDropDown,
                        "contentDescription",
                        tint = textColor
                    )
                }
                Text(text = title, color = textColor, modifier = Modifier.align(Alignment.CenterVertically))
            }
            Divider(color = if(expand) textColor else backgroundColor)
            AnimatedVisibility(expand) {
                Box(
                    modifier = Modifier.padding(20.dp)
                ) {
                    CompositionLocalProvider(content = content)
                }
            }
        }
    }
}