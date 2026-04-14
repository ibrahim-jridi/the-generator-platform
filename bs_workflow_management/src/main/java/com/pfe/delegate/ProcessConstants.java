package com.pfe.delegate;

public final class ProcessConstants {
    private ProcessConstants() {
    }

    // Process model identifiers
    public static final String AGENCE_PROMO_MODEL_LTS = "AgencePromo_Model_LTS";
    public static final String CRO_MODEL_LTS = "CRO_Model_LTS";
    public static final String IMPORT_EXPORT_MODEL_LTS = "IMPORT_EXPORT";
    public static final String AGENCE_PCT_MODEL_LTS = "AGENCE_PCT_MODEL_LTS";
    public static final String FILIALE_PCT_MODEL_LTS = "FILIALE_PCT_MODEL_LTS";
    public static final String CREATE_PCT_MODEL_LTS = "CREATE_PCT_MODEL_LTS";
    public static final String INDUSTRIE_HUMAIN_MODEL_LTS = "INDUSTRIE_HUMAIN_MODEL_LTS";
    public static final String INDUSTRIE_VETERINAIRE_MODEL_LTS = "INDUSTRIE_VETERINAIRE_MODEL_LTS";
    public static final String CREATION_GROSSISTE_MODEL_LTS = "CREATION_GROSSISTE_MODEL_LTS";
    public static final String CREATE_DESIGNATION_CREATE_PCT = "CREATE_DESIGNATION_CREATE_PCT";
    public static final String CREATE_DESIGNATION_FILIALE_PCT = "CREATE_DESIGNATION_FILIALE_PCT";
    public static final String CREATE_DESIGNATION_AGENCE_PCT = "CREATE_DESIGNATION_AGENCE_PCT";
    public static final String CREATE_DESIGNATION_CRO = "CREATE_DESIGNATION_CRO";
    public static final String CREATE_DESIGNATION_AGENCE_PROMO = "CREATE_DESIGNATION_AGENCE_PROMO";
    public static final String CREATE_DESIGNATION_CREATION_GROSSISTE = "CREATE_DESIGNATION_CREATION_GROSSISTE";

    // Lists
    public static final String LIST_OF_RESPONSABLE = "responsableAgence";
    public static final String LIST_OF_RESPONSABLE_LAB = "responsableAgenceLab";
    public static final String LIST_OF_DELEGUE_MEDICAL = "delegueMedical";
    public static final String LIST_OF_PHARMACIEN_VISITOR = "pharmacienVesiteur";
    public static final String LIST_OF_PHARMACIEN_ECHANTILLION = "pharmacienEchantillion";
    public static final String LIST_OF_PHARAMACY = "responsablePharmacieToCreate";
    public static final String LIST_OF_LOCAL = "responsableList";
    public static final String LIST_OF_STRANGER = "responsableEtrangerList";
    public static final String LIST_OF_DELEGATE = "assigneeList";
    public static final String LIST_OF_VISITOR = "visitorList";
    public static final String LIST_OF_RESPONSABLE_CRO = "responsableCROToCreate";
    public static final String LIST_OF_RESPONSABLE_INVESTIGATION_CRO = "responsableInvestigationCROToCreate";
    public static final String LIST_OF_RESPONSABLE_TECHNIQUE = "responsableTechniqueToCreate";
    public static final String LIST_OF_RESPONSABLE_LEGAL = "responsableLegaleToCreate";
    public static final String LIST_OF_RESPONSABLE_AGENCE_PCT = "responsableAgencePctToCreate";
    public static final String LIST_OF_RESPONSABLE_PCT = "responsablePctToCreate";
    public static final String LIST_OF_RESPONSABLE_FILIALE_PCT = "responsableFilialePctToCreate";
    public static final String LIST_OF_QUALITY_CONTROL_INDUSTRIE="qualityContolList";
    public static final String LIST_OF_PRODUCTION_OPERATION_INDUSTRIE = "productionOperationList";
    public static final String LIST_OF_PRT_INDUSTRIE="prtList";
    public static final String LIST_OF_RESPONSABLE_AFFAIRE_INDUSTRIE = "responsableAffaireList";
    public static final String LIST_OF_RESPONSABLE_CREATE_GROSSISTE = "responsablePharamacienGrossisteList";

