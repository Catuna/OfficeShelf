package no.roligheten.officeShelf.configs

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class WebMvcConfig : WebMvcConfigurer {

    @Value("\${image-stores.file-system-image-store-service.location}") private lateinit var fileLocation: String

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry
                .addResourceHandler("images/*")
                .addResourceLocations("file:$fileLocation")
                .setCachePeriod(0)
    }
}