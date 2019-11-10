package com.velikovp.diplomska.dispatcher.database.entity

import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.Lob
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

/**
 * Model representing a submitted solution for a given task.
 *
 * @param filename, name of the file.
 * @param fileContentType, content-type of the file.
 * @param language, language of the solution(Python/Java).
 * @param userId, the Id of the user who submitted the solution.
 * @param task, the task for which this solution is submitted for.
 * @param content, the content of the solution file.
 */
@Entity
@Table(name = "solutions")
class Solution(
  val filename: String,

  val fileContentType: String,

  val language: String,

  // todo foreign key to User
  val userId: Long,

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "task_id")
  val task: Task,

  @OneToMany(mappedBy = "solution")
  var solutionTestCases: List<SolutionTestCase> = emptyList(),

  val content: String
) : AbstractJpaPersistable<Long>()