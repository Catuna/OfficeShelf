package no.roligheten.officeShelf.controllers

import no.roligheten.officeShelf.repositories.BookRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class SearchResultController(private val bookRepository: BookRepository) {

    @GetMapping(path=[""])
    fun search(@RequestParam("query") query: String?, model: Model): String {

        val matchingBooks = if (query == null)
            bookRepository.findAll()
        else
            bookRepository.findByTitleIsContaining(query)

        model.addAttribute("matchingBooks", matchingBooks)
        model.addAttribute("query", query ?: "")

        return "searchResults"
    }
}