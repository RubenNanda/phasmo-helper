@file:OptIn(ExperimentalUnitApi::class, ExperimentalMaterialApi::class)

package windows.journal.pages

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import components.ExpandableCard
import data.json.model.Page

open class JournalPage(private val page: Page) : windows.journal.pages.Page {
    var selectedItem by mutableStateOf(page.pageItemList.first())

    override fun displayName() = page.displayName

    @Composable
    override fun build() {
        Row {
            // Item select button column
            Surface(
                modifier = Modifier.fillMaxHeight(),
                color = MaterialTheme.colors.surface,
                shape = RoundedCornerShape(0.dp, 0.dp, 0.dp, 20.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.3f).padding(20.dp).verticalScroll(ScrollState(0))
                ) {
                    for (item in page.pageItemList) {
                        Surface(
                            modifier = Modifier.fillMaxWidth().height(60.dp).padding(0.dp, 0.dp, 0.dp, 20.dp),
                            color = if (selectedItem == item) MaterialTheme.colors.primary else MaterialTheme.colors.secondary,
                            shape = RoundedCornerShape(20.dp),
                            onClick = {
                                selectedItem = item
                            }) {
                            Text(
                                modifier = Modifier.padding(20.dp, 10.dp, 0.dp, 0.dp),
                                text = item.displayName,
                                color = MaterialTheme.colors.onPrimary,
                            )
                        }
                    }
                }
            }

            // Selected item content
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background,
                shape = RoundedCornerShape(0.dp, 20.dp, 20.dp, 0.dp)
            ) {
                Column(
                    modifier = Modifier.verticalScroll(ScrollState(0))
                ) {
                    Surface(
                        modifier = Modifier.fillMaxWidth().padding(20.dp, 20.dp, 20.dp, 20.dp),
                        color = MaterialTheme.colors.background,
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = selectedItem.displayName,
                            color = MaterialTheme.colors.onPrimary,
                            fontSize = TextUnit(2.0F, TextUnitType.Em),
                            modifier = Modifier.padding(20.dp)
                        )
                    }
                    for (section in selectedItem.sections) {
                        Box(
                            modifier = Modifier.padding(20.dp, 0.dp, 20.dp, 20.dp)
                        ) {
                            ExpandableCard(
                                modifier = Modifier.fillMaxWidth(),
                                title = section.first,
                                startExpanded = true,
                                backgroundColor = MaterialTheme.colors.surface
                            ) {
                                Text(
                                    text = section.second,
                                    color = MaterialTheme.colors.onPrimary
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun resetSelected() {
        selectedItem = page.pageItemList.first()
    }
}