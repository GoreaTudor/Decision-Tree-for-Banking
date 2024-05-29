package data

import org.example.data.BankMarketing
import java.io.File
import kotlin.random.Random

fun String.toBooleanYesNo(): Boolean {
    return this.equals("\"yes\"", ignoreCase = true)
}

fun split(
    data: List<BankMarketing>,
    debug: Boolean = true
): Pair<List<BankMarketing>, List<BankMarketing>> {
    // 90% - training, 10% - testing

    val training = mutableListOf<BankMarketing>()
    val testing = mutableListOf<BankMarketing>()

    data.forEach {
        if (Random.nextInt(from = 1, until = 100) < 90) {
            testing.add(it)
        } else {
            training.add(it)
        }
    }

    if (debug) {
        println("Training size: ${training.size}")
        println("testing size: ${testing.size}")
    }

    return Pair(training, testing)
}

fun loadCSV(filePath: String, debug: Boolean = true): Pair<List<BankMarketing>, List<BankMarketing>> {
    val data = mutableListOf<BankMarketing>()

    File(filePath).bufferedReader().use { reader ->
        reader.readLine() // skip header

        reader.forEachLine { line ->
            val tokens = line.split(";").map { it.trim() }

            val bankMarketing = BankMarketing(
                age = tokens[0].toInt(),
                job = tokens[1],
                marital = tokens[2],
                education = tokens[3],
                default = tokens[4].toBooleanYesNo(),
                balance = tokens[5].toInt(),
                housing = tokens[6].toBooleanYesNo(),
                loan = tokens[7].toBooleanYesNo(),
                contact = if (tokens[8].isNotEmpty()) tokens[8] else null,
                day_of_week = tokens[9],
                month = tokens[10],
                duration = tokens[11].toInt(),
                campaign = tokens[12].toInt(),
                pdays = if (tokens[13].isNotEmpty()) tokens[13].toInt() else null,
                previous = tokens[14].toInt(),
                poutcome = tokens[15],
                y = tokens[16].toBooleanYesNo()
            )
            data.add(bankMarketing)
        }
    }

    return split(data, debug)
}
