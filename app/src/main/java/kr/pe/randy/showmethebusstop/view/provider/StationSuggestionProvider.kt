package kr.pe.randy.showmethebusstop.view.provider

import android.content.SearchRecentSuggestionsProvider

class StationSuggestionProvider: SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(
            AUTHORITY,
            MODE
        )
    }

    companion object {
        const val AUTHORITY = "kr.pe.randy.StationSuggestionProvider"
        const val MODE: Int = DATABASE_MODE_QUERIES
    }
}