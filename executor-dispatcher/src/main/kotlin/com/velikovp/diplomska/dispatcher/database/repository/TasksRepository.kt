package com.velikovp.diplomska.dispatcher.database.repository

import com.velikovp.diplomska.dispatcher.database.entity.Task
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Repository handling the CRUD operations with [Task]s.
 */
interface TasksRepository: JpaRepository<Task, Long>