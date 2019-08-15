package no.roligheten.officeShelf.repositories

import no.roligheten.officeShelf.models.Book
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface BookRepository: CrudRepository<Book, Int> {

    @Query(SEARCH_SQL, nativeQuery = true)
    fun findByTitleIsContaining(@Param("title") title: String): List<Book>

    companion object {
        private const val SEARCH_SQL =
                "SELECT * " +
                "FROM book " +
                "WHERE to_tsvector(title) @@ plainto_tsquery(:title) " +
                "OR to_tsvector(author) @@ plainto_tsquery(:title)"
    }
}