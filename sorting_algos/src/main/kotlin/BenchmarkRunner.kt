package org.example

import java.io.File

/**
 * Simple executable to run sorting algorithm benchmarks.
 *
 * This is a standalone program that can be run directly to benchmark
 * all sorting algorithms and generate reports.
 *
 * Usage:
 *   1. Run as main program
 *   2. Check console output for results
 *   3. Find exported CSV and Markdown files in project directory
 */
fun main() {
    println("╔════════════════════════════════════════════════════════════╗")
    println("║       SORTING ALGORITHM BENCHMARK SUITE                   ║")
    println("╚════════════════════════════════════════════════════════════╝")
    println()

    println("This benchmark will test 5 sorting algorithms:")
    println("  • Insertion Sort (O(n²))")
    println("  • Selection Sort (O(n²))")
    println("  • Merge Sort (O(n log n))")
    println("  • Quick Sort (O(n log n) average)")
    println("  • Heap Sort (O(n log n))")
    println()

    println("Input types tested:")
    println("  • Random data")
    println("  • Already sorted")
    println("  • Reverse sorted")
    println("  • Nearly sorted (90% sorted)")
    println()

    // Configuration
    val quickMode = false  // Set to true for faster testing

    val sizes = if (quickMode) {
        listOf(100, 1_000, 5_000)
    } else {
        listOf(10, 100, 1_000, 10_000, 50_000)
    }

    val trials = if (quickMode) 1 else 3

    println("Configuration:")
    println("  • Input sizes: ${sizes.joinToString(", ")}")
    println("  • Trials per test: $trials")
    println("  • Mode: ${if (quickMode) "QUICK (for testing)" else "FULL (accurate results)"}")
    println()

    println("Starting benchmark...")
    println("This may take a few minutes depending on your hardware.")
    println("─".repeat(60))
    println()

    // Run the benchmark
    val results = SortingBenchmark.runComprehensiveBenchmark(
        sizes = sizes,
        trials = trials
    )

    println()
    println("─".repeat(60))
    println("Benchmark complete!")
    println()

    // Display results
    SortingBenchmark.printResultsTable(results)
    SortingBenchmark.printAnalysis(results)

    // Export results
    println("\n" + "═".repeat(60))
    println("EXPORTING RESULTS")
    println("═".repeat(60) + "\n")

    try {
        // Export CSV
        val csv = SortingBenchmark.exportToCSV(results)
        val csvFile = File("sorting_benchmark_results.csv")
        csvFile.writeText(csv)
        println("✓ CSV exported to: ${csvFile.absolutePath}")

        // Export Markdown
        val markdown = SortingBenchmark.exportToMarkdown(results)
        val mdFile = File("BENCHMARK_RESULTS.md")
        mdFile.writeText(markdown)
        println("✓ Markdown exported to: ${mdFile.absolutePath}")

        // Create a summary report
        val summary = generateSummaryReport(results)
        val summaryFile = File("BENCHMARK_SUMMARY.txt")
        summaryFile.writeText(summary)
        println("✓ Summary exported to: ${summaryFile.absolutePath}")

    } catch (e: Exception) {
        println("⚠ Warning: Could not export files: ${e.message}")
        println("  Results are still displayed in console output.")
    }

    println()
    println("═".repeat(60))
    println("RECOMMENDATIONS")
    println("═".repeat(60) + "\n")

    printRecommendations(results)

    println()
    println("╔════════════════════════════════════════════════════════════╗")
    println("║                  BENCHMARK COMPLETE                       ║")
    println("╚════════════════════════════════════════════════════════════╝")
}

/**
 * Generates a text summary report of the benchmark results.
 */
private fun generateSummaryReport(results: List<SortingBenchmark.BenchmarkResult>): String {
    val report = StringBuilder()

    report.appendLine("SORTING ALGORITHM BENCHMARK SUMMARY")
    report.appendLine("=" .repeat(60))
    report.appendLine()
    report.appendLine("Generated: ${java.time.LocalDateTime.now()}")
    report.appendLine()

    // Overall winner
    val randomResults = results.filter { it.inputType == "Random" && it.inputSize >= 1000 }
    if (randomResults.isNotEmpty()) {
        val grouped = randomResults.groupBy { it.algorithmName }
        val avgTimes = grouped.mapValues { (_, list) ->
            list.map { it.timeSeconds }.average()
        }
        val winner = avgTimes.minByOrNull { it.value }

        report.appendLine("OVERALL WINNER (Random Data, Large Inputs):")
        report.appendLine("  ${winner?.key} - Average time: ${"%.6f".format(winner?.value)}s")
        report.appendLine()
    }

    // Best for sorted data
    val sortedResults = results.filter { it.inputType == "Sorted" && it.inputSize >= 1000 }
    if (sortedResults.isNotEmpty()) {
        val best = sortedResults.minByOrNull { it.timeSeconds }
        report.appendLine("BEST FOR ALREADY SORTED DATA:")
        report.appendLine("  ${best?.algorithmName} - ${"%.6f".format(best?.timeSeconds)}s on size ${best?.inputSize}")
        report.appendLine()
    }

    // Summary statistics
    report.appendLine("STATISTICS:")
    report.appendLine("-".repeat(60))

    for (algo in results.map { it.algorithmName }.distinct().sorted()) {
        val algoResults = results.filter { it.algorithmName == algo }
        val avgTime = algoResults.map { it.timeSeconds }.average()
        val minTime = algoResults.minOf { it.timeSeconds }
        val maxTime = algoResults.maxOf { it.timeSeconds }

        report.appendLine("$algo:")
        report.appendLine("  Average: ${"%.6f".format(avgTime)}s")
        report.appendLine("  Min: ${"%.6f".format(minTime)}s")
        report.appendLine("  Max: ${"%.6f".format(maxTime)}s")
        report.appendLine()
    }

    return report.toString()
}

/**
 * Prints practical recommendations based on benchmark results.
 */
private fun printRecommendations(results: List<SortingBenchmark.BenchmarkResult>) {
    println("Based on your benchmark results:\n")

    // Find fastest on random data
    val randomResults = results.filter { it.inputType == "Random" && it.inputSize >= 1000 }
    if (randomResults.isNotEmpty()) {
        val fastest = randomResults.groupBy { it.algorithmName }
            .mapValues { (_, list) -> list.map { it.timeSeconds }.average() }
            .minByOrNull { it.value }

        println("For general-purpose sorting:")
        println("  → Use ${fastest?.key}")
        println("    (Fastest on random data)")
        println()
    }

    // Check for adaptive behavior
    val insertionRandom = results.find {
        it.algorithmName == "Insertion Sort" && it.inputType == "Random" && it.inputSize >= 1000
    }
    val insertionSorted = results.find {
        it.algorithmName == "Insertion Sort" && it.inputType == "Sorted" && it.inputSize >= 1000
    }

    if (insertionRandom != null && insertionSorted != null) {
        val speedup = insertionRandom.timeSeconds / insertionSorted.timeSeconds
        if (speedup > 2.0) {
            println("For nearly sorted data:")
            println("  → Use Insertion Sort")
            println("    (${speedup.toInt()}x faster on sorted data!)")
            println()
        }
    }

    println("For production systems:")
    println("  → Consider hybrid algorithms (Timsort, Introsort)")
    println("    These combine strengths of multiple algorithms")
    println()

    println("For memory-constrained systems:")
    println("  → Use Heap Sort or in-place Quick Sort")
    println("    Both use O(1) or O(log n) extra space")
    println()

    println("When stability matters:")
    println("  → Use Merge Sort or Insertion Sort")
    println("    These preserve relative order of equal elements")
}