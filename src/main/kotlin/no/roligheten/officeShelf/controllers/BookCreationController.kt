package no.roligheten.officeShelf.controllers

import no.roligheten.officeShelf.bookProviders.BookProvider
import no.roligheten.officeShelf.imageStoreServices.FileSystemImageStoreService
import no.roligheten.officeShelf.models.Book
import no.roligheten.officeShelf.repositories.BookRepository
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.time.Year

@Controller
class BookCreationController(private val bookProvider: BookProvider,
                             private val imageStoreService: FileSystemImageStoreService,
                             private val bookRepository: BookRepository) {


    @GetMapping(path=["add"])
    fun add(): String = "addBookFrontpage"

    @GetMapping(path=["lookup"])
    fun lookup(@RequestParam("isbn") isbn: String?, model: Model): String {


        val bookMatch = if (isbn == null) null else bookProvider.getByIsbn(isbn)

        model.addAttribute("bookMatch", bookMatch)

        return "addBook"
    }

    @PostMapping(path=["lookup"])
    fun postLookup(@RequestParam("title") title: String?,
                   @RequestParam("author") author: String?,
                   @RequestParam("publishYear") publishYear: Year?,
                   @RequestParam("isbn") isbn: String?,
                   @RequestParam("imageUrl") imageUrl: String?): ResponseEntity<Any> {

        val imageId = try {
            imageUrl
                    ?.let { BufferedInputStream(URL(it).openStream()) }
                    .let { imageStoreService.storeImage(it as InputStream) }
        } catch (e: IOException) {
            // ERROR handler
            null
        }

        val bookToAdd = Book(null, title, author, publishYear, isbn, imageId)
        val storedBook = bookRepository.save(bookToAdd)



        return ResponseEntity
                .status(HttpStatus.SEE_OTHER)
                .header(HttpHeaders.LOCATION, "/add")
                .build()
    }

    @GetMapping(path=["edit"])
    fun edit(@RequestParam("id") id: Int, model: Model): String {
        val bookMatch = bookRepository.findById(id)

        if (!bookMatch.isPresent) {
            return ""; // TODO: 404
        }
        model.addAttribute("bookMatch", bookMatch.get())

        return "editBook"
    }

    @PostMapping(params = ["update"], path=["edit"])
    fun postEdit(@RequestParam("id") id: Int,
                   @RequestParam("title") title: String?,
                   @RequestParam("author") author: String?,
                   @RequestParam("publishYear") publishYear: Year?,
                   @RequestParam("isbn") isbn: String?,
                   @RequestParam("imageUrl") imageUrl: String?): ResponseEntity<Any> {

        val existingBook = bookRepository.findById(id).orElse(null)
            ?: return ResponseEntity.notFound().build();

        val imageId = when {
            imageUrl == null -> null
            imageUrl == existingBook.imageUrl -> existingBook.imageUrl
            else -> try {
                imageUrl
                    ?.let { BufferedInputStream(URL(it).openStream()) }
                    .let { imageStoreService.storeImage(it as InputStream) }
            } catch (e: IOException) {
                null
            }
        }

        val bookToAdd = Book(id, title, author, publishYear, isbn, imageId)
        bookRepository.save(bookToAdd)



        return ResponseEntity
            .status(HttpStatus.SEE_OTHER)
            .header(HttpHeaders.LOCATION, "/")
            .build()
    }

    @PostMapping(params = ["delete"], path=["edit"])
    fun postDelete(@RequestParam("id") id: Int): ResponseEntity<Any> {

        val existingBook = bookRepository.findById(id).orElse(null)
            ?: return ResponseEntity.notFound().build();

        bookRepository.delete(existingBook);

        return ResponseEntity
            .status(HttpStatus.SEE_OTHER)
            .header(HttpHeaders.LOCATION, "/")
            .build()
    }

}