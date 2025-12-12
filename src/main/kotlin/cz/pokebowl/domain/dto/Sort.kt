package cz.pokebowl.domain.dto

enum class SortOrder {
    ASCENDING,
    DESCENDING;

    companion object {
        fun fromStringOrNull(value: String?): SortOrder? =
            entries.find { it.name.equals(value, ignoreCase = true) }
    }
}

enum class SortBy {
    AVG_PRICE, NAME;

    companion object {
        fun fromStringOrNull(value: String?): SortBy? =
            entries.find { it.name.equals(value, ignoreCase = true) }
    }
}