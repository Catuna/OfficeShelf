package no.roligheten.officeShelf.repositories

import no.roligheten.officeShelf.models.Book
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface BookRepository: CrudRepository<Book, Int> {

    @Query("select * from book where to_tsvector(title) @@ to_tsquery(:title)", nativeQuery = true)
    fun findByTitleIsContaining(@Param("title") title: String): List<Book>
}