package org.example

/**
 * Selection Sort - O(n²) all cases
 *
 * Repeatedly finds the minimum element from the unsorted portion and
 * places it at the beginning of the sorted portion.
 *
 * Complexity Analysis:
 * - Time: O(n²) best, average, and worst case
 *         (Always performs n² comparisons regardless of input)
 * - Space: O(n) for the output array (O(1) auxiliary space)
 *
 * Characteristics:
 * - Stable: No (this implementation may swap equal elements)
 * - Adaptive: No (always makes same number of comparisons)
 * - In-place: Can be implemented in-place
 *
 * Best for:
 * - Small datasets
 * - When memory writes are expensive (makes minimum number of swaps)
 * - When simplicity is valued over performance
 */
object SelectionSort {

    /**
     * Sorts a list using selection sort algorithm.
     *
     * @param list the list to sort
     * @return a new sorted list in ascending order
     */
    fun <T : Comparable<T>> sort(list: List<T>): List<T> {
        if (list.size <= 1) return list.toList()

        val result = list.toMutableList()

        // For each position, find the minimum from remaining elements
        for (i in 0 until result.size - 1) {
            var minIndex = i

            // Find the index of minimum element in unsorted portion
            for (j in i + 1 until result.size) {
                if (result[j] < result[minIndex]) {
                    minIndex = j
                }
            }

            // Swap minimum element with current position
            if (minIndex != i) {
                val temp = result[i]
                result[i] = result[minIndex]
                result[minIndex] = temp
            }
        }

        return result
    }
}