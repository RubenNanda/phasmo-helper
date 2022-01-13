package windows.journal.pages

import androidx.compose.runtime.Composable

interface JournalPage {
    fun displayName(): String

    @Composable
    fun content()
}