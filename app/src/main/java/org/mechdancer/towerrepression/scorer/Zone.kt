package org.mechdancer.towerrepression.scorer

data class Zone(val content: IntArray = IntArray(3)) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Zone

        if (!content.contentEquals(other.content)) return false

        return true
    }

    override fun hashCode(): Int {
        return content.contentHashCode()
    }
}