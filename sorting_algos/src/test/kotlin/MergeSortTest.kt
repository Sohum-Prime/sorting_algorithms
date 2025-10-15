package org.example

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.random.Random

class MergeSortTest {

    @Test
    fun sortEmptyList() {
        val result = MergeSort.sort(emptyList<Int>())
        assertTrue(result.isEmpty())
    }

    @Test
    fun sortSingleElement() {
        val result = MergeSort.sort(listOf(42))
        assertEquals(listOf(42), result)
    }

    @Test
    fun sortTwoElements() {
        assertEquals(listOf(1, 2), MergeSort.sort(listOf(2, 1)))
        assertEquals(listOf(1, 2), MergeSort.sort(listOf(1, 2)))
    }

    @Test
    fun sortAlreadySorted() {
        val sorted = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val result = MergeSort.sort(sorted)
        assertEquals(sorted, result)
    }

    @Test
    fun sortReverseSorted() {
        val reversed = listOf(10, 9, 8, 7, 6, 5, 4, 3, 2, 1)
        val expected = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val result = MergeSort.sort(reversed)
        assertEquals(expected, result)
    }

    @Test
    fun sortRandomSmall() {
        val random = listOf(5, 2, 8, 1, 9, 3, 7, 4, 6)
        val expected = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
        val result = MergeSort.sort(random)
        assertEquals(expected, result)
    }

    @Test
    fun sortWithDuplicates() {
        val withDupes = listOf(5, 2, 8, 2, 9, 5, 7, 2, 6)
        val expected = listOf(2, 2, 2, 5, 5, 6, 7, 8, 9)
        val result = MergeSort.sort(withDupes)
        assertEquals(expected, result)
    }

    @Test
    fun sortAllSameElements() {
        val allSame = List(10) { 5 }
        val result = MergeSort.sort(allSame)
        assertEquals(allSame, result)
    }

    @Test
    fun sortNegativeNumbers() {
        val withNegatives = listOf(-5, 3, -1, 7, -9, 0, 2, -3)
        val expected = listOf(-9, -5, -3, -1, 0, 2, 3, 7)
        val result = MergeSort.sort(withNegatives)
        assertEquals(expected, result)
    }

    @Test
    fun sortStrings() {
        val strings = listOf("dog", "cat", "zebra", "ant", "bear")
        val expected = listOf("ant", "bear", "cat", "dog", "zebra")
        val result = MergeSort.sort(strings)
        assertEquals(expected, result)
    }

    @Test
    fun sortLargeRandomList() {
        val size = 1000
        val random = List(size) { Random.nextInt(1000) }
        val expected = random.sorted()
        val result = MergeSort.sort(random)
        assertEquals(expected, result)
    }

    @Test
    fun verifyStability() {
        data class Item(val key: Int, val id: Int) : Comparable<Item> {
            override fun compareTo(other: Item) = key.compareTo(other.key)
        }

        val items = listOf(
            Item(3, 1),
            Item(1, 2),
            Item(3, 3),
            Item(2, 4),
            Item(1, 5)
        )

        val expected = listOf(
            Item(1, 2),
            Item(1, 5),
            Item(2, 4),
            Item(3, 1),
            Item(3, 3)
        )

        val result = MergeSort.sort(items)
        assertEquals(expected, result)
        println("✓ Merge Sort is stable")
    }

    @Test
    fun sortDoesNotModifyInput() {
        val original = listOf(5, 2, 8, 1, 9)
        val originalCopy = original.toList()
        MergeSort.sort(original)
        assertEquals(originalCopy, original)
    }

    @Test
    fun sortNearlySorted() {
        val nearlySorted = listOf(1, 2, 3, 7, 5, 6, 4, 8, 9, 10)
        val expected = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val result = MergeSort.sort(nearlySorted)
        assertEquals(expected, result)
    }
}