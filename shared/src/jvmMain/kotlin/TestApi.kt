import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val api = OpenLibraryApi()
    val result = api.getBooksBySubject("love")
    println("Subject: ${result.name}, Works: ${result.workCount}")
    result.works.take(5).forEach {
        println("Title: ${it.title}, Authors: ${it.authors.joinToString { a -> a.name }}")
    }
}