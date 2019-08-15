package no.roligheten.officeShelf.configs

import no.roligheten.officeShelf.bookProviders.BookProvider
import no.roligheten.officeShelf.bookProviders.MultiStageBookProvider
import no.roligheten.officeShelf.bookProviders.googleBooks.GoogleBooksProvider
import no.roligheten.officeShelf.bookProviders.openLibrary.OpenLibraryBookProvider
import no.roligheten.officeShelf.imageStoreServices.FileSystemImageStoreService
import okhttp3.internal.immutableListOf
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import java.io.File

@Configuration
@EnableJpaRepositories("no.roligheten.officeShelf.repositories")
class OfficeShelfApplicationConfig {

    @Bean
    fun bookProvider(
            @Value("\${providers.googleBooksApiBaseUrl}") googleBooksBaseUrl: String,
            @Value("\${providers.openLibraryApiBaseUrl}") openLibraryBaseUrl: String): BookProvider {
        return MultiStageBookProvider(
                immutableListOf(
                        GoogleBooksProvider(googleBooksBaseUrl),
                        OpenLibraryBookProvider(openLibraryBaseUrl)
                )
        )
    }
    @Bean
    fun imageStore(
            @Value("\${image-stores.file-system-image-store-service.location}") location: String?): FileSystemImageStoreService {
        if (location == null) {
            throw IllegalStateException("image-stores.file-system-image-store-service.location must be defined")
        }

        if (!File(location).isDirectory) {
            throw IllegalStateException("Location \"$location\" does not exists or is not a directory")
        }
        return FileSystemImageStoreService(location)
    }
}