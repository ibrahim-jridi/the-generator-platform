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

@AnalyzeClasses(packagesOf = BsUserManagementApp.class, importOptions = DoNotIncludeTests.class)
class TechnicalStructureTest {

    // prettier-ignore
    @ArchTest
    static final ArchRule respectsTechnicalArchitectureLayers = layeredArchitecture()
        .consideringAllDependencies()
        .layer("Config").definedBy("..config..")
        .layer("Web").definedBy("..web..")
        .optionalLayer("Service").definedBy("..service..")
        .optionalLayer("Interceptor").definedBy("..interceptors..")
        .optionalLayer("Management").definedBy("..management..")
        .optionalLayer("Aop").definedBy("..aop..")
        .optionalLayer("Validator").definedBy("..validator..")
        .optionalLayer("feignService").definedBy("..feignService..")
        .layer("Security").definedBy("..security..")
        .optionalLayer("Persistence").definedBy("..repository..")
        .layer("Domain").definedBy("..domain..")

        .whereLayer("Web").mayOnlyBeAccessedByLayers("Config", "Validator")
        .whereLayer("Service")
        .mayOnlyBeAccessedByLayers("Web", "Config", "Service", "feignService", "Validator", "Aop")
        .whereLayer("Validator").mayOnlyBeAccessedByLayers("Service")
        .whereLayer("Security").mayOnlyBeAccessedByLayers("Config", "Service", "Web", "Validator")
        .whereLayer("Persistence")
        .mayOnlyBeAccessedByLayers("Service", "Security", "Web", "Config", "Validator")
        .whereLayer("Domain")
        .mayOnlyBeAccessedByLayers("Persistence", "Service", "Security", "Web", "Config",
            "Validator", "Aop")
        .whereLayer("Config").mayOnlyBeAccessedByLayers("Web", "feignService")
        .whereLayer("feignService").mayOnlyBeAccessedByLayers("Web", "Config", "Service", "Aop")



        .ignoreDependency(belongToAnyOf(BsUserManagementApp.class), alwaysTrue())
        .ignoreDependency(alwaysTrue(), belongToAnyOf(
            Constants.class,
            ApplicationProperties.class
        ));
}
