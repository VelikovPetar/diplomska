package com.velikovp.diplomska.dispatcher.database.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

/**
 * Defines the primary key for a Many-to-Many [SolutionTestCase] table.
 */
@Embeddable
data class SolutionTestCaseKey(
  @Column(name = "solution_id")
  val solutionId: Long,

  @Column(name = "test_case_id")
  val testCaseId: Long
) : Serializable