package no.roligheten.officeShelf.bookProviders.openLibrary

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import spock.lang.Specification

import java.time.Year

class OpenLibraryBookProviderSpec extends Specification {

    def "should call API and deserialize response correctly"() {
        setup:
        def server = new MockWebServer()
        server.enqueue(new MockResponse().setBody(validMockResponse))
        server.start()
        def baseUrl = server.url("/api/")

        def provider = new OpenLibraryBookProvider(baseUrl.toString())

        when:
        def resp = provider.getByIsbn("9780062316097")

        then:
        resp.title == mockResponseTitle
        resp.author == null
        resp.isbn == mockResponseIsbn
        resp.publishYear == Year.of(mockResponseDate)
        resp.imageUrl == mockResponseImage
    }


    def mockResponseIsbn = "9780062316097"
    def mockResponseTitle = "Sapiens: A Brief History of Humankind"
    def mockResponseDate = 2015
    def mockResponseImage = "https://covers.openlibrary.org/b/id/8594920-L.jpg"
    def validMockResponse = "{\"ISBN:${mockResponseIsbn}\": {\"publishers\": [{\"name\": \"Harper\"}], \"title\": \"${mockResponseTitle}\", \"url\": \"https://openlibrary.org/books/OL27000666M/Sapiens_A_Brief_History_of_Humankind\", \"identifiers\": {\"openlibrary\": [\"OL27000666M\"], \"isbn_10\": [\"0062316095\"]}, \"cover\": {\"small\": \"https://covers.openlibrary.org/b/id/8594920-S.jpg\", \"large\": \"https://covers.openlibrary.org/b/id/8594920-L.jpg\", \"medium\": \"${mockResponseImage}\"}, \"publish_date\": \"${mockResponseDate}\", \"key\": \"/books/OL27000666M\"}}"

}
