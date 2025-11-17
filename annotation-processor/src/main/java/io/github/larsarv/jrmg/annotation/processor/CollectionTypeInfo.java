package io.github.larsarv.jrmg.annotation.processor;

import com.palantir.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.function.Function;

/**
 * TypeInfo implementation for collection types (List and Set) that may contain
 * either simple elements, lists, sets or nested mutable records. This class generates the
 * necessary mutator methods for collections, handling both simple and complex
 * nested mutation scenarios.
 */
public class CollectionTypeInfo extends SimpleTypeInfo implements TypeInfo {
    private final static ClassName FUNCTION_CLASS_NAME = ClassName.get(Function.class); // java.util.Function

    private final TypeInfo elementTypeInfo;
    private final ClassName mutatorInterfaceClassName;
    private final ClassName mutatorImplementationClassName; // Mutator implementation class
    private final ClassName mutatorFunctionClassName; // Function used by the mutate function as parameter

    /**
     * Constructs a CollectionTypeInfo with the given type information.
     *
     * @param typeName the TypeName of the collection component
     * @param elementTypeInfo the TypeInfo for the elements contained in the collection
     * @param mutatorInterfaceClassName the ClassName of the mutator interface for this collection
     * @param mutatorImplementationClassName the ClassName of the mutator implementation for this collection
     * @param mutatorFunctionClassName the ClassName of the function type used for mutation
     */
    public CollectionTypeInfo(
            TypeName typeName,
            TypeInfo elementTypeInfo,
            ClassName mutatorInterfaceClassName,
            ClassName mutatorImplementationClassName,
            ClassName mutatorFunctionClassName
    ) {
        super(typeName);
        this.elementTypeInfo = elementTypeInfo;
        this.mutatorInterfaceClassName = mutatorInterfaceClassName;
        this.mutatorImplementationClassName = mutatorImplementationClassName;
        this.mutatorFunctionClassName = mutatorFunctionClassName;
    }

    @Override
    public boolean hasMutator() {
        return true;
    }

    @Override
    public TypeName getMutatorInterfaceTypeName() {
        if (elementTypeInfo.hasMutator()) {
            return ParameterizedTypeName.get(
                    mutatorInterfaceClassName,
                    elementTypeInfo.getTypeName(),
                    elementTypeInfo.getMutatorInterfaceTypeName(),
                    elementTypeInfo.getMutatorInterfaceTypeName());
        } else {
            return ParameterizedTypeName.get(
                    mutatorInterfaceClassName,
                    elementTypeInfo.getTypeName());
        }
    }

    @Override
    public TypeName getFirstConstructorTypeName() {
        if (!elementTypeInfo.hasMutator()) {
            return null;
        }
        return ParameterizedTypeName.get(
                mutatorInterfaceClassName,
                elementTypeInfo.getTypeName(),
                elementTypeInfo.getFirstConstructorTypeName(),
                elementTypeInfo.getLastConstructorTypeName());
    }

    @Override
    public TypeName getLastConstructorTypeName() {
        if (!elementTypeInfo.hasMutator()) {
            return null;
        }
        return ParameterizedTypeName.get(
                mutatorInterfaceClassName,
                elementTypeInfo.getTypeName(),
                elementTypeInfo.getFirstConstructorTypeName(),
                elementTypeInfo.getLastConstructorTypeName());
    }

    /**
     * Returns the TypeName of the constructor interface for this collection type.
     *
     * @return the TypeName of the constructor interface
     */
    public TypeName getConstructorInterfaceTypeName() {
        if (elementTypeInfo.hasMutator()) {
            return ParameterizedTypeName.get(
                    mutatorInterfaceClassName,
                    elementTypeInfo.getTypeName(),
                    elementTypeInfo.getFirstConstructorTypeName(),
                    elementTypeInfo.getLastConstructorTypeName());
        } else {
            return ParameterizedTypeName.get(
                    mutatorInterfaceClassName,
                    elementTypeInfo.getTypeName());
        }
    }

    @Override
    public void contributeToMutator(
            TypeSpec.Builder mutatorClassBuilder,
            TypeName mutatorTypeName,
            String componentName,
            TypeName recordMutatorInterfaceTypeName
    ) {
        super.contributeToMutator(mutatorClassBuilder, mutatorTypeName, componentName, recordMutatorInterfaceTypeName);

        String fieldName = toFiledName(componentName);

        mutatorClassBuilder
                .addMethod(createMutateMethod(mutatorTypeName,fieldName, componentName, recordMutatorInterfaceTypeName))
                .addMethod(createSetMethod(mutatorTypeName, fieldName, componentName, recordMutatorInterfaceTypeName));

        if (elementTypeInfo.getFirstConstructorTypeName() != null) { // TODO Replace with hasMutator() when map mutator is fixed
            mutatorClassBuilder.addMethod(createConstructMethod(mutatorTypeName, fieldName, componentName, recordMutatorInterfaceTypeName));
        }

    }
    private MethodSpec createMutateMethod(TypeName mutatorTypeName, String fieldName, String componentName, TypeName recordMutatorInterfaceTypeName) {
        CodeBlock.Builder codeBlock = CodeBlock.builder();
        codeBlock.add("$T<$T,$T> factory = ", FUNCTION_CLASS_NAME, typeName, getMutatorInterfaceTypeName());
        addMutatorFactoryCode(codeBlock, 0);
        codeBlock
                .add(";\n")
                .addStatement("$T.this.$N = function.mutate(factory.apply(this.$N)).build()", mutatorTypeName, fieldName, fieldName)
                .addStatement("return this");

        return createImplementationMethod(
                "mutate",
                createMutatorFunctionParameterType(),
                codeBlock.build(),
                componentName,
                recordMutatorInterfaceTypeName);
    }

