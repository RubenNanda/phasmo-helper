package windows.journal.pages

import androidx.compose.runtime.toMutableStateList
import data.json.DataManager

class JournalPageEquipment(dataManager: DataManager) :
    JournalPage("Equipment", dataManager.getPage().toMutableStateList()) {
}