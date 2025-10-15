package org.example

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.random.Random

class QuickSortTest {

    @Test
    fun sortEmptyList() {
        val result = QuickSort.sort(emptyList<Int>())
        assertTrue(result.isEmpty())
    }

    @Test
    fun sortSingleElement() {
        val result = QuickSort.sort(listOf(42))
        assertEquals(listOf(42), result)
    }

    @Test
    fun sortTwoElements() {
        assertEquals(listOf(1, 2), QuickSort.sort(listOf(2, 1)))
        assertEquals(listOf(1, 2), QuickSort.sort(listOf(1, 2)))
    }

    @Test
    fun sortAlreadySorted() {
        val sorted = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val result = QuickSort.sort(sorted)
        assertEquals(sorted, result)
    }

    @Test
    fun sortReverseSorted() {
        val reversed = listOf(10, 9, 8, 7, 6, 5, 4, 3, 2, 1)
        val expected = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val result = QuickSort.sort(reversed)
        assertEquals(expected, result)
    }

    @Test
    fun sortRandomSmall() {
        val random = listOf(5, 2, 8, 1, 9, 3, 7, 4, 6)
        val expected = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
        val result = QuickSort.sort(random)
        assertEquals(expected, result)
    }

    @Test
    fun sortWithDuplicates() {
        val withDupes = listOf(5, 2, 8, 2, 9, 5, 7, 2, 6)
        val expected = listOf(2, 2, 2, 5, 5, 6, 7, 8, 9)
        val result = QuickSort.sort(withDupes)
        assertEquals(expected, result)
    }

    @Test
    fun sortAllSameElements() {
        val allSame = List(10) { 5 }
        val result = QuickSort.sort(allSame)
        assertEquals(allSame, result)
    }

    @Test
    fun sortNegativeNumbers() {
        val withNegatives = listOf(-5, 3, -1, 7, -9, 0, 2, -3)
        val expected = listOf(-9, -5, -3, -1, 0, 2, 3, 7)
        val result = QuickSort.sort(withNegatives)
        assertEquals(expected, result)
    }

    @Test
    fun sortStrings() {
        val strings = listOf("dog", "cat", "zebra", "ant", "bear")
        val expected = listOf("ant", "bear", "cat", "dog", "zebra")
        val result = QuickSort.sort(strings)
        assertEquals(expected, result)
    }

    @Test
    fun sortLargeRandomList() {
        val size = 1000
        val random = List(size) { Random.nextInt(1000) }
        val expected = random.sorted()
        val result = QuickSort.sort(random)
        assertEquals(expected, result)
    }

    @Test
    fun sortDoesNotModifyInput() {
        val original = listOf(5, 2, 8, 1, 9)
        val originalCopy = original.toList()
        QuickSort.sort(original)
        assertEquals(originalCopy, original)
    }

    @Test
    fun sortNearlySorted() {
        val nearlySorted = listOf(1, 2, 3, 7, 5, 6, 4, 8, 9, 10)
        val expected = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val result = QuickSort.sort(nearlySorted)
        assertEquals(expected, result)
    }
}