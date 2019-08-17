package no.roligheten.officeShelf.converters

import java.time.LocalDate
import java.time.Year
import javax.persistence.AttributeConverter


class YearConverter : AttributeConverter<Year, Int> {

    override fun convertToDatabaseColumn(year: Year): Int {
        return year.value
    }

    override fun convertToEntityAttribute(year: Int): Year {
        return Year.of(year)
    }
}