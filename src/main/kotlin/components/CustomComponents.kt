package components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

@Composable
@Preview
fun ExpandableCard(
    title: String,
    modifier: Modifier = Modifier,
    startExpanded: Boolean = false,
    backgroundColor: Color = MaterialTheme.colors.surface,
    textColor: Color = Color.White,
    content: @Composable () -> Unit,
) {
    var expand by mutableStateOf(startExpanded)

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth().clickable {
            expand = !expand
        }
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().padding(10.dp)
            ) {
                Icon(
                    Icons.Filled.ArrowDropDown,
                    "expandable card",
                    tint = textColor
                )
                Text(
                    text = title,
                    color = textColor,
                    modifier = Modifier.align(Alignment.CenterVertically).padding(0.dp, 0.dp, 20.dp, 0.dp)
                )
            }

            AnimatedVisibility(expand) {
                Divider(modifier = Modifier.padding(20.dp, 0.dp))
                Box(
                    modifier = Modifier.padding(20.dp)
                ) {
                    CompositionLocalProvider(content = content)
                }
            }
        }
    }
}