package org.example

/**
 * Quick Sort - O(n log n) average case, O(n²) worst case
 *
 * Picks a pivot element, partitions the array so elements less than
 * the pivot are on the left and greater are on the right, then
 * recursively sorts both partitions.
 *
 * Complexity Analysis:
 * - Time: O(n log n) average case (with good pivot selection)
 *         O(n²) worst case (when pivot is always min/max)
 * - Space: O(log n) for recursion stack (average case)
 *          O(n) worst case recursion depth
 *
 * Characteristics:
 * - Stable: No (partitioning may change relative order)
 * - Adaptive: No (though some variants can be)
 * - In-place: Can be implemented in-place
 *
 * Best for:
 * - Large datasets (often fastest in practice)
 * - When average-case performance is more important than worst-case
 * - When O(1) auxiliary space is needed (in-place version)
 *
 * Pivot Selection Strategy:
 * This implementation uses "median-of-three" to choose a good pivot,
 * which significantly reduces the chance of worst-case behavior.
 */
object QuickSort {

    /**
     * Sorts a list using quick sort algorithm.
     *
     * @param list the list to sort
     * @return a new sorted list in ascending order
     */
    fun <T : Comparable<T>> sort(list: List<T>): List<T> {
        if (list.size <= 1) return list.toList()
        return quickSortHelper(list)
    }

    /**
     * Recursive helper for quick sort.
     */
    private fun <T : Comparable<T>> quickSortHelper(list: List<T>): List<T> {
        if (list.size <= 1) return list

        // Choose pivot using median-of-three strategy for better performance
        val pivot = medianOfThree(list)

        // Partition: elements < pivot, == pivot, > pivot
        val less = mutableListOf<T>()
        val equal = mutableListOf<T>()
        val greater = mutableListOf<T>()

        for (element in list) {
            when {
                element < pivot -> less.add(element)
                element > pivot -> greater.add(element)
                else -> equal.add(element)
            }
        }

        // Recursively sort partitions and combine
        return quickSortHelper(less) + equal + quickSortHelper(greater)
    }

    /**
     * Selects pivot using median-of-three strategy.
     * Takes the median of first, middle, and last elements.
     * This helps avoid worst-case behavior on sorted or reverse-sorted input.
     */
    private fun <T : Comparable<T>> medianOfThree(list: List<T>): T {
        val first = list.first()
        val middle = list[list.size / 2]
        val last = list.last()

        return when {
            first <= middle && middle <= last -> middle
            first <= last && last <= middle -> last
            middle <= first && first <= last -> first
            middle <= last && last <= first -> last
            last <= first && first <= middle -> first
            else -> middle
        }
    }
}