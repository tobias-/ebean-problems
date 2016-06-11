package com.foo.myapp.domain;

import javax.persistence.MappedSuperclass;
import javax.persistence.Id;
import javax.persistence.Version;
import com.avaje.ebean.Model;

import java.time.OffsetDateTime;

@MappedSuperclass
public class BaseDomain extends Model {

  @Id
  Long id;

  @Version
  Long version;

  // Usually also have whenCreated and whenModified

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

}
