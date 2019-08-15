package no.roligheten.officeShelf.imageStoreServices

import java.io.InputStream
import java.io.Serializable

interface ImageStoreService {
    fun storeImage(inputStream: InputStream): String
    fun deleteImage(identifier: String)
}