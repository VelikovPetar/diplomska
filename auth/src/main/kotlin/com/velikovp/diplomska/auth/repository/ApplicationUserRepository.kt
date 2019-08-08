package com.velikovp.diplomska.auth.repository

import com.velikovp.diplomska.auth.model.ApplicationUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * Repository handling the CRUD operations for the ApplicationUser model.
 */
interface ApplicationUserRepository : JpaRepository<ApplicationUser, Long> {

  /**
   * Finds the user identified by the given mail.
   *
   * @param email, the email to search the user by.
   */
  @Query("select u from ApplicationUser u where u.email = :email")
  fun findByEmail(@Param("email") email: String): ApplicationUser?
}