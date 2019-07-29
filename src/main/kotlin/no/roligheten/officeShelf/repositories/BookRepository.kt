package no.roligheten.officeShelf.repositories

import no.roligheten.officeShelf.models.Book
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface BookRepository: CrudRepository<Book, Int> {

    @Query("select b from Book b where b.title LIKE :title")
    fun findAllByFuzzyTitle(@Param("title") title: String): List<Book>
}