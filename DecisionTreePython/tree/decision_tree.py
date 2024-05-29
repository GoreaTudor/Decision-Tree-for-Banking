import math
from typing import List

from data.bank_marketing import BankMarketing
from tree.node import Node, LeafNode, DecisionNode


class DecisionTree:
    def __init__(self, data: List[BankMarketing], target):
        self.data = data
        self.target = target


    def build_tree(self) -> Node:
        return self._build(self.data)


    def _build(self, data: List[BankMarketing]) -> Node:
        if not data:
            return LeafNode(False)
        if all(self.target(item) == self.target(data[0]) for item in data):
            return LeafNode(self.target(data[0]))

        best_attr, best_threshold, best_split = self._find_best_split(data)
        if best_split is None:
            majority_value = max(set(self.target(item) for item in data),
                                 key=lambda val: sum(self.target(item) == val for item in data))
            return LeafNode(majority_value)

        left_data, right_data = best_split
        left_node = self._build(left_data)
        right_node = self._build(right_data)

        return DecisionNode(best_attr, best_threshold, left_node, right_node)


    def _find_best_split(self, data: List[BankMarketing]):

        features = ["age", "job", "marital", "education", "default", "balance", "housing", "loan", "contact",
                    "day_of_week", "month", "duration", "campaign", "pdays", "previous", "poutcome"]
        best_gain = 0.0
        best_attr = None
        best_threshold = None
        best_split = None

        for feature in features:
            values = set(self._get_attribute(item, feature) for item in data)

            for value in values:
                left, right = self._partition(data, feature, value)
                gain = self._information_gain(data, left, right)

                if gain > best_gain:
                    best_gain = gain
                    best_attr = feature
                    best_threshold = value
                    best_split = (left, right)

        return best_attr, best_threshold, best_split


    def _partition(self, data: List[BankMarketing], feature: str, value):
        left = [item for item in data if self._compare(self._get_attribute(item, feature), value) <= 0]
        right = [item for item in data if self._compare(self._get_attribute(item, feature), value) > 0]

        return left, right


    def _get_attribute(self, data: BankMarketing, attribute: str):
        return getattr(data, attribute)


    def _compare(self, a, b) -> int:
        if isinstance(a, str) and isinstance(b, str):
            return (a > b) - (a < b)
        return (a > b) - (a < b)


    def _information_gain(self, total: List[BankMarketing], left: List[BankMarketing],
                          right: List[BankMarketing]) -> float:
        entropy_total = self._entropy(total)
        entropy_left = self._entropy(left)
        entropy_right = self._entropy(right)
        left_weight = len(left) / len(total)
        right_weight = len(right) / len(total)
        return entropy_total - (left_weight * entropy_left + right_weight * entropy_right)


    def _entropy(self, data: List[BankMarketing]) -> float:
        target_count = {val: sum(1 for item in data if self.target(item) == val) for val in
                        set(self.target(item) for item in data)}
        total = len(data)
        return -sum((count / total) * math.log2(count / total) for count in target_count.values())


    def predict(self, tree: Node, instance: BankMarketing) -> bool:
        if isinstance(tree, LeafNode):
            return tree.prediction

        if self._compare(self._get_attribute(instance, tree.attribute), tree.threshold) <= 0:
            return self.predict(tree.left, instance)
        else:
            return self.predict(tree.right, instance)
