package no.roligheten.officeShelf.imageStoreServices

import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.UUID



class FileSystemImageStoreService(private val storeDirectory: String): ImageStoreService {
    override fun storeImage(inputStream: InputStream): String {
        val uuid = UUID.randomUUID()
        Files.copy(inputStream, Paths.get(storeDirectory).resolve(uuid.toString()), StandardCopyOption.REPLACE_EXISTING)

        return uuid.toString()
    }

    override fun deleteImage(identifier: String) {
        TODO("not implemented")
    }
}