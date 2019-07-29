package no.roligheten.officeShelf.bookProviders

import no.roligheten.officeShelf.models.Book
import java.time.Year

class PlaceholderBookProvider: BookProvider {
    override fun getByIsbn(isbn: String): Book? {
        return Book(
                1,
                "The Design of Everyday Things",
                "Don Norman",
                Year.of(1988),
                "9780465067107")

    }
}