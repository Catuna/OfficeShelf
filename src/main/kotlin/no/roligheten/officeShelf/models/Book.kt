package no.roligheten.officeShelf.models

import no.roligheten.officeShelf.converters.YearConverter
import java.time.Year
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Book(
    @Id @GeneratedValue val id: Int?,
    val title: String?,
    val author: String?,
    @Convert(converter = YearConverter::class) val publishYear: Year?,
    val isbn: String?,
    val imageUrl: String?)