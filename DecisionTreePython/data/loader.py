import random
from typing import List

from data.bank_marketing import BankMarketing


def split(data: List[BankMarketing], debug: bool = True):
    # 90% - training, 10% - testing
    training = []
    testing = []

    for item in data:
        if random.randint(1, 100) < 90:
            testing.append(item)
        else:
            training.append(item)

    if debug:
        print(f"Training size: {len(training)}")
        print(f"Testing size: {len(testing)}")

    return training, testing


def load_csv(file_path: str):
    data = []

    with open(file_path, 'r') as file:
        file.readline()  # ignore header

        for line in file:
            tokens = line.strip().split(';')
            data.append(BankMarketing(
                age=int(tokens[0]),
                job=tokens[1],
                marital=tokens[2],
                education=tokens[3],
                default=tokens[4] == '"yes"',
                balance=int(tokens[5]),
                housing=tokens[6] == '"yes"',
                loan=tokens[7] == '"yes"',
                contact=tokens[8] if tokens[8] else None,
                day_of_week=tokens[9],
                month=tokens[10],
                duration=int(tokens[11]),
                campaign=int(tokens[12]),
                pdays=int(tokens[13]) if tokens[13] else None,
                previous=int(tokens[14]),
                poutcome=tokens[15],
                y=tokens[16] == '"yes"'
            ))

    return split(data)
