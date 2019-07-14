package com.velikovp.diplomska.auth.model

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