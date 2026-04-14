package com.pfe.service.dto;

import java.util.Set;

public class RolePermissionUpdate {

  private String roleName;
  private Set<String> permissions;

  // Constructors, getters, and setters
  public RolePermissionUpdate() {
  }

  public RolePermissionUpdate(String roleName, Set<String> permissions) {
    this.roleName = roleName;
    this.permissions = permissions;
  }

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  public Set<String> getPermissions() {
    return permissions;
  }

  public void setPermissions(Set<String> permissions) {
    this.permissions = permissions;
  }
}