    // Criteria
    public static final String CRITERE_RESPONSABLE_CRO ="critereResponsableCRO";
    public static final String CRITERE_RESPONSABLE_INVESTIGATION_CRO ="critereResponsableInvestigationCRO";
    public static final String CRITERE_VISITOR ="critereVisitor";
    public static final String CRITERE_DELEGATE ="critereDelegate";
    public static final String CRITERE_STRANGER ="critereStranger";
    public static final String CRITERE_LOCALE ="critereLocale";
    public static final String CRITERE_RESPONSABLE_AGENCE ="critereResponsable";
    public static final String CRITERE_PHARMACIE ="criterePharmacie";
    public static final String CRITERE_AGENCE_PCT ="critereAgencePCT";
    public static final String CRITERE_PCT ="criterePCT";
    public static final String CRITERE_FILIALE_PCT ="critereFilialePCT";
    public static final String CRITERE_PRT_INDUSTRIE ="criterePRTIndustrie";
    public static final String CRITERE_QUALITY_CONTROL_INDUSTRIE ="critereQualityControlIndustrie";
    public static final String CRITERE_PRODUCTION_OPERATION_INDUSTRIE ="critereProductionOperationIndustrie";
    public static final String CRITERE_RESPONSBALE_AFFAIRE_INDUSTRIE ="critereResponsableAffaireIndustrie";
    public static final String CRITERE_RESPONSABME_CREATE_GROSSISTE ="critereResponsableCreateGrossiste";

    //Decisions
    public static final String DECISION_RESPONSABLE_CRO = "decisionResponsableCRO";
    public static final String DECISION_RESPONSABLE_INVESTIGATION_CRO = "decisionResponsableInvestigationCRO";
    public static final String DECISION_RESPONSABLE_AGENCE = "decisionResponsable";
    public static final String DECISION_PHARAMACIE_AGENCE = "decisionPharmacie";
    public static final String DECISION_VISITEUR_VALEUR = "decisionVisiteurValeur";
    public static final String DECISION_LOCAL_VALEUR = "decisionLocaleValeur";
    public static final String DECISION_DELEGATE_VALEUR = "decisionDelegueValeur";
    public static final String DECISION_ETRANGER_VALEUR = "decisionEtrangerValeur";
    public static final String DECISION_RESPONSABLE_TECHNIQUE_IMPORT ="decisionResponsableTechnique";
    public static final String DECISION_RESPONSABLE_LEGAL_IMPORT = "decisionResponsableLegale";
    public static final String DECISION_RESPONSABLE_AGENCE_PCT = "decisionResponsableAgencePCT";
    public static final String DECISION_RESPONSABLE_FILIALE_PCT = "decisionResponsableFilialePCT";
    public static final String DECISION_RESPONSABLE_PCT = "decisionResponsablePCT";
    public static final String DECISION_PRT_INDUSTRIE = "DECISION_APPOINT_RESPONSIBLE_TECHNICAL_PHARMACIST";
    public static final String DECISION_RESPONSABLE_AFFAIRE_INDUSTRIE = "DECISION_APPOINT_REGULATORY_AFFAIRS_OFFICER";
    public static final String DECISION_QUALITY_CONTROL_INDUSTRIE = "DECISION_APPOINT_QUALITY_CONTROL_PHARMACIST";
    public static final String DECISION_PRODUCTION_OPERATION_INDUSTRIE = "DECISION_APPOINT_PRODUCTION_OPERATIONS_PHARMACIST";
    public static final String DECISION_RESPONSABLE_CREATE_GROSSISTE = "decisionResponsablePharamcienGrossiste";

