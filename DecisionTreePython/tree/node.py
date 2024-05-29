class Node:
    pass


class DecisionNode(Node):
    def __init__(self, attribute: str, threshold, left: Node, right: Node):
        self.attribute = attribute
        self.threshold = threshold
        self.left = left
        self.right = right


class LeafNode(Node):
    def __init__(self, prediction: bool):
        self.prediction = prediction
