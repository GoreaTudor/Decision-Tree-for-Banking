package org.example.tree

import org.example.data.BankMarketing
import kotlin.math.log2

class DecisionTree(
    private val data: List<BankMarketing>,
    private val target: (BankMarketing) -> Boolean
) {

    fun buildTree(): Node {
        return build(data)
    }

    fun predict(tree: Node, instance: BankMarketing): Boolean {
        return when (tree) {
            is Node.Leaf -> tree.prediction

            is Node.Decision -> {
                val value = getAttribute(instance, tree.attribute)
                if (compareAttributes(value, tree.threshold) <= 0) predict(tree.left, instance)
                else predict(tree.right, instance)
            }

            else -> {
                throw Exception("You can't get here anyway")
            }
        }
    }


    private fun build(data: List<BankMarketing>): Node {
        if (data.isEmpty()) return Node.Leaf(false)
        if (data.all { target(it) == target(data[0]) }) return Node.Leaf(target(data[0]))

        val (bestAttr, bestThreshold, bestSplit) = findBestSplit(data) ?: return Node.Leaf(
            data.groupBy { target(it) }.maxByOrNull { it.value.size }!!.key
        )

        val (leftData, rightData) = bestSplit
        return Node.Decision(bestAttr, bestThreshold, build(leftData), build(rightData))
    }

    private fun findBestSplit(
        data: List<BankMarketing>
    ): Triple<String, Any, Pair<List<BankMarketing>, List<BankMarketing>>>? {
        val features = listOf(
            "age",
            "job",
            "marital",
            "education",
            "default",
            "balance",
            "housing",
            "loan",
            "contact",
            "day_of_week",
            "month",
            "duration",
            "campaign",
            "pdays",
            "previous",
            "poutcome"
        )
        var bestGain = 0.0
        var bestAttr: String? = null
        var bestThreshold: Any? = null
        var bestSplit: Pair<List<BankMarketing>, List<BankMarketing>>? = null

        for (feature in features) {
            val values = data.map { getAttribute(it, feature) }.distinct()
            for (value in values) {
                val (left, right) = data.partition {
                    compareAttributes(
                        getAttribute(it, feature),
                        value
                    ) <= 0
                }
                val gain = calculateInformationGain(data, left, right)
                if (gain > bestGain) {
                    bestGain = gain
                    bestAttr = feature
                    bestThreshold = value
                    bestSplit = Pair(left, right)
                }
            }
        }
        return bestAttr?.let { Triple(it, bestThreshold!!, bestSplit!!) }
    }

    private fun compareAttributes(a: Comparable<*>, b: Any): Int {
        return when {
            a is String && b is String -> a.compareTo(b)

            a is Int && b is Int -> a.compareTo(b)

            a is Boolean && b is Boolean -> a.compareTo(b)

            else -> throw IllegalArgumentException("Cannot compare attributes of different types")
        }
    }

    private fun calculateInformationGain(
        total: List<BankMarketing>,
        left: List<BankMarketing>,
        right: List<BankMarketing>
    ): Double {
        val entropyTotal = calculateEntropy(total)
        val entropyLeft = calculateEntropy(left)
        val entropyRight = calculateEntropy(right)

        val leftWeight = left.size.toDouble() / total.size
        val rightWeight = right.size.toDouble() / total.size

        return entropyTotal - (leftWeight * entropyLeft + rightWeight * entropyRight)
    }

    private fun calculateEntropy(data: List<BankMarketing>): Double {
        val targetCount = data
            .groupBy { target(it) }
            .mapValues { it.value.size }

        val total = data.size.toDouble()

        // sumOf == map + sum
        return targetCount.values
            .sumOf { -it / total * log2(it / total) }
    }

    private fun getAttribute(data: BankMarketing, attribute: String): Comparable<*> {
        return when (attribute) {
            "age" -> data.age
            "job" -> data.job
            "marital" -> data.marital
            "education" -> data.education
            "default" -> data.default
            "balance" -> data.balance
            "housing" -> data.housing
            "loan" -> data.loan
            "contact" -> data.contact ?: "unknown"
            "day_of_week" -> data.day_of_week
            "month" -> data.month
            "duration" -> data.duration
            "campaign" -> data.campaign
            "pdays" -> data.pdays ?: -1
            "previous" -> data.previous
            "poutcome" -> data.poutcome
            else -> throw IllegalArgumentException("Unknown attribute $attribute")
        }
    }
}
