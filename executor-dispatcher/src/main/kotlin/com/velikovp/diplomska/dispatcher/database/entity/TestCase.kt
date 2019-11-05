package com.velikovp.diplomska.dispatcher.database.entity

import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

/**
 * Model representing a test case for a task.
 *
 * @param task, the task for which the test case is.
 * @param input, the input for the test case.
 * @param expectedOutput, the expected output of the test case.
 */
@Entity
@Table(name = "test_cases")
class TestCase(
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "task_id")
  val task: Task? = null,
  val input: String,
  val expectedOutput: String
) : AbstractJpaPersistable<Long>()