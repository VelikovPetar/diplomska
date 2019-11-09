package com.velikovp.diplomska.dispatcher.service.utils

import org.springframework.web.multipart.MultipartFile
import java.io.File

/**
 * Object holding common methods for [File] and [MultipartFile] operations.
 */
object FileUtils {

  /**
   * Reads the contents from a [MultipartFile] as a String.
   *
   * @param multipartFile, the given [MultipartFile].
   * @return the contents of the multipart file as a String.
   */
  fun readContentsFromMultipartFile(multipartFile: MultipartFile) =
    multipartFile.inputStream.readBytes().toString(Charsets.UTF_8)

  /**
   * Reads the contents from a [File] as a String.
   *
   * @param file, the given [File].
   * @return the contents of the file as a String.
   */
  fun readContentsFromFile(file: File) = file.inputStream().readBytes().toString(Charsets.UTF_8)
}