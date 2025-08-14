package dev.drivemode.techtest.data

import dev.drivemode.techtest.BookModel
import dev.drivemode.techtest.Works
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable

class BookRepository(
    private val client: HttpClient
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getBooksBySubject(subject: String): Works {
        if (subject.isBlank()) {
            throw IllegalArgumentException("Subject cannot be empty")
        }

        val response: HttpResponse = client.get("https://openlibrary.org/subjects/${subject}.json") {
            contentType(ContentType.Application.Json)
        }

        if (!response.status.isSuccess()) {
            throw Exception("Failed to fetch books: ${response.status}")
        }

        val body = response.bodyAsText()
        val parsed = json.decodeFromString(SubjectResponse.serializer(), body)

        if (parsed.works.isEmpty()) {
            throw Exception("No books found for subject: $subject")
        }

        val books = parsed.works.map {
            BookModel(
                title = it.title,
                author = it.authors.firstOrNull()?.name ?: "Unknown",
                key = it.key,
                imageUrl = "https://covers.openlibrary.org/b/olid/${it.cover_edition_key}-M.jpg"
            )
        }

        return Works(books, System.currentTimeMillis().toString())
    }
}

@Serializable
data class SubjectResponse(
    val works: List<WorkItem>
)

@Serializable
data class WorkItem(
    val title: String,
    val key: String,
    val cover_edition_key: String? = null,
    val authors: List<AuthorItem> = emptyList()
)

@Serializable
data class AuthorItem(
    val name: String
)