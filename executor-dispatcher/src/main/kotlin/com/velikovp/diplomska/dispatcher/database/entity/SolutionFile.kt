package com.velikovp.diplomska.dispatcher.database.entity

import javax.persistence.Entity
import javax.persistence.Lob
import javax.persistence.Table

/**
 * Model representing a submitted solution for a given task.
 *
 * @param filename, name of the file.
 * @param fileContentType, content-type of the file.
 * @param language, language of the solution(Python/Java).
 * @param taskId, the Id of the task which this solution was submitted for.
 * @param submittedBy, the Id of the user which submitted the solution.
 * @param content, the content of the solution file.
 */
@Entity
@Table(name = "solutions")
class SolutionFile(val filename: String,
                   val fileContentType: String,
                   val language: String,
                   val taskId: Long,
                   val submittedBy: Long,
                   @Lob val content: ByteArray
): AbstractJpaPersistable<Long>()