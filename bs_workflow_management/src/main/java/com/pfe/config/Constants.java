package com.pfe.config;

import java.util.UUID;

/**
 * Application constants.
 */
public final class Constants {

    public static final String SYSTEM = "system";
    public static final String ROOT_USERID = "BS_ROOT";
    public static UUID ROOT_ID;
    public static final String USER_ID = "user_id";
    public static final String BS_ROOT = "BS_ROOT";
    public static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
    public static final String DEFAULT_ROLE = "BS_ROLE_DEFAULT_ROLE";
    public static final String FIRSTNAME_REGEX = "^[\\p{L}\\p{Z}]*$";
    public static final String FIRSTNAME_REGEX_ERROR_MSG = "400 BadRequest/invalid firstName input";
    public static final String LASTNAME_REGEX = "^[A-Za-z\\s]+$";
    public static final String TUNISIAN_PHONE_NUMBER_REGEX = "^\\+216[9527]\\d{7}$";
    public static final String LASTNAME_REGEX_ERROR_MSG = "400 BadRequest/invalid lastName input";
    public static final String EMAIL_REGEX_ERROR_MSG = "400 BadRequest/invalid email input";
    public static final String CIN_NATIONAL_ID_REGEX = "^\\d{8}$";
    public static final String TUNISIAN_NATIONALITY = "Tunisie";
    public static final String PASSPORT_REGEX = "^(?=(.*[a-zA-Z]){6,})[a-zA-Z0-9]*$";
    public static final String DATAE_FORMAT_REGEX = "^^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$";
    public static final String BS_ROLE_PM_AGENCE_PROMOTION = "BS_ROLE_PM_AGENCE_PROMOTION";
    public static final String BS_ROLE_PM_CRO = "BS_ROLE_PM_CRO";
    public static final String BS_ROLE_PP_PHARMACIEN = "BS_ROLE_PP_PHARMACIEN";
    public static final String BS_ROLE_PP_MEDECIN = "BS_ROLE_PP_MEDECIN";
    public static final String BS_ROLE_PP_MEDECIN_DENTISTE = "BS_ROLE_PP_MEDECIN_DENTISTE";
    public static final String BS_ROLE_PP_MEDECIN_VETERINAIRE = "BS_ROLE_PP_MEDECIN_VETERINAIRE";
    public static final String BS_ROLE_PM_SOCIETE_CONSULTING = "BS_ROLE_PM_SOCIETE_CONSULTING";
    public static final String BS_ROLE_RESP_AGENCE_PROMOT = "BS_ROLE_RESP_AGENCE_PROMOT";
    public static final String BS_ROLE_PM_REPRI_LABO = "BS_ROLE_PM_REPRI_LABO";
    public static final String BS_ROLE_PM_DELEGUEE_MEDICAL = "BS_ROLE_PM_DELEGUEE_MEDICAL";
    public static final String BS_ROLE_PM_VISITOR_MEDICAL = "BS_ROLE_PM_VISITOR_MEDICAL";
    public static final String BS_ROLE_PM_PHARMA_ECHANTILLONS = "BS_ROLE_PM_PHARMA_ECHANTILLONS";
    public static final String BS_PM_RESP_CRO = "BS_ROLE_PM_RESPO_CRO";
    public static final String BS_PM_INVISTG_CRO = "BS_ROLE_PM_RESPO_PROD_INVEST";
    public static final String BS_PM_TECH_IMPORT_EXPORT = "BS_ROLE_PM_RESP_TECH";
    public static final String BS_PM_LEGAL_IMPORT_EXPORT = "BS_ROLE_PM_REP_LEG";
    public static final String BS_ROLE_PM_AGENCE_PCT = "BS_ROLE_PM_AGENCE_PCT";
    public static final String BS_ROLE_PM_FILIALE_PCT = "BS_ROLE_PM_FILIALE_PCT";
    public static final String BS_ROLE_PM_PCT = "BS_ROLE_PM_PCT";
    public static final String BS_ROLE_PM_PHARMA_RESP_TECH ="BS_ROLE_PM_PHARMA_RESP_TECH";
    public static final String PERCENTAGE = "%";
    public static final String BS_ROLE_PM_LABO_LOCAL = "BS_ROLE_PM_LABO_LOC";
    public static final String BS_ROLE_PM_LABO_ETRANGER = "BS_ROLE_PM_LABO_ETRAN";
    public static final String BS_ROLE_ECTD ="BS_ROLE_ECTD";
    public static final String BS_ROLE_PM_INDUSTRIEL = "BS_ROLE_PM_INDUSTRIEL";
    public static final String BS_ROLE_PM_PHARMA_RESP_PROD = "BS_ROLE_PM_PHARMA_RESP_PROD";
    public static final String BS_ROLE_PM_RESP_CTRL_QA = "BS_ROLE_PM_RESP_CTRL_QA";
    public static final String BS_ROLE_PM_RESP_REG_AFF = "BS_ROLE_PM_RESP_REG_AFF";
    public static final String BS_ROLE_PM_GROSSISTE = "BS_ROLE_PM_GROSSISTE";
    public static final String BS_ROLE_PM_EXPORT_IMPORT = "BS_ROLE_PM_EXPORT_IMPORT";
    public static final String BS_ROLE_PM_TRANSITAIRE = "BS_ROLE_PM_TRANSITAIRE";
    public static final String BS_ROLE_PM_ASSOCIATION = "BS_ROLE_PM_ASSOCIATION";
    public static final String BS_ROLE_PM_STRUCT_DIPLOM = "BS_ROLE_PM_STRUCT_DIPLOM";
    public static final String BS_ROLE_PM_SOCIETE_PHARMA = "BS_ROLE_PM_SOCIETE_PHARMA";
    public static final String BS_ROLE_PM_DEFAULT = "BS_ROLE_PM_DEFAULT";


    public static UUID getRootId() {
        return ROOT_ID;
    }

    public static void setRootId(UUID rootId) {
        ROOT_ID = rootId;
    }

    private Constants() {
    }
}
