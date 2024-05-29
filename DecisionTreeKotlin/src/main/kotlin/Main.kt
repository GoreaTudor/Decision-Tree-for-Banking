package org.example

import data.loadCSV
import org.example.data.BankMarketing
import org.example.tree.DecisionTree
import org.example.tree.Node
import kotlin.time.DurationUnit
import kotlin.time.measureTime


fun main() {
    run(20)
}


fun run(times: Int = 1) {
    var avgPercentage: Double = 0.0
    var avgTime: Double = 0.0

    for (i in 1..times) {
        val (training, testing) = loadCSV(".\\src\\main\\resources\\bank.csv", debug = false)

        var decisionTree: DecisionTree?
        var tree: Node?

        val duration = measureTime {
            decisionTree = DecisionTree(training) { it.y }
            tree = decisionTree!!.buildTree()
        }

        val correct = test(decisionTree!!, tree!!, testing, debug = false)
        val percentage = correct.toDouble() / testing.size.toDouble() * 100.0

        avgPercentage += percentage
        avgTime += duration.toDouble(DurationUnit.MILLISECONDS)

        // Run info
        println("$i. p: $percentage%, t: $duration")
    }

    avgPercentage /= times
    avgTime /= times

    // Result info
    println("\n>> Avg. Percentage: $avgPercentage%")
    println(">> Avg. Time: ${avgTime}ms")
}


fun test(
    decisionTree: DecisionTree,
    tree: Node,
    data: List<BankMarketing>,
    debug: Boolean = true
): Int {
    var countCorrect = 0

    data.forEach {
        val prediction = decisionTree.predict(tree, it)

        if (debug) {
            println("\nItem: $it")
            println("Target: ${it.y}")
            println("Prediction: $prediction")
        }

        if (prediction == it.y) {
            countCorrect++
        }
    }

    return countCorrect
}