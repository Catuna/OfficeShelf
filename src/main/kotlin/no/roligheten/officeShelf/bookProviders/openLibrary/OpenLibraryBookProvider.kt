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

        val bookDto = Klaxon()
                .converter(OpenLibraryJsonConverter())
                .parse<OpenLibraryBookDTO>(response.body!!.string())

        return if (bookDto == null)
            null
        else
            Book(null,
                bookDto.title,
                null,
                try { Year.of(Integer.valueOf(bookDto.publishDate)) } catch(e: Exception) { null },
                isbn,
                bookDto.cover?.medium)
    }

    private fun bookUrlByIsbn(isbn: String): URL {
        return URL("${apiBaseUrl}books?bibkeys=ISBN:$isbn&format=json&jscmd=data")
    }
}


data class OpenLibraryBookDTO(@Json(name = "publish_date") val publishDate: String?, val title: String?, val cover: CoverDTO?)

data class CoverDTO(val medium: String?)

class OpenLibraryJsonConverter: Converter {
    override fun canConvert(cls: Class<*>): Boolean = cls == OpenLibraryBookDTO::class.java

    override fun fromJson(jv: JsonValue): OpenLibraryBookDTO? {
        val wrappedBook = jv.obj?.entries?.first()?.value as JsonObject
        return Klaxon().parseFromJsonObject<OpenLibraryBookDTO>(wrappedBook)
    }

    override fun toJson(value: Any): String {
        throw NotImplementedError()
    }
}