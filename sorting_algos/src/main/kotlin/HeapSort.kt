package org.example

/**
 * Heap Sort - O(n log n) all cases
 *
 * Builds a max-heap from the input, then repeatedly extracts the maximum
 * element and places it at the end of the array.
 *
 * Complexity Analysis:
 * - Time: O(n log n) best, average, and worst case
 *         - O(n) to build initial heap
 *         - O(n log n) for n extractions, each taking O(log n)
 * - Space: O(n) for output array (can be done in-place with O(1))
 *
 * Characteristics:
 * - Stable: No (heap operations may change relative order)
 * - Adaptive: No (always performs same operations)
 * - In-place: Can be implemented in-place
 *
 * Best for:
 * - When worst-case O(n log n) is required
 * - When O(1) auxiliary space is needed (in-place version)
 * - Systems with limited memory
 */
object HeapSort {

    /**
     * Sorts a list using heap sort algorithm.
     *
     * @param list the list to sort
     * @return a new sorted list in ascending order
     */
    fun <T : Comparable<T>> sort(list: List<T>): List<T> {
        if (list.size <= 1) return list.toList()

        val heap = list.toMutableList()
        val n = heap.size

        // Build max-heap: start from last non-leaf node and heapify down
        for (i in n / 2 - 1 downTo 0) {
            heapify(heap, n, i)
        }

        // Extract elements from heap one by one
        for (i in n - 1 downTo 1) {
            // Move current root (maximum) to end
            val temp = heap[0]
            heap[0] = heap[i]
            heap[i] = temp

            // Heapify the reduced heap
            heapify(heap, i, 0)
        }

        return heap
    }

    /**
     * Maintains the max-heap property for a subtree rooted at index i.
     *
     * @param heap the array representation of the heap
     * @param heapSize size of the heap portion
     * @param i root index of subtree to heapify
     */
    private fun <T : Comparable<T>> heapify(heap: MutableList<T>, heapSize: Int, i: Int) {
        var largest = i
        val left = 2 * i + 1   // Left child index
        val right = 2 * i + 2  // Right child index

        // If left child exists and is larger than root
        if (left < heapSize && heap[left] > heap[largest]) {
            largest = left
        }

        // If right child exists and is larger than current largest
        if (right < heapSize && heap[right] > heap[largest]) {
            largest = right
        }

        // If largest is not root, swap and recursively heapify
        if (largest != i) {
            val temp = heap[i]
            heap[i] = heap[largest]
            heap[largest] = temp

            heapify(heap, heapSize, largest)
        }
    }
}