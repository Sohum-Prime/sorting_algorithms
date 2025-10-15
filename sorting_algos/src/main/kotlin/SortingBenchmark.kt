package org.example

import kotlin.time.measureTime
import kotlin.time.DurationUnit
import kotlin.random.Random

/**
 * Comprehensive benchmarking suite for sorting algorithms.
 *
 * Tests performance across various input sizes and types:
 * - Random data
 * - Already sorted data
 * - Reverse sorted data
 * - Nearly sorted data
 */
object SortingBenchmark {

    data class BenchmarkResult(
        val algorithmName: String,
        val inputSize: Int,
        val inputType: String,
        val timeSeconds: Double,
        val verificationsPerformed: Int = 3
    )

    // Define all algorithms with their sorting functions
    private val algorithms = listOf(
        "Insertion Sort" to { list: List<Int> -> InsertionSort.sort(list) },
        "Selection Sort" to { list: List<Int> -> SelectionSort.sort(list) },
        "Merge Sort" to { list: List<Int> -> MergeSort.sort(list) },
        "Quick Sort" to { list: List<Int> -> QuickSort.sort(list) },
        "Heap Sort" to { list: List<Int> -> HeapSort.sort(list) }
    )

    /**
     * Runs comprehensive benchmarks across all algorithms and input types.
     *
     * @param sizes list of input sizes to test
     * @param trials number of trials to run for each configuration (uses median time)
     * @return list of all benchmark results
     */
    fun runComprehensiveBenchmark(
        sizes: List<Int> = listOf(10, 100, 1_000, 10_000, 50_000),
        trials: Int = 3
    ): List<BenchmarkResult> {
        val results = mutableListOf<BenchmarkResult>()

        println("=== Sorting Algorithm Benchmark ===")
        println("Running ${algorithms.size} algorithms on ${sizes.size} input sizes")
        println("Each test performed $trials times (using median)\n")

        for (size in sizes) {
            println("Testing size: $size")

            // Test on random data
            results.addAll(benchmarkInputType(size, "Random", trials) {
                generateRandomList(it)
            })

            // Test on sorted data
            results.addAll(benchmarkInputType(size, "Sorted", trials) {
                generateSortedList(it)
            })

            // Test on reverse sorted data
            results.addAll(benchmarkInputType(size, "Reverse", trials) {
                generateReverseSortedList(it)
            })

            // Test on nearly sorted data
            results.addAll(benchmarkInputType(size, "Nearly Sorted", trials) {
                generateNearlySortedList(it)
            })

            println()
        }

        return results
    }

    /**
     * Benchmarks all algorithms on a specific input type.
     */
    private fun benchmarkInputType(
        size: Int,
        inputType: String,
        trials: Int,
        generator: (Int) -> List<Int>
    ): List<BenchmarkResult> {
        val results = mutableListOf<BenchmarkResult>()

        for ((name, sortFunction) in algorithms) {
            // Skip slow O(n²) algorithms on very large inputs
            if (shouldSkip(name, size)) {
                println("  $name on $inputType: SKIPPED (too slow for size $size)")
                continue
            }

            val times = mutableListOf<Double>()

            // Run multiple trials
            for (trial in 1..trials) {
                val input = generator(size)

                val time = measureTime {
                    sortFunction(input)
                }.toDouble(DurationUnit.SECONDS)

                times.add(time)
            }

            // Use median time to reduce impact of outliers
            val medianTime = times.sorted()[times.size / 2]

            results.add(BenchmarkResult(name, size, inputType, medianTime, trials))

            println("  $name on $inputType: ${formatTime(medianTime)}")
        }

        return results
    }

    /**
     * Determines if an algorithm should be skipped for a given size.
     * O(n²) algorithms are too slow for large inputs.
     */
    private fun shouldSkip(algorithmName: String, size: Int): Boolean {
        val slowAlgorithms = setOf("Insertion Sort", "Selection Sort")
        return algorithmName in slowAlgorithms && size > 50_000
    }

    /**
     * Formats time in human-readable format.
     */
    private fun formatTime(seconds: Double): String {
        return when {
            seconds < 0.001 -> "${(seconds * 1_000_000).toInt()} µs"
            seconds < 1.0 -> "${(seconds * 1_000).toInt()} ms"
            else -> "${"%.3f".format(seconds)} s"
        }
    }

    // === Data Generation Functions ===

    /**
     * Generates a list of random integers.
     */
    private fun generateRandomList(size: Int): List<Int> {
        return List(size) { Random.nextInt(size * 10) }
    }

    /**
     * Generates an already sorted list.
     */
    private fun generateSortedList(size: Int): List<Int> {
        return List(size) { it }
    }

    /**
     * Generates a reverse-sorted list.
     */
    private fun generateReverseSortedList(size: Int): List<Int> {
        return List(size) { size - it }
    }

    /**
     * Generates a nearly sorted list (90% sorted, 10% randomly placed).
     */
    private fun generateNearlySortedList(size: Int): List<Int> {
        val sorted = List(size) { it }.toMutableList()

        // Randomly swap ~10% of elements
        val swaps = size / 10
        repeat(swaps) {
            val i = Random.nextInt(size)
            val j = Random.nextInt(size)
            val temp = sorted[i]
            sorted[i] = sorted[j]
            sorted[j] = temp
        }

        return sorted
    }

