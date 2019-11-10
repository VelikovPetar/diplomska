package com.velikovp.diplomska.dispatcher.database.entity

import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "solution_test_cases")
data class SolutionTestCase(

  val executionSuccessOutput: String? = null,

  val executionErrorOutput: String? = null,

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "solution_id")
  val solution: Solution,

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "test_case_id")
  val testCase: TestCase
): AbstractJpaPersistable<Long>()