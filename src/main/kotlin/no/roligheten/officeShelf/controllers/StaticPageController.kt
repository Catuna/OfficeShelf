package no.roligheten.officeShelf.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class StaticPageController {

    @GetMapping(path=[""])
    fun frontpage(): String = "frontpage"

    @GetMapping(path=["addBookFrontpage"])
    fun addBookFrontpage(): String = "addBookFrontpage"
}