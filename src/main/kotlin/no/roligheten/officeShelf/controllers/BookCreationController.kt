package no.roligheten.officeShelf.controllers

import no.roligheten.officeShelf.bookProviders.BookProvider
import no.roligheten.officeShelf.models.Book
import no.roligheten.officeShelf.repositories.BookRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import java.time.Year

@Controller
class BookCreationController(private val bookProvider: BookProvider,
                             private val bookRepository: BookRepository) {


    @GetMapping(path=["add"])
    fun add(): String = "addBookFrontpage"

    @GetMapping(path=["lookup"])
    fun lookup(@RequestParam("isbn") isbn: String?, model: Model): String {


        val bookMatch = if (isbn == null) null else bookProvider.getByIsbn(isbn)

        model.addAttribute("bookMatch", bookMatch)

        return "addBook"
    }

    @PostMapping(path=["postBook"])
    fun postBook(@RequestParam("title") title: String?,
                 @RequestParam("author") author: String?,
                 @RequestParam("publishYear") publishYear: Year?,
                 @RequestParam("isbn") isbn: String?,
                 @RequestParam("imageUrl") imageUrl: String?): ResponseEntity<Any> {

        val bookToAdd = Book(null, title, author, publishYear, isbn, imageUrl)
        bookRepository.save(bookToAdd)

        return ResponseEntity.ok("")
    }
}