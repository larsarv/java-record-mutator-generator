package io.github.larsarv.jrmg.annotation.processor.type.manager;

import com.palantir.javapoet.*;
import io.github.larsarv.jrmg.api.Builder;

import javax.lang.model.element.Modifier;
import java.util.Locale;
import java.util.function.Function;

/**
 * TypeManager implementation for nested records that have their own generated builders (Mtor/Ctor).
 * <p>
 * This class generates methods to recursively mutate or construct nested records using their
 * generated builders.
 */
public class RecordWithBuilderTypeManager extends SimpleTypeManager implements TypeManager {
    private final TypeName recordMtorTypeName;
    private final ClassName recordMtorClassName;

    private final TypeName recordCtorTypeName;
    private final ClassName recordCtorClassName;

    private final String firstComponentName;

    /**
     * Constructs a new RecordWithBuilderTypeManager.
     *
     * @param typeName            the TypeName of the record
     * @param recordMtorTypeName  the TypeName of the record's mutator
     * @param recordMtorClassName the ClassName of the record's mutator
     * @param recordCtorTypeName  the TypeName of the record's constructor
     * @param recordCtorClassName the ClassName of the record's constructor
     * @param firstComponentName  the name of the first component of the record
     */
    public RecordWithBuilderTypeManager(
            TypeName typeName,
            TypeName recordMtorTypeName,
            ClassName recordMtorClassName,
            TypeName recordCtorTypeName,
            ClassName recordCtorClassName,
            String firstComponentName
    ) {
        super(typeName);
        this.recordMtorTypeName = recordMtorTypeName;
        this.recordMtorClassName = recordMtorClassName;
        this.recordCtorTypeName = recordCtorTypeName;
        this.recordCtorClassName = recordCtorClassName;
        this.firstComponentName = firstComponentName;
    }

    @Override
    public boolean hasMutator() {
        return recordMtorTypeName != null;
    }

    @Override
    public boolean hasConstructor() {
        return recordCtorTypeName != null;
    }

    @Override
    public TypeName getMutatorInterfaceTypeName() {
        return recordMtorTypeName;
    }

    @Override
    public TypeName getConstructorInterfaceTypeName() {
        return getConstructorSetterTypeName(firstComponentName);
    }

    @Override
    public void addMutatorFactoryCode(CodeBlock.Builder codeBlockbuilder, int factoryMethodIndex) {
        if (hasMutator()) {
            codeBlockbuilder.add("\nrecord$L -> $T.mutator(record$L)", factoryMethodIndex, recordMtorClassName, factoryMethodIndex);
        } else {
            codeBlockbuilder.add("null");
        }
    }

    @Override
    public void addConstructorFactoryCode(CodeBlock.Builder codeBlockbuilder, int factoryMethodIndex) {
        if (hasConstructor()) {
            codeBlockbuilder.add("element$L -> $T.constructor()", factoryMethodIndex, recordCtorClassName);
        } else {
            codeBlockbuilder.add("null");
        }
    }

    /**
     * Contributes methods to the mutator, including `mutate` and `construct` methods for nested records.
     *
     * @param mutatorClassBuilder the builder for the Mutator class
     * @param mutatorClassName    the name of the Mutator class
     * @param componentName       the name of the component
     * @param returnType          the return type for fluent methods
     */
    @Override
    public void contributeToMutator(
            TypeSpec.Builder mutatorClassBuilder,
            TypeName mutatorClassName,
            String componentName,
            TypeName returnType
    ) {
        super.contributeToMutator(mutatorClassBuilder, mutatorClassName, componentName, returnType);

        String fieldName = toFieldName(componentName);

        if (hasMutator()) {
            mutatorClassBuilder
                    .addMethod(createMutateFunction(mutatorClassName, componentName, returnType, fieldName))
                    .addMethod(createMutateSetMethod(mutatorClassName, returnType, componentName, fieldName));
        }

        if (hasConstructor()) {
            mutatorClassBuilder
                    .addMethod(createConstructMethod(mutatorClassName, returnType, componentName, fieldName));
        }
    }

