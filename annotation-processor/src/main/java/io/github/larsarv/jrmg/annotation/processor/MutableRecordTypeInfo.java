package io.github.larsarv.jrmg.annotation.processor;

import com.palantir.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.Locale;
import java.util.function.Function;

/**
 * TypeInfo implementation for record types that are annotated with GenerateMutator.
 * This class generates not only basic setter and getter methods but also a mutate method
 * that allows for nested mutation of the record component.
 */
public class MutableRecordTypeInfo extends SimpleTypeInfo implements TypeInfo {
    private final TypeName recordMutatorTypeName;
    private final ClassName recordMutatorClassName;
    private final String firstComponentName;
    /**
     * Constructs a MutableRecordTypeInfo with the given type name and mutator information.
     *
     * @param typeName               the TypeName of the record component
     * @param recordMutatorTypeName  the TypeName of the mutator interface for this record
     * @param recordMutatorClassName the ClassName of the mutator implementation for this record
     * @param firstComponentName       the name of the first component in the record, null if the
     *                               record has no components.
     */
    public MutableRecordTypeInfo(TypeName typeName, TypeName recordMutatorTypeName, ClassName recordMutatorClassName, String firstComponentName) {
        super(typeName);
        this.recordMutatorTypeName = recordMutatorTypeName;
        this.recordMutatorClassName = recordMutatorClassName;
        this.firstComponentName = firstComponentName;
    }

    @Override
    public boolean hasMutator() {
        return true;
    }

    @Override
    public TypeName getMutatorInterfaceTypeName() {
        return recordMutatorTypeName;
    }

    @Override
    public TypeName getFirstConstructorTypeName() {
        return recordMutatorClassName.nestedClass(toConstructorSetterName(firstComponentName));
    }

    @Override
    public TypeName getLastConstructorTypeName() {
        return recordMutatorClassName.nestedClass("ConstructorDone");
    }

    @Override
    public void addMutatorFactoryCode(CodeBlock.Builder codeBlockbuilder, int factoryMethodIndex) {
        codeBlockbuilder.add("\nrecord$L -> $T.mutator(record$L)", factoryMethodIndex, recordMutatorClassName, factoryMethodIndex);
    }
    @Override
    public void addConstructorFactoryCode(CodeBlock.Builder codeBlockbuilder, int factoryMethodIndex) {
        codeBlockbuilder.add("\nrecord$L -> $T.constructor()", factoryMethodIndex, recordMutatorClassName);
    }

    @Override
    public void contributeToMutator(
            TypeSpec.Builder mutatorClassBuilder,
            TypeName mutatorClassName,
            String componentName,
            TypeName recordMutatorInterfaceTypeName
    ) {
        super.contributeToMutator(mutatorClassBuilder, mutatorClassName, componentName, recordMutatorInterfaceTypeName);

        String fieldName = toFiledName(componentName);

        mutatorClassBuilder
                .addMethod(createMutateFunction(mutatorClassName, componentName, recordMutatorInterfaceTypeName, fieldName))
                .addMethod(createSetMethod(mutatorClassName, recordMutatorInterfaceTypeName, componentName, fieldName))
                .addMethod(createConstructMethod(mutatorClassName, componentName, recordMutatorInterfaceTypeName, fieldName));
    }

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

        String fieldName = toFiledName(componentName);

        constructorClassBuilder
                .addMethod(createSetMethod(mutatorClassName, nextType, componentName, fieldName))
                .addMethod(createConstructMethod(mutatorClassName, componentName, nextType, fieldName));

        constructorInterfaceBuilder
                .addMethod(createInterfaceMethod(
                        "set",
                        recordMutatorClassName,
                        recordMutatorClassName,
                        nextType,
                        componentName))
                .addMethod(createInterfaceMethod(
                        "construct",
                        recordMutatorClassName.nestedClass(toConstructorSetterName(firstComponentName)),
                        recordMutatorClassName.nestedClass("ConstructorDone"),
                        nextType,
                        componentName));
    }

    private MethodSpec createMutateFunction(TypeName mutatorClassName, String componentName, TypeName recordMutatorInterfaceTypeName, String fieldName) {
        return createImplementationMethod(
                "mutate",
                recordMutatorTypeName,
                recordMutatorTypeName,
                CodeBlock.of("$T.this.$N = function.apply($T.mutator(this.$N)).build()", mutatorClassName, fieldName, recordMutatorClassName, fieldName),
                componentName,
                recordMutatorInterfaceTypeName);
    }

    private MethodSpec createSetMethod(TypeName mutatorClassName, TypeName nextType, String componentName, String fieldName) {
        return createImplementationMethod(
                "set",
                recordMutatorTypeName,
                recordMutatorTypeName,
                CodeBlock.of("$T.this.$N = function.apply($T.mutator(null)).build()", mutatorClassName, fieldName, recordMutatorClassName),
                componentName,
                nextType);
    }

    private MethodSpec createConstructMethod(TypeName mutatorClassName, String componentName, TypeName recordMutatorInterfaceTypeName, String fieldName) {
        return createImplementationMethod(
                "construct",
                recordMutatorClassName.nestedClass(toConstructorSetterName(firstComponentName)),
                recordMutatorClassName.nestedClass("ConstructorDone"),
                CodeBlock.of("$T.this.$N = $T.mutator(null).construct(function).build()", mutatorClassName, fieldName, recordMutatorClassName),
                componentName,
                recordMutatorInterfaceTypeName);
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

    private String toConstructorSetterName(String firstComponentName) {
        if (firstComponentName == null) {
            return "ConstructorDone";
        }
        return firstComponentName.substring(0, 1).toUpperCase(Locale.ROOT)
                + firstComponentName.substring(1)
                + "ConstructorSetter";
    }

}
