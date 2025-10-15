package org.example

/**
 * Insertion Sort - O(n²) average/worst case, O(n) best case
 *
 * Builds the sorted array one element at a time by inserting each element
 * into its correct position within the already-sorted portion.
 *
 * Complexity Analysis:
 * - Time: O(n²) average and worst case (nested loops)
 *         O(n) best case (already sorted - inner loop never executes)
 * - Space: O(n) for the output array (O(1) auxiliary space)
 *
 * Characteristics:
 * - Stable: Yes (equal elements maintain their relative order)
 * - Adaptive: Yes (performs well on nearly sorted data)
 * - In-place: Can be implemented in-place (this version creates new array)
 *
 * Best for:
 * - Small datasets (typically n < 50)
 * - Nearly sorted data
 * - Online sorting (elements arrive one at a time)
 */
object InsertionSort {

    /**
     * Sorts a list using insertion sort algorithm.
     *
     * @param list the list to sort
     * @return a new sorted list in ascending order
     */
    fun <T : Comparable<T>> sort(list: List<T>): List<T> {
        if (list.size <= 1) return list.toList()

        val result = list.toMutableList()

        // Start from second element (first element is trivially sorted)
        for (i in 1 until result.size) {
            val key = result[i]
            var j = i - 1

            // Shift elements greater than key one position to the right
            while (j >= 0 && result[j] > key) {
                result[j + 1] = result[j]
                j--
            }

            // Insert key at its correct position
            result[j + 1] = key
        }

        return result
    }
}