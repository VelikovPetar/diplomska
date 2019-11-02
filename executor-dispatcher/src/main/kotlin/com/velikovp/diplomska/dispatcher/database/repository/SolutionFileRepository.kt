package com.velikovp.diplomska.dispatcher.database.repository

import com.velikovp.diplomska.dispatcher.database.entity.SolutionFile
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Repository handling the CRUD operations for the [SolutionFile] model.
 */
interface SolutionFileRepository : JpaRepository<SolutionFile, Long> {
}