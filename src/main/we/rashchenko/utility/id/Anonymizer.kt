package we.rashchenko.utility.id

interface Anonymizer<in T> {
    fun anonymize(item: T): Int
}