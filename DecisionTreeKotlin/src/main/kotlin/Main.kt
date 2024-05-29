package org.example

import data.loadCSV
import org.example.data.BankMarketing
import org.example.tree.DecisionTree
import org.example.tree.Node
import kotlin.system.measureTimeMillis
import kotlin.time.Duration
import kotlin.time.measureTime


fun main() {
    val (training, testing) = loadCSV(".\\src\\main\\resources\\bank.csv")

    var decisionTree: DecisionTree?
    var tree: Node?

    val time = measureTime {
        decisionTree = DecisionTree(training) { it.y }
        tree = decisionTree!!.buildTree()
    }

    test(decisionTree!!, tree!!, testing, time, debug = false)
}


fun test(
    decisionTree: DecisionTree,
    tree: Node,
    data: List<BankMarketing>,
    time: Duration,
    debug: Boolean = true
) {
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

    println("\n\n>> Tested: ${data.size}")
    println(">> Correct: $countCorrect")
    println(">> Wrong: ${data.size - countCorrect}")
    println("\n>> Correct Percentage: ${countCorrect.toDouble() / data.size.toDouble() * 100}%")
    println(">> Time: $time")
}