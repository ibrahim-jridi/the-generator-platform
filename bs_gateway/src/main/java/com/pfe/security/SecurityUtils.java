package com.pfe.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtils {

  public static final String CLAIMS_NAMESPACE = "https://www.esprit.tn/";
  public static final String PREFERRED_USERNAME = "preferred_username";

  private SecurityUtils() {
  }

  /**
   * Get the login of the current user.
   *
   * @return the login of the current user.
   */
  public static Optional<String> getCurrentUserLogin() {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
  }

  private static String extractPrincipal(Authentication authentication) {
    if (authentication == null) {
      return null;
    } else if (authentication instanceof BearerTokenAuthentication) {
      Map<String, Object> tokenAttributes = ((BearerTokenAuthentication) authentication).getTokenAttributes();
      if (tokenAttributes.containsKey(PREFERRED_USERNAME)) {
        return (String) tokenAttributes.get(PREFERRED_USERNAME);
      }
    } else if (authentication.getPrincipal() instanceof UserDetails) {
      UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
      return springSecurityUser.getUsername();
    } else if (authentication instanceof JwtAuthenticationToken) {
      return (String) ((JwtAuthenticationToken) authentication).getToken().getClaims()
          .get(PREFERRED_USERNAME);
    } else if (authentication.getPrincipal() instanceof DefaultOidcUser) {
      Map<String, Object> attributes = ((DefaultOidcUser) authentication.getPrincipal()).getAttributes();
      if (attributes.containsKey(PREFERRED_USERNAME)) {
        return (String) attributes.get(PREFERRED_USERNAME);
      }
    } else if (authentication.getPrincipal() instanceof String) {
      return (String) authentication.getPrincipal();
    }
    return null;
  }

  /**
   * Check if a user is authenticated.
   *
   * @return true if the user is authenticated, false otherwise.
   */
  public static boolean isAuthenticated() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null && getAuthorities(authentication).noneMatch(
        AuthoritiesConstants.BS_ANONYMOUS::equals);
  }

  /**
   * Checks if the current user has any of the authorities.
   *
   * @param authorities the authorities to check.
   * @return true if the current user has any of the authorities, false otherwise.
   */
  public static boolean hasCurrentUserAnyOfAuthorities(String... authorities) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return (
        authentication != null && getAuthorities(authentication).anyMatch(
            authority -> Arrays.asList(authorities).contains(authority))
    );
  }

  /**
   * Checks if the current user has none of the authorities.
   *
   * @param authorities the authorities to check.
   * @return true if the current user has none of the authorities, false otherwise.
   */
  public static boolean hasCurrentUserNoneOfAuthorities(String... authorities) {
    return !hasCurrentUserAnyOfAuthorities(authorities);
  }

  /**
   * Checks if the current user has a specific authority.
   *
   * @param authority the authority to check.
   * @return true if the current user has the authority, false otherwise.
   */
  public static boolean hasCurrentUserThisAuthority(String authority) {
    return hasCurrentUserAnyOfAuthorities(authority);
  }

  private static Stream<String> getAuthorities(Authentication authentication) {
    Collection<? extends GrantedAuthority> authorities =
        authentication instanceof JwtAuthenticationToken
            ? extractAuthorityFromClaims(
            ((JwtAuthenticationToken) authentication).getToken().getClaims())
            : authentication.getAuthorities();
    return authorities.stream().map(GrantedAuthority::getAuthority);
  }

  public static List<GrantedAuthority> extractAuthorityFromClaims(Map<String, Object> claims) {
    return mapRolesToGrantedAuthorities(getRolesFromClaims(claims));
  }

  @SuppressWarnings("unchecked")
  private static Collection<String> getRolesFromClaims(Map<String, Object> claims) {

    Map resource_map = (Map) claims.getOrDefault(
        "resource_access",
        claims.getOrDefault("roles",
            claims.getOrDefault(CLAIMS_NAMESPACE + "roles", new HashMap<>()))
    );
    if (resource_map == null) {
      return new ArrayList<>();
    } else {
      return (Collection<String>) ((Map) resource_map.getOrDefault(getClientIDFromToken(claims),
          new HashMap<>())).getOrDefault("roles", new ArrayList<>());
    }

  }

  private static List<GrantedAuthority> mapRolesToGrantedAuthorities(Collection<String> roles) {
    return roles.stream().filter(role -> role.startsWith("BS_")).map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
  }

  public static String getIssuerCurrentUser() {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    Authentication authentication = securityContext.getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      return null;
    } else {
      Map<String, Object> tokenAttributes = authentication instanceof JwtAuthenticationToken
          ? ((JwtAuthenticationToken) authentication).getTokenAttributes()
          : authentication instanceof BearerTokenAuthentication
              ? ((BearerTokenAuthentication) authentication).getTokenAttributes()
              : new HashMap<>();
      return (String) tokenAttributes.get("iss");
    }
  }

  public static String getClientIDCurrentUser() {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    Authentication authentication = securityContext.getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      return null;
    } else {
      Map<String, Object> tokenAttributes = authentication instanceof JwtAuthenticationToken
          ? ((JwtAuthenticationToken) authentication).getTokenAttributes()
          : authentication instanceof BearerTokenAuthentication
              ? ((BearerTokenAuthentication) authentication).getTokenAttributes()
              : new HashMap<>();
      return (String) tokenAttributes.get("azp");
    }
  }

  public static String getClientIDFromToken(Map<String, Object> claims) {

    return (String) claims.getOrDefault("azp", "undefined");
  }

  public static String getPreferredUsernameCurrentUser() {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    Authentication authentication = securityContext.getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      return null;
    } else {
      Map<String, Object> tokenAttributes = authentication instanceof JwtAuthenticationToken
          ? ((JwtAuthenticationToken) authentication).getTokenAttributes()
          : authentication instanceof BearerTokenAuthentication
              ? ((BearerTokenAuthentication) authentication).getTokenAttributes()
              : new HashMap<>();
      return (String) tokenAttributes.get("preferred_username");
    }
  }

  public static RealmConfig getRealmFromConnectedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Map<String, Object> mapClaims = ((BearerTokenAuthentication) authentication).getTokenAttributes();
    String clientId = (String) mapClaims.get("azp");
    String fullUrl = (String) mapClaims.get("iss");
    String realm = fullUrl.substring(fullUrl.indexOf("realms/") + 7);
    String urlBase = fullUrl.substring(0, fullUrl.indexOf("/realms/"));
    return new RealmConfig(realm, fullUrl, urlBase, clientId);
  }
}
