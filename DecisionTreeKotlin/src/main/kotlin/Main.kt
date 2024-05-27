package org.example

import data.loadCSV
import org.example.data.BankMarketing
import org.example.tree.DecisionTree
import org.example.tree.Node


fun main() {
    val (training, testing) = loadCSV(".\\src\\main\\resources\\bank.csv")

    val decisionTree = DecisionTree(training) { it.y }
    val tree = decisionTree.buildTree()

    println(training[10])

    test(tree, decisionTree, testing, debug = true)
}

fun test(
    tree: Node,
    decisionTree: DecisionTree,
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

    println("Result: ${countCorrect.toDouble() / data.size.toDouble() * 100}%")
}