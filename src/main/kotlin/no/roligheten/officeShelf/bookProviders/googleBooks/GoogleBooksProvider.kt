package no.roligheten.officeShelf.bookProviders.googleBooks

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.Parser
import no.roligheten.officeShelf.bookProviders.BookProvider
import no.roligheten.officeShelf.models.Book
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.StringReader
import java.time.Year
import java.util.regex.Matcher
import java.util.regex.Pattern


class GoogleBooksProvider(private val apiBaseUrl: String): BookProvider {
    private val httpClient = OkHttpClient()

    override fun getByIsbn(isbn: String): Book? {
        val searchRequest = Request.Builder()
                .url(bookUrlByIsbn(isbn))
                .build()

        val searchResponse = httpClient.newCall(searchRequest).execute()

        val bookResourceUrl = searchResponse
                .takeIf(Response::isSuccessful)
                ?.body?.string()
                ?.let(::getBookUrlFromSearchResult) ?: return null

        val resourceRequest = Request.Builder()
                .url(bookResourceUrl)
                .build()

        val resourceResponse = httpClient.newCall(resourceRequest).execute()

        val volume = resourceResponse.takeIf(Response::isSuccessful)
                ?.body?.string()
                ?.let(::getBookResourceFromResourceResult) ?: return null

        return volume.run {
            Book(
                    null,
                    title,
                    authors?.firstOrNull(),
                    getYearFromResponsePublishedDate(publishedDate),
                    getIsbnFromResponseIndustryIdentifiers(industryIdentifiers),
                    stripCurlFromUrl(imageLinks?.medium ?: imageLinks?.small)
            )
        }
    }

    private fun bookUrlByIsbn(isbn: String) = "${apiBaseUrl}volumes?q=isbn:$isbn"

    private fun getIsbnFromResponseIndustryIdentifiers(industryIdentifiers: List<IndustryIdentifierDTO>?): String? =
        industryIdentifiers?.firstOrNull { it.type == "ISBN_13" }?.identifier

    private fun getBookUrlFromSearchResult(searchResponse: String): String? {
        val parsedResponse = Parser.default().parse(StringReader(searchResponse)) as JsonObject

        return parsedResponse
                .array<JsonObject>("items")
                ?.firstOrNull()
                ?.string("selfLink")
    }

    private fun getYearFromResponsePublishedDate(date: String?): Year? =
            date?.let(DATE_YEAR_MATCHER::matcher)
                ?.takeIf(Matcher::find)
                ?.group()
                ?.let(String::toInt)
                ?.let(Year::of)

    private fun getBookResourceFromResourceResult(response: String): VolumeDTO? {
        val parsedResponse = Parser.default().parse(StringReader(response)) as JsonObject

        return parsedResponse.obj("volumeInfo")?.let { Klaxon().parseFromJsonObject<VolumeDTO>(it) }
    }

    // For some reason Google returns a URL that includes a "curl" at the bottom, this removes that
    private fun stripCurlFromUrl(imageUrl: String?): String? =
            imageUrl?.let { return it.replace("&edge=curl", "") }

    companion object {
        val DATE_YEAR_MATCHER = Pattern.compile("(\\d{4})")!!
    }
}

data class VolumeDTO(val title: String? = null,
                     val authors: List<String>? = null,
                     val publishedDate: String? = null,
                     val industryIdentifiers: List<IndustryIdentifierDTO>? = null,
                     val imageLinks: ImageLinksDTO? = null)

data class IndustryIdentifierDTO(val type: String, val identifier: String)

data class ImageLinksDTO(val medium: String? = null, val small: String? = null)