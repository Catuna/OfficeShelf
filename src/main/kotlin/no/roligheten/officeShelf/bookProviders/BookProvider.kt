package no.roligheten.officeShelf.bookProviders

import no.roligheten.officeShelf.models.Book

interface BookProvider {
    fun getByIsbn(isbn: String): Book?
}