    /**
     * Contributes methods to the constructor, including `construct` methods for nested records.
     *
     * @param constructorClassBuilder     the builder for the Constructor class
     * @param constructorInterfaceBuilder the builder for the Constructor interface
     * @param mutatorClassName            the name of the Mutator class
     * @param nextType                    the return type for the next step in the chain
     * @param componentName               the name of the component
     */
    @Override
    public void contributeToConstructor(
            TypeSpec.Builder constructorClassBuilder,
            TypeSpec.Builder constructorInterfaceBuilder,
            TypeName mutatorClassName,
            TypeName nextType,
            String componentName
    ) {
        super.contributeToConstructor(constructorClassBuilder, constructorInterfaceBuilder, mutatorClassName, nextType,
                componentName);


        if (hasConstructor()) {
            constructorClassBuilder
                    .addMethod(createConstructorConstructMethodImpl(mutatorClassName, componentName, nextType));

            constructorInterfaceBuilder
                    .addMethod(createInterfaceMethod(
                            "construct",
                            getConstructorSetterTypeName(firstComponentName),
                            getBuilderTypeName(),
                            nextType,
                            componentName));
        }
    }

    private MethodSpec createConstructorConstructMethodImpl(TypeName mutatorClassName, String componentName, TypeName nextType) {
        String fieldName = toFieldName(componentName);
        TypeName constructorSetterType = getConstructorSetterTypeName(firstComponentName);
        return createImplementationMethod(
                "construct",
                constructorSetterType,
                getBuilderTypeName(),
                CodeBlock.of("$T.this.$N = function.apply($T.constructor()).build()", mutatorClassName, fieldName, recordCtorClassName),
                componentName,
                nextType);
    }

    private MethodSpec createMutateFunction(TypeName mutatorClassName, String componentName, TypeName recordMutatorInterfaceTypeName, String fieldName) {
        return createImplementationMethod(
                "mutate",
                recordMtorTypeName,
                recordMtorTypeName,
                CodeBlock.of("$T.this.$N = function.apply($T.mutator(this.$N)).build()", mutatorClassName, fieldName, recordMtorClassName, fieldName),
                componentName,
                recordMutatorInterfaceTypeName);
    }

    private MethodSpec createMutateSetMethod(TypeName mutatorClassName, TypeName returnTypeName, String componentName, String fieldName) {
        CodeBlock codeBlock = CodeBlock.of("$T.this.$N = function.apply($T.mutator(null)).build()", mutatorClassName, fieldName, recordMtorClassName);
        return createImplementationMethod(
                "set",
                recordMtorTypeName,
                recordMtorTypeName,
                codeBlock,
                componentName,
                returnTypeName);
    }

    private MethodSpec createConstructMethod(TypeName mutatorClassName, TypeName returnTypeName, String componentName, String fieldName) {
        CodeBlock codeBlock = CodeBlock.of("$T.this.$N = function.apply($T.constructor()).build()", mutatorClassName, fieldName, recordCtorClassName);
        return createImplementationMethod(
                "construct",
                getConstructorSetterTypeName(firstComponentName),
                getBuilderTypeName(),
                codeBlock,
                componentName,
                returnTypeName);
    }

    private MethodSpec createImplementationMethod(
            String methodNamePrefix,
            TypeName functionArgumentTypeName,
            TypeName functionReturnTypeName,
            CodeBlock codeBlock,
            String componentName,
            TypeName returnTypeName
    ) {
        return MethodSpec.methodBuilder(toMethodName(methodNamePrefix, componentName))
                .addModifiers(Modifier.PUBLIC)
                .returns(returnTypeName)
                .addParameter(
                        ParameterizedTypeName.get(
                                ClassName.get(Function.class),
                                functionArgumentTypeName,
                                functionReturnTypeName),
                        "function")
                .addStatement(codeBlock)
                .addStatement("return this")
                .build();
    }

    private MethodSpec createInterfaceMethod(
            String methodName,
            TypeName functionArgumentTypeName,
            TypeName functionReturnTypeName,
            TypeName nextType,
            String componentName
    ) {
        return MethodSpec.methodBuilder(toMethodName(methodName, componentName))
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(nextType)
                .addParameter(
                        ParameterizedTypeName.get(
                                ClassName.get(Function.class),
                                functionArgumentTypeName,
                                functionReturnTypeName),
                        "function")
                .build();
    }

    private TypeName getConstructorSetterTypeName(String componentName) {
        if (componentName == null) {
            return getBuilderTypeName();
        }
        return recordCtorClassName.nestedClass(componentName.substring(0, 1).toUpperCase(Locale.ROOT)
                + componentName.substring(1)
                + "ConstructorSetter");
    }

    private TypeName getBuilderTypeName() {
        return ParameterizedTypeName.get(ClassName.get(Builder.class), typeName);
    }

}