    private MethodSpec createSetMethod(TypeName mutatorTypeName, String fieldName, String componentName, TypeName recordMutatorInterfaceTypeName) {
        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();
        codeBlockBuilder.add("$T<$T,$T> factory = ", FUNCTION_CLASS_NAME, typeName, getMutatorInterfaceTypeName());
        addMutatorFactoryCode(codeBlockBuilder, 0);
        codeBlockBuilder
                .add(";\n")
                .addStatement("$T.this.$N = function.mutate(factory.apply(null)).build()",
                        mutatorTypeName,
                        fieldName)
                .addStatement("return this");

        return createImplementationMethod(
                "set",
                createMutatorFunctionParameterType(),
                codeBlockBuilder.build(),
                componentName,
                recordMutatorInterfaceTypeName
        );
    }
    private MethodSpec createConstructMethod(TypeName mutatorTypeName, String fieldName, String componentName, TypeName returnTypeName) {
        CodeBlock.Builder codeBlock = CodeBlock.builder();
        codeBlock.add("$T<$T,$T> factory = ", FUNCTION_CLASS_NAME, typeName, getConstructorInterfaceTypeName());
        addConstructorFactoryCode(codeBlock, 0);
        codeBlock
                .add(";\n")
                .addStatement("$T.this.$N = function.mutate(factory.apply(null)).build()", mutatorTypeName, fieldName)
                .addStatement("return this");

        return createImplementationMethod(
                "construct",
                createConstructorFunctionParameterType(),
                codeBlock.build(),
                componentName,
                returnTypeName);
    }

    private ParameterizedTypeName createMutatorFunctionParameterType() {
        if (elementTypeInfo.hasMutator()) {
            return ParameterizedTypeName.get(
                    mutatorFunctionClassName,
                    elementTypeInfo.getTypeName(),
                    elementTypeInfo.getMutatorInterfaceTypeName(),
                    elementTypeInfo.getMutatorInterfaceTypeName());
        } else {
            return ParameterizedTypeName.get(
                    mutatorFunctionClassName,
                    elementTypeInfo.getTypeName());
        }
    }

    private ParameterizedTypeName createConstructorFunctionParameterType() {
        if (elementTypeInfo.hasMutator()) {
            return ParameterizedTypeName.get(
                    mutatorFunctionClassName,
                    elementTypeInfo.getTypeName(),
                    elementTypeInfo.getFirstConstructorTypeName(),
                    elementTypeInfo.getLastConstructorTypeName());
        } else {
            return ParameterizedTypeName.get(
                    mutatorFunctionClassName,
                    elementTypeInfo.getTypeName());
        }
    }

    @Override
    public void addMutatorFactoryCode(CodeBlock.Builder codeBlockbuilder, int factoryMethodIndex) {
        codeBlockbuilder.add("\nelement$L -> $T.mutator(element$L, ", factoryMethodIndex, mutatorImplementationClassName, factoryMethodIndex);
        elementTypeInfo.addMutatorFactoryCode(codeBlockbuilder, factoryMethodIndex + 1);
        codeBlockbuilder.add(")");
    }
    @Override
    public void addConstructorFactoryCode(CodeBlock.Builder codeBlockbuilder, int factoryMethodIndex) {
        codeBlockbuilder.add("\nelement$L -> $T.mutator(element$L, ", factoryMethodIndex, mutatorImplementationClassName, factoryMethodIndex);
        elementTypeInfo.addConstructorFactoryCode(codeBlockbuilder, factoryMethodIndex + 1);
        codeBlockbuilder.add(")");
    }

    @Override
    public void contributeToConstructor(
            TypeSpec.Builder constructorClassBuilder,
            TypeSpec.Builder constructorInterfaceBuilder,
            TypeName mutatorTypeName,
            TypeName nextType,
            String componentName
    ) {
        super.contributeToConstructor(constructorClassBuilder, constructorInterfaceBuilder, mutatorTypeName, nextType,
                componentName);

        String fieldName = toFiledName(componentName);

        constructorClassBuilder
                .addMethod(createSetMethod(mutatorTypeName, fieldName, componentName, nextType));

        constructorInterfaceBuilder.addMethod(createInterfaceMethod(
                "set",
                createMutatorFunctionParameterType(),
                componentName,
                nextType
        ));

        if (elementTypeInfo.getFirstConstructorTypeName() != null) { // TODO Replace with hasMutator() when map mutator is fixed
            constructorClassBuilder
                    .addMethod(createConstructMethod(mutatorTypeName, fieldName, componentName, nextType));
    
            constructorInterfaceBuilder.addMethod(createInterfaceMethod(
                    "construct",
                    createConstructorFunctionParameterType(),
                    componentName,
                    nextType
            ));
        }
    }

    private MethodSpec createImplementationMethod(
            String methodNamePrefix,
            TypeName parameterTypeName,
            CodeBlock codeBlock,
            String componentName,
            TypeName returnTypeName
    ) {
        return MethodSpec.methodBuilder(toMethodName(methodNamePrefix, componentName))
                .addModifiers(Modifier.PUBLIC)
                .returns(returnTypeName)
                .addParameter(
                        parameterTypeName,
                        "function")
                .addCode(codeBlock)
                .build();
    }

    private MethodSpec createInterfaceMethod(
            String methodNamePrefix,
            TypeName parameterTypeName,
            String componentName,
            TypeName returnTypeName
    ) {
        return MethodSpec.methodBuilder(toMethodName(methodNamePrefix, componentName))
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(returnTypeName)
                .addParameter(
                        parameterTypeName,
                        "function")
                .build();
    }

}
