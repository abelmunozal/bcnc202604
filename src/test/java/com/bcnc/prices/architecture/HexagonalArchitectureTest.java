package com.bcnc.prices.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

/**
 * Executable enforcement of the Hexagonal / DDD dependency rule: the domain (and the
 * application layer) must stay free of framework and infrastructure dependencies.
 * These rules fail the build the moment a layering violation is introduced, so the
 * architecture is verified, not just documented.
 */
@AnalyzeClasses(packages = "com.bcnc.prices", importOptions = ImportOption.DoNotIncludeTests.class)
class HexagonalArchitectureTest {

    @ArchTest
    static final ArchRule domain_is_free_of_frameworks_and_outer_layers =
        noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAnyPackage(
                "..application..",
                "..infrastructure..",
                "org.springframework..",
                "jakarta.persistence..",
                "com.fasterxml..");

    @ArchTest
    static final ArchRule application_does_not_depend_on_infrastructure_or_web =
        noClasses()
            .that().resideInAPackage("..application..")
            .should().dependOnClassesThat().resideInAnyPackage(
                "..infrastructure..",
                "org.springframework.web..",
                "jakarta.persistence..");

    @ArchTest
    static final ArchRule layers_respect_the_dependency_rule =
        layeredArchitecture().consideringOnlyDependenciesInLayers()
            .layer("Domain").definedBy("..domain..")
            .layer("Application").definedBy("..application..")
            .layer("Infrastructure").definedBy("..infrastructure..")
            .whereLayer("Infrastructure").mayNotBeAccessedByAnyLayer()
            .whereLayer("Application").mayOnlyBeAccessedByLayers("Infrastructure");
}
