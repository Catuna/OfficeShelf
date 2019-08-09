package no.roligheten.officeShelf.bookProviders

import no.roligheten.officeShelf.models.Book

class MultiStageBookProvider(private val providers: List<BookProvider>): BookProvider {
    override fun getByIsbn(isbn: String): Book? {
        return providers
                .asSequence()
                .map { it.getByIsbn(isbn) }
                .firstOrNull { it != null }
    }
}