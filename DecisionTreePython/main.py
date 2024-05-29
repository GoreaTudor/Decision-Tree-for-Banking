from typing import List

import time
from data.bank_marketing import BankMarketing
from data.loader import load_csv
from tree.decision_tree import DecisionTree
from tree.node import Node


def test(decision_tree: DecisionTree, tree: Node, data: List[BankMarketing], time, debug=True):
    count_correct = 0

    for item in data:
        prediction = decision_tree.predict(tree, item)

        if debug:
            print(f"\nItem: {item.to_string()}")
            print(f"Target: {item.y}")
            print(f"Prediction: {prediction}")

        if prediction == item.y:
            count_correct += 1

    print(f"\n\n>> Tested: {len(data)}")
    print(f">> Correct: {count_correct}")
    print(f">> Wrong: {len(data) - count_correct}")
    print(f"\n>> Correct Percentage: {count_correct / len(data) * 100}%")
    print(f">> Time: {time}ms")


if __name__ == "__main__":
    training, testing = load_csv(".\\data\\bank.csv")

    start = time.time()
    decision_tree = DecisionTree(training, lambda x: x.y)
    tree = decision_tree.build_tree()
    end = time.time()

    duration = (end - start) * 1_000.0

    test(decision_tree, tree, testing, duration, debug=False)
