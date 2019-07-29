package no.roligheten.officeShelf.models

import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Book(
        @Id val id: Int,
        val title: String,
        val author: String,
        val publishDate: LocalDate,
        val isbn: String)