class BankMarketing:
    def __init__(self, age, job, marital, education, default, balance, housing, loan, contact, day_of_week, month,
                 duration, campaign, pdays, previous, poutcome, y):
        self.age = age
        self.job = job
        self.marital = marital
        self.education = education
        self.default = default
        self.balance = balance
        self.housing = housing
        self.loan = loan
        self.contact = contact
        self.day_of_week = day_of_week
        self.month = month
        self.duration = duration
        self.campaign = campaign
        self.pdays = pdays
        self.previous = previous
        self.poutcome = poutcome
        self.y = y


    def to_string(self):
        return (f"({self.age}, {self.job}, {self.marital}, {self.education}, {self.default}, {self.balance}, "
                f"{self.housing}, {self.loan}, {self.contact}, {self.day_of_week}, {self.month}, {self.month}, "
                f"{self.duration}, {self.campaign}, {self.pdays}, {self.previous}, {self.poutcome}, {self.y})")
