package com.pfe.config;

/**
 * Application constants.
 */
public final class Constants {

    public static final String SYSTEM = "system";
    public static final String ROOT_USERID = "BS_ROOT";
    public static final String USER_ID = "user_id";
    public static final String BS_ROOT = "BS_ROOT";
    public static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
    public static final String DEFAULT_ROLE = "BS_ROLE_DEFAULT_ROLE";
    public static final String FIRSTNAME_REGEX = "^[\\p{L}\\p{Z}]*$";
    public static final String FIRSTNAME_REGEX_ERROR_MSG = "400 BadRequest/invalid firstName input";
    public static final String LASTNAME_REGEX = "^[a-zA-Z\\u00C0-\\u017F\\s-]*$";
    public static final String TUNISIAN_PHONE_NUMBER_REGEX = "^\\+216[9527]\\d{7}$";
    public static final String LASTNAME_REGEX_ERROR_MSG = "400 BadRequest/invalid lastName input";
    public static final String EMAIL_REGEX_ERROR_MSG = "400 BadRequest/invalid email input";
    public static final String CIN_NATIONAL_ID_REGEX = "^\\d{8}$";
    public static final String TUNISIAN_NATIONALITY = "Tunisie";
    public static final String PASSPORT_REGEX = "^(?=.*\\d)[A-Z0-9]{6,9}$";
    public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\p{P}\\p{S}])[A-Za-z\\d\\p{P}\\p{S}]{8,}$";
    public static final String DATAE_FORMAT_REGEX = "^^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$";
    public static final String ADDRESS_REGEX = "^.{10,}$";
    public static final String BS_DESIGNATIONS_USERS_AUTHORITY ="BS_DESIGNATIONS_USERS";
    public static final String BS_ROLE_PM_AGENCE_PROMOTION = "BS_ROLE_PM_AGENCE_PROMOTION";
    public static final String ECTD_AUTHORITY = "BS_REDIRECTION_ECTD";
    public static final String BS_ROLE_ECTD = "BS_ROLE_ECTD";
    public static final String BS_ROLE_PM_INDUSTRIEL = "BS_ROLE_PM_INDUSTRIEL";
    public static final String BS_ROLE_PM_SOCIETE_CONSULTING = "BS_ROLE_PM_SOCIETE_CONSULTING";
    public static final String BS_ROLE_PM_LABO_LOC="BS_ROLE_PM_LABO_LOC";
    public static final String BS_ROLE_PM_LABO_ETRAN="BS_ROLE_PM_LABO_ETRAN";
    public static final String BS_ROLE_PM_PHARMA_RESP_TECH="BS_ROLE_PM_PHARMA_RESP_TECH";
    public static final String BS_ROLE_PM_RESP_CTRL_QA="BS_ROLE_PM_RESP_CTRL_QA";
    public static final String BS_ROLE_PM_PHARMA_RESP_PROD="BS_ROLE_PM_PHARMA_RESP_PROD";
    public static final String BS_ROLE_PM_RESP_REG_AFF="BS_ROLE_PM_RESP_REG_AFF";
    public static final String BS_ROLE_RESP_AGENCE_PROMOT = "BS_ROLE_RESP_AGENCE_PROMOT";
    public static final String BS_ROLE_PM_REPRI_LABO = "BS_ROLE_PM_REPRI_LABO";
    public static final String BS_ROLE_PM_DELEGUEE_MEDICAL = "BS_ROLE_PM_DELEGUEE_MEDICAL";
    public static final String BS_ROLE_PM_VISITOR_MEDICAL = "BS_ROLE_PM_VISITOR_MEDICAL";
    public static final String BS_ROLE_PM_PHARMA_ECHANTILLONS = "BS_ROLE_PM_PHARMA_ECHANTILLONS";

    private Constants() {
    }
}
