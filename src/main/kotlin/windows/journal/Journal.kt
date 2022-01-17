@file:OptIn(ExperimentalAnimationApi::class)

package windows.journal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.desktop.ui.tooling.preview.Preview
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
import data.json.DataManager
import theme.AppTheme
import windows.journal.pages.JournalPage
import windows.journal.pages.Page
import windows.journal.pages.SettingsPage

class Journal(
    dataManager: DataManager
) {
    var windowState: Boolean by mutableStateOf(true)
    var isMinimized: Boolean by mutableStateOf(false)

    private var pages = mutableStateListOf<Page>()

    init {
        for(page in dataManager.getPages()){
            pages.add(JournalPage(page))
        }
        pages.add(SettingsPage(dataManager.getSettings()))
    }

    @Preview
    @Composable
    fun content() {
        var selectedPage by remember { mutableStateOf(pages[0]) }

        AppTheme(useDarkTheme = true) {
            AnimatedVisibility(visible = windowState, enter = scaleIn(), exit = scaleOut()) {
                Surface(
                    color = MaterialTheme.colors.background,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxSize(),
                    ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        // Page select button row
                        Row(
                            modifier = Modifier.height(50.dp),
                        ) {
                            for (page in pages) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxHeight().background(
                                        if (selectedPage == page) MaterialTheme.colors.primary else MaterialTheme.colors.secondary,
                                        RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp)
                                    ).clickable {
                                        selectedPage = page
                                        page.resetSelected()
                                    },
                                ) {
                                    Text(
                                        modifier = Modifier.padding(20.dp, 0.dp),
                                        text = page.displayName(),
                                        color = MaterialTheme.colors.onPrimary
                                    )
                                }
                            }
                        }

                        // Selected page content
                        CompositionLocalProvider(content = { selectedPage.build() })
                    }
                }
            }
        }
    }
}