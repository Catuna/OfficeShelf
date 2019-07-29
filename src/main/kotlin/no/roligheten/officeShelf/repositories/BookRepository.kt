package no.roligheten.officeShelf.repositories

import no.roligheten.officeShelf.models.Book
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface BookRepository: CrudRepository<Book, Int> {

    fun findByTitleIsContaining(@Param("title") title: String): List<Book>
}