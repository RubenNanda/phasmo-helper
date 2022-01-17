package windows.journal.pages

import androidx.compose.runtime.Composable

interface Page {
    fun displayName(): String

    @Composable
    fun build()

    fun resetSelected()
}