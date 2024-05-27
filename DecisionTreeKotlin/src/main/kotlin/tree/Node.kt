package org.example.tree

sealed class Node {
    data class Decision(val attribute: String, val threshold: Any, val left: Node, val right: Node): Node()
    data class Leaf(val prediction: Boolean): Node()
}