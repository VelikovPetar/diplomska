package com.velikovp.diplomska.auth.repository

import com.velikovp.diplomska.auth.model.ApplicationUser
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Repository handling the CRUD operations for the ApplicationUser model.
 */
interface ApplicationUserRepository : JpaRepository<ApplicationUser, Long> {
}