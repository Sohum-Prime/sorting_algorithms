package org.example

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SortingBenchmarkTest {

    @Test
    fun benchmarkRunsWithoutErrors() {
        // Run a quick benchmark with small sizes
        val results = SortingBenchmark.runComprehensiveBenchmark(
            sizes = listOf(10, 100),
            trials = 1
        )

        // Should have results for multiple algorithms and input types
        assertTrue(results.isNotEmpty())

        // Check we have results for both sizes
        assertTrue(results.any { it.inputSize == 10 })
        assertTrue(results.any { it.inputSize == 100 })

        // Check we have different input types
        val inputTypes = results.map { it.inputType }.distinct()
        assertTrue(inputTypes.contains("Random"))
        assertTrue(inputTypes.contains("Sorted"))
        assertTrue(inputTypes.contains("Reverse"))
        assertTrue(inputTypes.contains("Nearly Sorted"))
    }

    @Test
    fun allTimesArePositive() {
        val results = SortingBenchmark.runComprehensiveBenchmark(
            sizes = listOf(50),
            trials = 1
        )

        for (result in results) {
            assertTrue(result.timeSeconds >= 0.0,
                "${result.algorithmName} had negative time: ${result.timeSeconds}")
        }
    }

    @Test
    fun largerInputsTakeLonger() {
        val results = SortingBenchmark.runComprehensiveBenchmark(
            sizes = listOf(100, 1000),
            trials = 1
        )

        // Group by algorithm and input type
        val grouped = results.groupBy { it.algorithmName to it.inputType }

        for ((key, group) in grouped) {
            if (group.size >= 2) {
                val sorted = group.sortedBy { it.inputSize }
                val smaller = sorted[0]
                val larger = sorted[1]

                // Larger inputs should generally take longer
                // (with some tolerance for measurement variation)
                assertTrue(
                    larger.timeSeconds >= smaller.timeSeconds * 0.5,
                    "${key.first} on ${key.second}: size ${larger.inputSize} " +
                            "should take longer than size ${smaller.inputSize}"
                )
            }
        }
    }

    @Test
    fun csvExportWorks() {
        val results = SortingBenchmark.runComprehensiveBenchmark(
            sizes = listOf(10),
            trials = 1
        )

        val csv = SortingBenchmark.exportToCSV(results)

        // Should have header
        assertTrue(csv.startsWith("Algorithm,Size,InputType,TimeSeconds"))

        // Should have data rows
        val lines = csv.split("\n").filter { it.isNotBlank() }
        assertTrue(lines.size > 1, "CSV should have header + data rows")

        // Each line should have 4 columns
        for (line in lines.drop(1)) {
            val columns = line.split(",")
            assertEquals(4, columns.size, "Each CSV row should have 4 columns")
        }
    }

    @Test
    fun markdownExportWorks() {
        val results = SortingBenchmark.runComprehensiveBenchmark(
            sizes = listOf(10),
            trials = 1
        )

        val markdown = SortingBenchmark.exportToMarkdown(results)

        // Should have title
        assertTrue(markdown.contains("# Sorting Algorithm Benchmark Results"))

        // Should have table with pipes
        assertTrue(markdown.contains("|"))

        // Should have size heading
        assertTrue(markdown.contains("## Size:"))
    }

    @Test
    fun insertionSortFasterOnSortedData() {
        val results = SortingBenchmark.runComprehensiveBenchmark(
            sizes = listOf(1000),
            trials = 2
        )

        // Get Insertion Sort results
        val insertionRandom = results.find {
            it.algorithmName == "Insertion Sort" && it.inputType == "Random"
        }
        val insertionSorted = results.find {
            it.algorithmName == "Insertion Sort" && it.inputType == "Sorted"
        }

        assertNotNull(insertionRandom)
        assertNotNull(insertionSorted)

        // Insertion Sort should be much faster on sorted data (it's adaptive!)
        assertTrue(
            insertionSorted!!.timeSeconds < insertionRandom!!.timeSeconds * 0.5,
            "Insertion Sort should be much faster on sorted data due to adaptivity. " +
                    "Random: ${insertionRandom.timeSeconds}s, Sorted: ${insertionSorted.timeSeconds}s"
        )

        println("✓ Insertion Sort is adaptive!")
        println("  Random data: ${insertionRandom.timeSeconds}s")
        println("  Sorted data: ${insertionSorted.timeSeconds}s")
    }

    @Test
    fun nLogNFasterThanN2OnLargeData() {
        val results = SortingBenchmark.runComprehensiveBenchmark(
            sizes = listOf(5000),
            trials = 1
        )

        // Compare O(n²) vs O(n log n) on random data
        val insertionRandom = results.find {
            it.algorithmName == "Insertion Sort" && it.inputType == "Random"
        }
        val mergeSortRandom = results.find {
            it.algorithmName == "Merge Sort" && it.inputType == "Random"
        }

        assertNotNull(insertionRandom)
        assertNotNull(mergeSortRandom)

        // O(n log n) should be significantly faster
        assertTrue(
            mergeSortRandom!!.timeSeconds < insertionRandom!!.timeSeconds,
            "Merge Sort (O(n log n)) should be faster than Insertion Sort (O(n²)) on large random data. " +
                    "Merge: ${mergeSortRandom.timeSeconds}s, Insertion: ${insertionRandom.timeSeconds}s"
        )

        println("✓ O(n log n) algorithms faster on large data!")
        println("  Insertion Sort (O(n²)): ${insertionRandom.timeSeconds}s")
        println("  Merge Sort (O(n log n)): ${mergeSortRandom.timeSeconds}s")
        println("  Speedup: ${insertionRandom.timeSeconds / mergeSortRandom.timeSeconds}x")
    }

    @Test
    fun demonstrateBenchmarkUsage() {
        println("\n=== Mini Benchmark Demo ===\n")

        // Run a small benchmark
        val results = SortingBenchmark.runComprehensiveBenchmark(
            sizes = listOf(100, 1000),
            trials = 2
        )

        // Print results
        SortingBenchmark.printResultsTable(results)
        SortingBenchmark.printAnalysis(results)

        println("\n✓ Benchmark completed successfully!")
        assertTrue(true) // Always passes, just demonstrates usage
    }

    @Test
    fun verifySkippingLogic() {
        // This test verifies that slow algorithms are skipped for large inputs
        val results = SortingBenchmark.runComprehensiveBenchmark(
            sizes = listOf(100_000),
            trials = 1
        )

        // Should not have Insertion or Selection Sort for size 100,000
        val hasInsertionSort = results.any {
            it.algorithmName == "Insertion Sort" && it.inputSize == 100_000
        }
        val hasSelectionSort = results.any {
            it.algorithmName == "Selection Sort" && it.inputSize == 100_000
        }

        assertFalse(hasInsertionSort, "Insertion Sort should be skipped for size 100,000")
        assertFalse(hasSelectionSort, "Selection Sort should be skipped for size 100,000")

        // Should still have the fast algorithms
        assertTrue(results.any { it.algorithmName == "Merge Sort" && it.inputSize == 100_000 })

        println("✓ Skipping logic works - O(n²) algorithms skipped for large inputs")
    }
}