    //Users
    public static final String RESPONSABLE_AGENCE_ID = "PROMOTION_AGENCY_RESPONSABLE_ID";
    public static final String RESPONSABLE_AGENCE_EMAIL = "E_MAIL_PROMOTION_AGENCY_RESPONSABLE";
    public static final String LOCAL_AGENCE_ID = "PROMOTION_AGENCY_RESPONSABLE_LAB_ID";
    public static final String LOCAL_AGENCE_EMAIL = "E_MAIL_PROMOTION_AGENCY_RESPONSABLE_LAB";
    public static final String STRANGER_AGENCE_ID = "PROMOTION_AGENCY_RESPONSABLE_LAB_ID2";
    public static final String STRANGER_AGENCE_EMAIL = "E_MAIL_PROMOTION_AGENCY_RESPONSABLE_LAB2";
    public static final String VISITOR_AGENCE_ID = "PHARMACEUTICAL_VISITOR_ID";
    public static final String VISITOR_AGENCE_EMAIL = "E_MAIl_PHARMACEUTICAL_VISITOR";
    public static final String DELEGATE_AGENCE_ID = "MEDICAL_DELEGATE_ID1";
    public static final String DELGATE_AGENCE_EMAIL = "E_MAIL_MEDICAL_DELEGATE1";
    public static final String PHARMACY_AGENCE_ID = "E_MAIl_PHARMACIST_RESPONSIBLE_HOLDING_SAMPLESID";
    public static final String PHARMACY_AGENCE_EMAIL = "E_MAIl_PHARMACIST_RESPONSIBLE_HOLDING_SAMPLES";
    public static final String RESPONSABLE_CRO_ID = "CRO_RESPONSABLE_ID";
    public static final String RESPONSABLE_CRO_EMAIL = "E_MAIL_CRO_RESPONSIBLE";
    public static final String RESPONSABLE_INVESTIGATION_CRO_ID = "CRO_RESPONSABLE_INVESTIGATION_ID";
    public static final String RESPONSABLE_INVESTIGATION_CRO_EMAIL = "E_MAIL_CRO_RESPONSIBLE_INVESTIGATION";
    public static final String RESPONSABLE_TECHNIQUE_IMPORT_EXPORT_ID = "TECHNICAL_MANAGER_UNIQUE_ID";
    public static final String RESPONSABLE_TECHNIQUE_IMPORT_EXPORT_EMAIL ="TECHNICAL_MANAGER_EMAIL_ADDRESS";
    public static final String REPRESENTANT_LEGAL_IMPORT_EXPORT_ID = "LEGAL_REPRESENTATIVE_UNIQUE_ID";
    public static final String REPRESENTANT_LEGAL_IMPORT_EXPORT_EMAIL = "LEGAL_REPRESENTATIVE_EMAIL_ADDRESS";
    public static final String REPRESENTANT_LEGAL_IMPORT_EXPORT_IDENTIFIER = "PLATFORM_IDENTIFIER";
    public static final String RESPONSABLE_AGENCE_PCT_ID = "PHARMACIST_RESPONSABLE_ID";
    public static final String RESPONSABLE_AGENCE_PCT_EMAIL = "E_MAIL_PHARMACIST_RESPONSIBLE";
    public static final String RESPONSABLE_PCT_ID = "PCT_RESPONSIBLE_PHARMACIST_ID";
    public static final String RESPONSABLE_PCT_EMAIL = "PCT_RESPONSIBLE_PHARMACIST_E_MAIL";
    public static final String RESPONSABLE_FILIALE_PCT_ID = "PCT_PHARMACIST_RESPONSABLE_ID";
    public static final String RESPONSABLE_FILIALE_PCT_EMAIL = "PCT_E_MAIL_PHARMACIST_RESPONSIBLE";
    public static final String COMPANY_NAME = "COMPANY_NAME";
    public static final String URL_WEB_SITE = "URL_WEB_SITE";
    public static final String IMPORT_EXPORT_PRODUCT_CATEGORY = "IMPORT_EXPORT_PRODUCT_CATEGORY";
    public static final String ANOTHER_CATEGORIES = "ANOTHER_CATEGORIES";
    public static final String QUALITY_CONTROL_INDUSTRIE_HUMAN_ID="QUALITY_CONTROL_PHARMACIST_ID";
    public static final String QUALITY_CONTROL_INDUSTRIE_HUMAN_EMAIL="QUALITY_CONTROL_PHARMACIST_EMAIL";
    public static final String PRODUCTION_OPERATION_INDUSTRIE_HUMAN_ID="PRODUCTION_OPERATIONS_PHARMACIST_ID";
    public static final String PRODUCTION_OPERATION_INDUSTRIE_HUMAN_EMAIL="EMAIL_ADDRESS_PRODUCTION_OPERATIONS";
    public static final String PRT_INDUSTRIE_HUMAN_ID = "TECHNICAL_RESPONSIBLE_PHARMACIST_ID";
    public static final String PRT_INDUSTRIE_HUMAN_EMAIL = "EMAIL_ADDRESS_PRT";
    public static final String RESPONSABLE_AFFAIRE_INDUSTRIE_HUMAN_ID="REGULATORY_REPRESENTATIVE_UNIQUE_ID";
    public static final String RESPONSABLE_AFFAIRE_INDUSTRIE_HUMAN_EMAIL="REGULATORY_REPRESENTATIVE_EMAIL_ADDRESS";
    public static final String RESPONSABLE_AFFAIRE_INDUSTRIE_HUMAN_IDENTIFIER="REGULATORY_REPRESENTATIVE_PLATFORM_IDENTIFIER";
    public static final String RESPONSBALE_PHARAMCIEN_CREATION_GROSSISTE_ID="WHOLESALER_RESPONSIBLE_PHARMACIST_ID";
    public static final String RESPONSBALE_PHARAMCIEN_CREATION_GROSSISTE_EMAIL="WHOLESALER_RESPONSIBLE_PHARMACIST_E_MAIL";



