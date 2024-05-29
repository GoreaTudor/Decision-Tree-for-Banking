from typing import List

from data.bank_marketing import BankMarketing
from data.loader import load_csv
from tree.decision_tree import DecisionTree
from tree.node import Node


def test(decision_tree: DecisionTree, tree: Node, data: List[BankMarketing], debug=True):
    count_correct = 0

    for item in data:
        prediction = decision_tree.predict(tree, item)

        if debug:
            print(f"\nItem: {item.to_string()}")
            print(f"Target: {item.y}")
            print(f"Prediction: {prediction}")

        if prediction == item.y:
            count_correct += 1

    print(f"Result: {count_correct / len(data) * 100}%")


if __name__ == "__main__":
    training, testing = load_csv(".\\data\\bank.csv")

    decision_tree = DecisionTree(training, lambda x: x.y)
    tree = decision_tree.build_tree()

    test(decision_tree, tree, testing, debug=True)
