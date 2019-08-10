package com.velikovp.diplomska.auth.database.repository

import com.velikovp.diplomska.auth.database.entity.ApplicationUser
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

  /**
   * Finds the user identified by the given mail and password.
   *
   * @param email, the email to search the user by.
   * @param password, the password to search the suer by.
   */
  @Query("select u from ApplicationUser u where u.email = :email and u.password = :password")
  fun findByEmailAndPassword(@Param("email") email: String, @Param("password") password:String): ApplicationUser?
}