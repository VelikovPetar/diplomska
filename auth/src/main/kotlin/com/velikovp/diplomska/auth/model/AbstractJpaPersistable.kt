package com.velikovp.diplomska.auth.model

import org.springframework.data.util.ProxyUtils
import java.io.Serializable
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

/**
 * Abstract superclass for Hibernate models. Handles the ID generation, equals() and hashCode() methods.
 */
@MappedSuperclass
abstract class AbstractJpaPersistable<T : Serializable> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private var id: T? = null

  fun getId(): T? {
    return id
  }

  override fun equals(other: Any?): Boolean {
    other ?: return false
    if (this === other) {
      return true
    }
    if (javaClass != ProxyUtils.getUserClass(other)) {
      return false
    }
    if (id == null) {
      return false
    }
    return id == (other as AbstractJpaPersistable<*>).id
  }

  override fun hashCode(): Int {
    return 31
  }

  override fun toString(): String {
    return "Entity of type ${javaClass.name} with id: $id"
  }
}