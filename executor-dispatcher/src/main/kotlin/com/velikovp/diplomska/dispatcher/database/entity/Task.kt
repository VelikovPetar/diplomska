package com.velikovp.diplomska.dispatcher.database.entity

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.OneToMany
import javax.persistence.Table

/**
 * Model representing a task.
 *
 * @param descriptionText, the description of the task.
 * @param testCases, list of test cases against which submitted solutions will be tested.
 */
@Entity
@Table(name = "tasks")
class Task(
  val descriptionText: String,
  @OneToMany(mappedBy = "task", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
  val testCases: List<TestCase> = emptyList()
) : AbstractJpaPersistable<Long>()