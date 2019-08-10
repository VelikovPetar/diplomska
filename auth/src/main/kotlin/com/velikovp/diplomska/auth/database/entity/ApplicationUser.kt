package com.velikovp.diplomska.auth.database.entity

import javax.persistence.Entity

/**
 * Model representing a user of the application.
 */
@Entity
class ApplicationUser(val email: String,
                      val name: String,
                      val surname: String,
                      var password: String
) : AbstractJpaPersistable<Long>()