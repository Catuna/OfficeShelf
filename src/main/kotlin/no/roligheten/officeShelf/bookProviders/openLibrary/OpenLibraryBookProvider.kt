package no.roligheten.officeShelf.bookProviders.openLibrary

import com.beust.klaxon.*
import no.roligheten.officeShelf.bookProviders.BookProvider
import no.roligheten.officeShelf.models.Book
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL
import java.time.Year

class OpenLibraryBookProvider(val apiBaseUrl: String): BookProvider {
    private val httpClient = OkHttpClient()

    override fun getByIsbn(isbn: String): Book? {
        val request = Request.Builder()
                .url(bookUrlByIsbn(isbn))
                .build()

        val response = httpClient.newCall(request).execute()



        return try {
            val bookDto = Klaxon()
                    .converter(OpenLibraryJsonConverter())
                    .parse<OpenLibraryBookDTO>(response.body!!.string())
            bookDto?.run {
                Book(null,
                        title,
                        null,
                        try { Year.of(Integer.valueOf(publishDate)) } catch(e: IllegalArgumentException) { null },
                        isbn,
                        cover?.medium)
            }
        } catch (e: KlaxonException) {
            null
        }
    }

    private fun bookUrlByIsbn(isbn: String): URL {
        return URL("${apiBaseUrl}books?bibkeys=ISBN:$isbn&format=json&jscmd=data")
    }
}


data class OpenLibraryBookDTO(@Json(name = "publish_date") val publishDate: String? = null, val title: String? = null, val cover: CoverDTO? = null)

data class CoverDTO(val medium: String? = null)

class OpenLibraryJsonConverter: Converter {
    override fun canConvert(cls: Class<*>): Boolean = cls == OpenLibraryBookDTO::class.java

    override fun fromJson(jv: JsonValue): OpenLibraryBookDTO? {
        val wrappedBook = jv.obj?.entries?.firstOrNull()?.value

        wrappedBook ?: throw KlaxonException("No matching result found")

        return Klaxon().parseFromJsonObject<OpenLibraryBookDTO>(wrappedBook as JsonObject)
    }

    override fun toJson(value: Any): String {
        throw NotImplementedError()
    }
}