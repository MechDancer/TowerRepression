package org.mechdancer.towerrepression.event

data class FieldEvent(
    val towerData: IntArray, //size 3
    val zoneData: IntArray //size 6
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FieldEvent

        if (!towerData.contentEquals(other.towerData)) return false
        if (!zoneData.contentEquals(other.zoneData)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = towerData.contentHashCode()
        result = 31 * result + zoneData.contentHashCode()
        return result
    }

    companion object {
        fun empty() = FieldEvent(intArrayOf(0, 0, 0), intArrayOf(0, 0, 0, 0, 0, 0))
    }
}