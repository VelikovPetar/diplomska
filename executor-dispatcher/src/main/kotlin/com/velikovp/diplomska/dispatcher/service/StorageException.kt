package com.velikovp.diplomska.dispatcher.service

import java.io.IOException
import java.lang.Exception

/**
 * Exception thrown in case of a failure in the service layer.
 *
 * @param message, the message to propagate.
 * @param exception, the exception to propagate.
 */
class StorageException(message: String, exception: Exception? = null): IOException(message, exception)