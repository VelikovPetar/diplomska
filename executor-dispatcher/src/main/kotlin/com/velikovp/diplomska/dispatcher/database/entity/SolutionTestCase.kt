package com.velikovp.diplomska.dispatcher.database.entity

import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.MapsId
import javax.persistence.Table

@Entity
@Table(name = "solution_test_cases")
data class SolutionTestCase(
  @EmbeddedId
  val id: SolutionTestCaseKey,

  val output: String,

  @ManyToOne(fetch = FetchType.EAGER)
  @MapsId("solution_id")
  @JoinColumn(name = "solution_id")
  val solution: Solution,

  @ManyToOne(fetch = FetchType.EAGER)
  @MapsId("test_case_id")
  @JoinColumn(name = "test_case_id")
  val testCase: TestCase
)