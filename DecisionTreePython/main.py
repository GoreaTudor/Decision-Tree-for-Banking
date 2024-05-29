from typing import List

import time
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

    return count_correct


def run(times=1):
    avg_percentage = 0.0
    avg_time = 0.0

    for i in range(times):
        training, testing = load_csv(".\\data\\bank.csv", debug=False)

        start = time.time()
        decision_tree = DecisionTree(training, lambda x: x.y)
        tree = decision_tree.build_tree()
        end = time.time()

        duration = (end - start) * 1_000.0  # seconds to ms
        correct = test(decision_tree, tree, testing, debug=False)
        percentage = (correct / len(testing)) * 100.0

        avg_percentage = avg_percentage + percentage
        avg_time = avg_time + duration

        # Run info
        print(f"{i + 1}. p: {percentage}%, t: {duration}ms")

    avg_percentage = avg_percentage / times
    avg_time = avg_time / times

    # Result info
    print(f"\n>> Avg. Percentage: {avg_percentage}%")
    print(f">> Avg. Time: {avg_time}ms")


if __name__ == "__main__":
    run(times=20)
