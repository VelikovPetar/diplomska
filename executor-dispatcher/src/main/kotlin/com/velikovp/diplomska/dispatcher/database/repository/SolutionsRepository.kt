package com.velikovp.diplomska.dispatcher.database.repository

import com.velikovp.diplomska.dispatcher.database.entity.Solution
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Repository handling the CRUD operations for the [Solution] model.
 */
interface SolutionsRepository : JpaRepository<Solution, Long>