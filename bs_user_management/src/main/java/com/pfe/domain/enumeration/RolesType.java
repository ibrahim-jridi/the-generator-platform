package com.pfe.domain.enumeration;

public enum RolesType {
  PERSONNE_PHYSIQUE("personne physique"),
  PERSONNE_MORALE("personne morale"),
  INTERNE("interne");

  private final String label;

  RolesType(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  @Override
  public String toString() {
    return label;
  }
}
