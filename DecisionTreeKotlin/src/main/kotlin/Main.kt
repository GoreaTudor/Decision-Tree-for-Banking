package org.example

import data.loadCSV
import org.example.data.BankMarketing
import org.example.tree.DecisionTree
import org.example.tree.Node


fun main() {
    val (training, testing) = loadCSV(".\\src\\main\\resources\\bank.csv")

    val decisionTree = DecisionTree(training) { it.y }
    val tree = decisionTree.buildTree()

    test(decisionTree, tree, testing, debug = true)
}

fun test(
    decisionTree: DecisionTree,
    tree: Node,
    data: List<BankMarketing>,
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
            countCorrect ++
        }
    }

    println("\n\n>> Tested: ${data.size}")
    println(">> Correct: $countCorrect")
    println(">> Wrong: ${data.size - countCorrect}")
    println("\n>> Percentage: ${countCorrect.toDouble() / data.size.toDouble() * 100}%")
}