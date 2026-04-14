package com.pfe;

import static com.tngtech.archunit.base.DescribedPredicate.alwaysTrue;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.belongToAnyOf;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.pfe.config.ApplicationProperties;
import com.pfe.config.Constants;
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packagesOf = ReportManagementApp.class, importOptions = DoNotIncludeTests.class)
class TechnicalStructureTest {

    // prettier-ignore
    @ArchTest
    static final ArchRule respectsTechnicalArchitectureLayers = layeredArchitecture()
        .consideringAllDependencies()
        .layer("Config").definedBy("..config..")
        .layer("Web").definedBy("..web..")
        .layer("Validator").definedBy("..validator..")
        .optionalLayer("Service").definedBy("..service..")
        .layer("Security").definedBy("..security..")
        .optionalLayer("Persistence").definedBy("..repository..")
        .layer("Domain").definedBy("..domain..")
        .layer("Interceptors").definedBy("..interceptors..")
        .layer("Aop").definedBy("..aop..")
        .layer("FeignServices").definedBy("..feignServices..")


        .whereLayer("Config").mayOnlyBeAccessedByLayers("Service","Aop","FeignServices")
        .whereLayer("Web").mayOnlyBeAccessedByLayers("Config","Validator")
        .whereLayer("Service").mayOnlyBeAccessedByLayers("Web", "Config","Validator","Aop")
        .whereLayer("Security").mayOnlyBeAccessedByLayers("Config", "Service", "Web")
        .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Service", "Security", "Web", "Config","Validator")
        .whereLayer("Domain").mayOnlyBeAccessedByLayers("Persistence", "Service", "Security", "Web", "Config")
        .whereLayer("Validator").mayOnlyBeAccessedByLayers("Service")
        .whereLayer("Aop").mayOnlyBeAccessedByLayers("Service","Config")
        .whereLayer("Interceptors").mayOnlyBeAccessedByLayers("Aop")
        .whereLayer("FeignServices").mayOnlyBeAccessedByLayers("Service","Config")


        .ignoreDependency(belongToAnyOf(ReportManagementApp.class), alwaysTrue())
        .ignoreDependency(alwaysTrue(), belongToAnyOf(
            Constants.class,
            ApplicationProperties.class
        ));
}
