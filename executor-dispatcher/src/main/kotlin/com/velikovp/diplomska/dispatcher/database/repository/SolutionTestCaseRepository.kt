package com.velikovp.diplomska.dispatcher.database.repository

import com.velikovp.diplomska.dispatcher.database.entity.SolutionTestCase
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Repository handling the CRUD operations with [SolutionTestCase]s.
 */
interface SolutionTestCaseRepository: JpaRepository<SolutionTestCase, Long>