    //LOCAL LAB
    public static final String EMAIL_STRANGER_LAB = "E_MAIL_PROMOTION_AGENCY_LAB_FOREIGN";
    public static final String TAX_REGISTRATION_STRANGER_LAB = "FOREIGN_TAX_REGISTRATION_NUMBER";
    public static final String DENOMINATION_STRANGER_LAB = "LABORATORY_REPRESENT_FOREIGN";
    public static final String TRAD_NAME_STRANGER_LAB = "TRADE_NAME_FOREIGN";
    public static final String COMPANY_ADDRESS_STRANGER_LAB = "COMPANY_ADDRESS_FOREIGN";
    public static final String PRIMARY_ACTIVITY_STRANGER_LAB = "PRIMARY_ACTIVITY_FOREIGN";
    public static final String PRIMARY_ACTIVITY_CODE_STRANGER_LAB = "PRIMARY_ACTIVITY_CODE_FOREIGN";
    public static final String LEGAL_FORM_STRANGER_LAB = "LEGAL_FORM_FOREIGN";
    public static final String REGISTRATION_STATUS_STRANGER_LAB = "REGISTRATION_STATUS_FOREIGN";
    public static final String PHONE_NUMBER_STRANGER_LAB = "PHONE_NUMBER_FOREIGN";
    public static final String FILE_COMPANY_STATUS_STRANGER_LAB = "COMPANY_STATUS_FOREIGN";
    public static final String FILE_PATENTE_STRANGER_LAB = "PATENT_FILE_FOREIGN";
    public static final String LAB_LOCAL = "LOCAL_LAB_REPRESENTATIVE";
    public static final String LAB_STRANGER = "FOREIGN_LAB_REPRESENTATIVE";
    public static final String LIST_LAB_STRANGER_TO_CREATE = "listofLabStrangerToCreate";
    public static final String LIST_OF_LAB = "listOfLab";
    public static final String LIST_OF_LAB_LOCAL = "listOfLabLocal";
    public static final String LIST_OF_RESPONSABLE_LAB_LOC = "responsableAgenceLabLoc";
    public static final String LIST_OF_RESPONSABLE_LAB_ETR = "responsableAgenceLabEtr";

}
