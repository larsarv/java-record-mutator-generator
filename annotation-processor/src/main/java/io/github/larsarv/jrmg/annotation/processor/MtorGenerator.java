package io.github.larsarv.jrmg.annotation.processor;

import com.palantir.javapoet.*;
import io.github.larsarv.jrmg.annotation.processor.type.manager.TypeManager;
import io.github.larsarv.jrmg.annotation.processor.type.manager.TypeManagerFactory;
import io.github.larsarv.jrmg.api.Builder;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.RecordComponentElement;
import javax.lang.model.element.TypeElement;
import java.util.List;

/**
 * Generator for creating "Mtor" (Mutator) classes for records.
 * <p>
 * The generated Mtor class provides a fluent API for modifying existing instances of the record,
 * allowing for deep mutations of nested structures.
 */
public class MtorGenerator {
    private final TypeManagerFactory typeManagerFactory;
    private final GeneratorUtils utils;

    /**
     * Constructs a new MtorGenerator.
     *
     * @param processingEnv      the processing environment
     * @param typeManagerFactory the factory for creating TypeManagers
     */
    public MtorGenerator(ProcessingEnvironment processingEnv, TypeManagerFactory typeManagerFactory) {
        this.typeManagerFactory = typeManagerFactory;
        this.utils = new GeneratorUtils(processingEnv);
    }

    /**
     * Generates the Mtor class for the given record element.
     *
     * @param recordElement the record to generate a Mtor for
     */
    public void process(TypeElement recordElement) {
        String recordElementPackageName = utils.getPackageName(recordElement);
        ClassName mutatorClassName = ClassName.get(recordElementPackageName, recordElement.getSimpleName() + "Mtor");
        ClassName recordClassName = ClassName.get(recordElement);

        TypeSpec.Builder mutatorClassBuilder = TypeSpec.classBuilder(mutatorClassName)
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(Builder.class), recordClassName))
                .addModifiers(Modifier.PUBLIC);

        utils.addFields(mutatorClassBuilder, recordElement);
        addConstructor(mutatorClassBuilder, recordElement, recordClassName);
        addMutatorComponentMethods(mutatorClassBuilder, recordElement, mutatorClassName);
        addFactoryMethods(mutatorClassBuilder, recordElement, mutatorClassName, recordClassName);
        addBuildMethod(recordElement, mutatorClassBuilder, recordClassName);

        utils.writeJavaFile(recordElementPackageName, mutatorClassBuilder.build(), recordElement);
    }


    private void addMutatorComponentMethods(TypeSpec.Builder mutatorClassBuilder, TypeElement recordElement, ClassName mutatorClassName) {
        for (RecordComponentElement recordComponentElement : recordElement.getRecordComponents()) {
            String componentName = recordComponentElement.getSimpleName().toString();

            TypeManager typeManager = typeManagerFactory.createTypeManager(recordComponentElement.asType());
            typeManager.contributeToMutator(mutatorClassBuilder, mutatorClassName, componentName, mutatorClassName);
        }
    }

    private void addFactoryMethods(
            TypeSpec.Builder mutatorClassBuilder,
            TypeElement recordElement,
            ClassName mutatorClassName,
            ClassName recordClassName
    ) {
        // No argument factory method
        mutatorClassBuilder.addMethod(MethodSpec.methodBuilder("mutator")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(mutatorClassName)
                .addStatement("return new $T(null)", mutatorClassName)
                .build());

        // Factory method
        mutatorClassBuilder.addMethod(MethodSpec.methodBuilder("mutator")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(recordClassName, "value")
                .returns(mutatorClassName)
                .addStatement("return new $T(value)", mutatorClassName)
                .build());
    }

    private void addBuildMethod(
            TypeElement recordElement,
            TypeSpec.Builder mutatorClassBuilder,
            ClassName recordClassName
    ) {
        List<String> fieldNameList = utils.createFieldNameList(recordElement);
        mutatorClassBuilder.addMethod(MethodSpec.methodBuilder("build")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(recordClassName)
                .addStatement("return new $T($L)", recordClassName, String.join(", ", fieldNameList))
                .build());
    }

    private void addConstructor(
            TypeSpec.Builder mutatorClassBuilder,
            TypeElement recordElement,
            ClassName recordClassName
    ) {
        CodeBlock.Builder constructorCodeBuilder = CodeBlock.builder();
        constructorCodeBuilder.beginControlFlow("if (value != null)");
        for (RecordComponentElement recordComponentElement : recordElement.getRecordComponents()) {
            String componentName = recordComponentElement.getSimpleName().toString();
            String fieldName = utils.toFieldName(componentName);

            constructorCodeBuilder.addStatement("this.$N = value.$N()", fieldName, componentName);
        }
        constructorCodeBuilder.endControlFlow();

        mutatorClassBuilder.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addParameter(recordClassName, "value")
                .addCode(constructorCodeBuilder.build())
                .build());
    }

}