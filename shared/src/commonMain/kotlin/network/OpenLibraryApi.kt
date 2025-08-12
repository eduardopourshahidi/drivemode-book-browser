package network

import dev.drivemode.techtest.BookModel
import dev.drivemode.techtest.Works
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubjectResponse(
    val name: String,
    @SerialName("work_count") val workCount: Int,
    val works: List<Worka>
)

@Serializable
data class Work(
    val title: String,
    @SerialName("cover_id") val coverId: Int? = null,
    val authors: List<Author>
)

@Serializable
data class Author(
    val name: String
)

class OpenLibraryApi {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun getBooksBySubject(subject: String): SubjectResponse {
        val url = "https://openlibrary.org/subjects/${subject}.json"
        return client.get(url).body()
    }
}