package we.rashchenko.utility.id

interface DeAnonymizer<out T> {
    fun deAnonymize(id: Int): T
}