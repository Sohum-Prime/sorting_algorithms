package org.example

/**
 * Merge Sort - O(n log n) all cases
 *
 * Recursively divides the array in half, sorts each half, then merges
 * the sorted halves back together.
 *
 * Complexity Analysis:
 * - Time: O(n log n) best, average, and worst case
 *         - log n levels of recursion (dividing by 2 each time)
 *         - O(n) work per level (merging)
 * - Space: O(n) for temporary arrays during merging
 *
 * Characteristics:
 * - Stable: Yes (equal elements maintain relative order during merge)
 * - Adaptive: No (always performs same operations)
 * - In-place: No (requires O(n) auxiliary space)
 *
 * Best for:
 * - Large datasets
 * - When stability is required
 * - Linked lists (can be done with O(1) space)
 * - When worst-case O(n log n) is needed
 */
object MergeSort {

    /**
     * Sorts a list using merge sort algorithm.
     *
     * @param list the list to sort
     * @return a new sorted list in ascending order
     */
    fun <T : Comparable<T>> sort(list: List<T>): List<T> {
        // Base case: lists of size 0 or 1 are already sorted
        if (list.size <= 1) return list.toList()

        // Divide: split the list in half
        val mid = list.size / 2
        val left = list.subList(0, mid)
        val right = list.subList(mid, list.size)

        // Conquer: recursively sort both halves
        val sortedLeft = sort(left)
        val sortedRight = sort(right)

        // Combine: merge the sorted halves
        return merge(sortedLeft, sortedRight)
    }

    /**
     * Merges two sorted lists into a single sorted list.
     * Helper function for merge sort.
     *
     * @param left sorted list
     * @param right sorted list
     * @return merged sorted list
     */
    private fun <T : Comparable<T>> merge(left: List<T>, right: List<T>): List<T> {
        val result = mutableListOf<T>()
        var i = 0  // Index for left list
        var j = 0  // Index for right list

        // Compare elements from both lists and add smaller one
        while (i < left.size && j < right.size) {
            if (left[i] <= right[j]) {
                result.add(left[i])
                i++
            } else {
                result.add(right[j])
                j++
            }
        }

        // Add remaining elements from left list (if any)
        while (i < left.size) {
            result.add(left[i])
            i++
        }

        // Add remaining elements from right list (if any)
        while (j < right.size) {
            result.add(right[j])
            j++
        }

        return result
    }
}