    // === Result Analysis Functions ===

    /**
     * Prints a formatted table of benchmark results.
     */
    fun printResultsTable(results: List<BenchmarkResult>) {
        println("\n=== Benchmark Results Table ===\n")

        // Group by input type and size
        val grouped = results.groupBy { it.inputType to it.inputSize }

        for ((key, group) in grouped.toSortedMap(compareBy({ it.first }, { it.second }))) {
            val (inputType, size) = key
            println("$inputType data, size $size:")
            println("-".repeat(60))

            for (result in group.sortedBy { it.timeSeconds }) {
                println("  ${result.algorithmName.padEnd(20)} ${formatTime(result.timeSeconds)}")
            }
            println()
        }
    }

    /**
     * Prints analysis comparing algorithms across different input types.
     */
    fun printAnalysis(results: List<BenchmarkResult>) {
        println("\n=== Performance Analysis ===\n")

        // Find fastest algorithm for each scenario
        val byScenario = results.groupBy { "${it.inputType}-${it.inputSize}" }

        println("Fastest algorithm for each scenario:")
        println("-".repeat(60))

        for ((scenario, group) in byScenario.toSortedMap()) {
            val fastest = group.minByOrNull { it.timeSeconds }!!
            println("  $scenario: ${fastest.algorithmName} (${formatTime(fastest.timeSeconds)})")
        }

        println("\nKey Observations:")
        println("-".repeat(60))

        // Analyze random data performance
        val randomResults = results.filter { it.inputType == "Random" }
        val largeRandomResults = randomResults.filter { it.inputSize >= 10_000 }

        if (largeRandomResults.isNotEmpty()) {
            val avgTimes = largeRandomResults.groupBy { it.algorithmName }
                .mapValues { (_, results) -> results.map { it.timeSeconds }.average() }

            println("\nAverage time on large random inputs (≥10,000):")
            for ((name, avgTime) in avgTimes.toList().sortedBy { it.second }) {
                println("  ${name.padEnd(20)} ${formatTime(avgTime)}")
            }
        }

        // Analyze best-case performance
        val sortedResults = results.filter { it.inputType == "Sorted" && it.inputSize >= 1000 }
        if (sortedResults.isNotEmpty()) {
            println("\nBest-case (already sorted) performance:")
            val sorted = sortedResults.groupBy { it.algorithmName }
                .mapValues { (_, results) -> results.map { it.timeSeconds }.average() }

            for ((name, avgTime) in sorted.toList().sortedBy { it.second }.take(3)) {
                println("  ${name.padEnd(20)} ${formatTime(avgTime)}")
            }
        }

        // Analyze worst-case performance
        val reverseResults = results.filter { it.inputType == "Reverse" && it.inputSize >= 1000 }
        if (reverseResults.isNotEmpty()) {
            println("\nWorst-case (reverse sorted) performance:")
            val reverse = reverseResults.groupBy { it.algorithmName }
                .mapValues { (_, results) -> results.map { it.timeSeconds }.average() }

            for ((name, avgTime) in reverse.toList().sortedBy { it.second }.take(3)) {
                println("  ${name.padEnd(20)} ${formatTime(avgTime)}")
            }
        }
    }

    /**
     * Generates a CSV export of results for further analysis.
     */
    fun exportToCSV(results: List<BenchmarkResult>): String {
        val csv = StringBuilder()
        csv.appendLine("Algorithm,Size,InputType,TimeSeconds")

        for (result in results) {
            csv.appendLine("${result.algorithmName},${result.inputSize},${result.inputType},${result.timeSeconds}")
        }

        return csv.toString()
    }

    /**
     * Generates a Markdown table of results.
     */
    fun exportToMarkdown(results: List<BenchmarkResult>): String {
        val md = StringBuilder()

        md.appendLine("# Sorting Algorithm Benchmark Results")
        md.appendLine()

        // Group by size
        val bySizes = results.groupBy { it.inputSize }

        for ((size, sizeResults) in bySizes.toSortedMap()) {
            md.appendLine("## Size: $size")
            md.appendLine()

            // Create table by input type
            val byInputType = sizeResults.groupBy { it.inputType }

            md.appendLine("| Algorithm | ${byInputType.keys.joinToString(" | ")} |")
            md.appendLine("|-----------|${byInputType.keys.joinToString("|") { "------" }}|")

            // Get all unique algorithms
            val allAlgorithms = sizeResults.map { it.algorithmName }.distinct().sorted()

            for (algo in allAlgorithms) {
                md.append("| $algo |")
                for (inputType in byInputType.keys.sorted()) {
                    val result = byInputType[inputType]?.find { it.algorithmName == algo }
                    md.append(" ${result?.let { formatTime(it.timeSeconds) } ?: "N/A"} |")
                }
                md.appendLine()
            }
            md.appendLine()
        }

        return md.toString()
    }
}