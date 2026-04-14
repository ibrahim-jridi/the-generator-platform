package com.pfe.security;

import java.io.Serializable;
import java.util.Objects;

public class RealmConfig implements Serializable {

  private static final long serialVersionUID = -7696289414420574723L;

  private String realm;

  private String url;

  private String urlbase;
  private String clientid;

  public RealmConfig() {
  }

  public RealmConfig(String realm, String url, String urlbase, String clientid) {
    this.realm = realm;
    this.url = url;
    this.urlbase = urlbase;
    this.clientid = clientid;
  }

  public String getRealm() {
    return this.realm;
  }

  public void setRealm(String realm) {
    this.realm = realm;
  }

  public String getUrl() {
    return this.url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getClientid() {
    return this.clientid;
  }

  public void setClientid(String clientid) {
    this.clientid = clientid;
  }

  public String getUrlbase() {
    return this.urlbase;
  }

  public void setUrlbase(String urlbase) {
    this.urlbase = urlbase;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RealmConfig that = (RealmConfig) o;
    return Objects.equals(this.realm, that.realm) && Objects.equals(this.urlbase, that.urlbase);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.realm, this.urlbase);
  }
}
