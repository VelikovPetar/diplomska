package com.velikovp.diplomska.dispatcher.database.repository

import com.velikovp.diplomska.dispatcher.database.entity.TestCase
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Repository handling the CRUD operations for test cases.
 */
interface TestCasesRepository : JpaRepository<TestCase, Long>