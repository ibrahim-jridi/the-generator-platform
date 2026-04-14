package com.pfe.domain;

import java.util.UUID;

public class ConfigurationReportTestSamples {

    public static ConfigurationReport getConfigurationReportSample1() {
        return new ConfigurationReport()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .name("name1")
            .address("address1")
            .postalCode("postalCode1")
            .phone("phone1")
            .fax("fax1")
            .email("email1")
            .logo("logo1")
            .footer("footer1");
    }

    public static ConfigurationReport getConfigurationReportSample2() {
        return new ConfigurationReport()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .name("name2")
            .address("address2")
            .postalCode("postalCode2")
            .phone("phone2")
            .fax("fax2")
            .email("email2")
            .logo("logo2")
            .footer("footer2");
    }

    public static ConfigurationReport getConfigurationReportRandomSampleGenerator() {
        return new ConfigurationReport()
            .id(UUID.randomUUID())
            .name(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .postalCode(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .fax(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .logo(UUID.randomUUID().toString())
            .footer(UUID.randomUUID().toString());
    }
}
