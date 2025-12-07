package io.github.larsarv.jrmg.annotation.processor;

import com.palantir.javapoet.*;
import io.github.larsarv.jrmg.annotation.processor.type.manager.TypeManagerFactory;
import io.github.larsarv.jrmg.api.Builder;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.RecordComponentElement;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Generator for creating "Ctor" (Constructor) classes for records.
 * <p>
 * The generated Ctor class provides a fluent API for constructing new instances of the record,
 * enforcing that all fields are set in the order of their declaration.
 */
public class CtorGenerator {
    private final TypeManagerFactory typeManagerFactory;
    private final GeneratorUtils utils;

    /**
     * Constructs a new CtorGenerator.
     *
     * @param processingEnv      the processing environment
     * @param typeManagerFactory the factory for creating TypeManagers
     */
    public CtorGenerator(ProcessingEnvironment processingEnv, TypeManagerFactory typeManagerFactory) {
        this.typeManagerFactory = typeManagerFactory;
        this.utils = new GeneratorUtils(processingEnv);
    }

    /**
     * Generates the Ctor class for the given record element.
     *
     * @param recordElement the record to generate a Ctor for
     */
    public void process(TypeElement recordElement) {
        String recordElementPackageName = utils.getPackageName(recordElement);
        ClassName ctorClassName = ClassName.get(recordElementPackageName, recordElement.getSimpleName() + "Ctor");
        ClassName recordClassName = ClassName.get(recordElement);

        TypeSpec.Builder ctorClassBuilder = TypeSpec.classBuilder(ctorClassName)
                .addModifiers(Modifier.PUBLIC);

        addConstructor(ctorClassBuilder, recordElement);
        addConstructorComponentMethods(ctorClassBuilder, recordElement, ctorClassName);
        addFactoryMethod(ctorClassBuilder, recordElement, ctorClassName);

        utils.writeJavaFile(recordElementPackageName, ctorClassBuilder.build(), recordElement);
    }

    private void addConstructor(TypeSpec.Builder ctorClassBuilder, TypeElement recordElement) {
        ctorClassBuilder.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .build());
    }

    private void addConstructorComponentMethods(
            TypeSpec.Builder ctorClassBuilder,
            TypeElement recordElement,
            ClassName ctorClassName
    ) {
        TypeSpec.Builder builderClassBuilder = TypeSpec.classBuilder("BuilderImpl")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC);

        utils.addFields(builderClassBuilder, recordElement);

        TypeName nestedTypeName = TypeName.get(recordElement.asType());
        TypeName prevType = ParameterizedTypeName.get(ClassName.get(Builder.class), nestedTypeName);

        builderClassBuilder.addSuperinterface(prevType);

        // Add build method to BuilderImpl using its own fields
        List<String> builderFieldNameList = utils.createFieldNameList(recordElement);
        builderClassBuilder.addMethod(MethodSpec.methodBuilder("build")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.get(recordElement.asType()))
                .addStatement("return new $T($L)", ClassName.get(recordElement), String.join(", ", builderFieldNameList))
                .build());

        // Iterate through components in reverse order to build the chain
        List<RecordComponentElement> reverseComponentList = new ArrayList<>(recordElement.getRecordComponents());
        Collections.reverse(reverseComponentList);

        for (RecordComponentElement recordComponentElement : reverseComponentList) {
            String componentName = recordComponentElement.getSimpleName().toString();
            TypeSpec.Builder constructorInterfaceBuilder = TypeSpec.interfaceBuilder(toConstructorInterfaceName(componentName))
                    .addModifiers(Modifier.PUBLIC);

            typeManagerFactory.createTypeManager(recordComponentElement.asType())
                    .contributeToConstructor(
                            builderClassBuilder,
                            constructorInterfaceBuilder,
                            ctorClassName.nestedClass("BuilderImpl"),
                            prevType,
                            componentName);

            TypeSpec setterInterface = constructorInterfaceBuilder.build();

            prevType = ClassName.get(ctorClassName.packageName(), ctorClassName.simpleName(), setterInterface.name());
            builderClassBuilder.addSuperinterface(prevType);
            ctorClassBuilder.addType(setterInterface);
        }

        ctorClassBuilder.addType(builderClassBuilder.build());
    }

    private String toConstructorInterfaceName(String componentName) {
        return componentName.substring(0, 1).toUpperCase(Locale.ROOT)
                + componentName.substring(1)
                + "ConstructorSetter";
    }

    private void addFactoryMethod(
            TypeSpec.Builder ctorClassBuilder,
            TypeElement recordElement,
            ClassName ctorClassName
    ) {
        String firstComponentName = getFirstComponentName(recordElement);
        if (firstComponentName != null) {
            ctorClassBuilder.addMethod(MethodSpec.methodBuilder("constructor")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(ctorClassName.nestedClass(toConstructorInterfaceName(firstComponentName)))
                    .addStatement("return new $T()", ctorClassName.nestedClass("BuilderImpl"))
                    .build());
        } else {
            ctorClassBuilder.addMethod(MethodSpec.methodBuilder("constructor")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(ParameterizedTypeName.get(ClassName.get(Builder.class), TypeName.get(recordElement.asType())))
                    .addStatement("return new $T()", ctorClassName.nestedClass("BuilderImpl"))
                    .build());
        }
    }

    private static String getFirstComponentName(TypeElement recordElement) {
        if (recordElement.getRecordComponents().isEmpty()) {
            return null;
        }
        return recordElement.getRecordComponents().get(0).getSimpleName().toString();
    }

}