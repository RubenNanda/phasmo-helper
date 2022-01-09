@file:OptIn(ExperimentalAnimationApi::class)

package windows.journal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import theme.AppTheme

class Journal {
    var windowState: Boolean by mutableStateOf(true)
    var isMinimized: Boolean by mutableStateOf(false)

    var pages = listOf("Example 1", "Example 2", "Example 3")

    @Preview
    @Composable
    fun content() {
        var selectedPage by remember { mutableStateOf(pages[0]) }

        AppTheme(useDarkTheme = true) {
            AnimatedVisibility(visible = windowState, enter = scaleIn(), exit = scaleOut()) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.height(50.dp),
                        ) {
                            for (page in pages) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxHeight().background(
                                        if (selectedPage == page) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
                                        RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp)
                                    ).clickable {
                                        selectedPage = page
                                    },
                                ) {
                                    Text(modifier = Modifier.padding(20.dp, 0.dp), text = page)
                                }
                            }
                        }
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            shape = RoundedCornerShape(0.dp, 20.dp, 20.dp, 20.dp),
                            color = MaterialTheme.colors.primary
                        ) {

                        }
                    }
                }
            }
        }
    }
}