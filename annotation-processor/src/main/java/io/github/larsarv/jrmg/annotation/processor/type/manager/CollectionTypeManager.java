package io.github.larsarv.jrmg.annotation.processor.type.manager;

import com.palantir.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.function.Function;

/**
 * TypeManager implementation for collection types (List, Set).
 * <p>
 * This class generates methods to mutate or construct collections, supporting both simple elements
 * and nested mutable elements.
 */
public class CollectionTypeManager extends SimpleTypeManager implements TypeManager {
    /**
     * The ClassName for java.util.Function.
     */
    protected final static ClassName FUNCTION_CLASS_NAME = ClassName.get(Function.class); // java.util.Function

    /**
     * The TypeManager for the elements of the collection.
     */
    protected final TypeManager elementTypeManager;
    /**
     * The ClassName for the mutator interface.
     */
    protected final ClassName mtorInterfaceClassName;
    /**
     * The ClassName for the mutator function interface.
     */
    protected final ClassName mtorFunctionClassName;
    /**
     * The ClassName for the constructor interface.
     */
    protected final ClassName ctorInterfaceClassName;
    /**
     * The ClassName for the constructor function interface.
     */
    protected final ClassName ctorFunctionClassName;
    /**
     * The ClassName for the builder implementation.
     */
    protected final ClassName builderImplementationClassName;

    /**
     * Constructs a new CollectionTypeManager.
     *
     * @param typeName                       the TypeName of the collection
     * @param elementTypeManager             the TypeManager for the elements
     * @param mtorInterfaceClassName         the ClassName for the mutator interface
     * @param mtorFunctionClassName          the ClassName for the mutator function interface
     * @param ctorInterfaceClassName         the ClassName for the constructor interface
     * @param ctorFunctionClassName          the ClassName for the constructor function interface
     * @param builderImplementationClassName the ClassName for the builder implementation
     */
    public CollectionTypeManager(
            TypeName typeName,
            TypeManager elementTypeManager,
            ClassName mtorInterfaceClassName,
            ClassName mtorFunctionClassName,
            ClassName ctorInterfaceClassName,
            ClassName ctorFunctionClassName,
            ClassName builderImplementationClassName
    ) {
        super(typeName);
        this.elementTypeManager = elementTypeManager;
        this.mtorInterfaceClassName = mtorInterfaceClassName;
        this.mtorFunctionClassName = mtorFunctionClassName;
        this.ctorInterfaceClassName = ctorInterfaceClassName;
        this.ctorFunctionClassName = ctorFunctionClassName;
        this.builderImplementationClassName = builderImplementationClassName;
    }

    @Override
    public boolean hasMutator() {
        return true;
    }

    @Override
    public boolean hasConstructor() {
        return true;
    }

    @Override
    public TypeName getMutatorInterfaceTypeName() {
        if (elementTypeManager.hasMutator()) {
            return ParameterizedTypeName.get(
                    mtorInterfaceClassName,
                    elementTypeManager.getTypeName(),
                    elementTypeManager.getMutatorInterfaceTypeName());
        } else {
            return ParameterizedTypeName.get(
                    mtorInterfaceClassName,
                    elementTypeManager.getTypeName());
        }
    }

    @Override
    public TypeName getConstructorInterfaceTypeName() {
        if (elementTypeManager.hasConstructor()) {
            TypeName firstConstructorType = elementTypeManager.getConstructorInterfaceTypeName();
            return ParameterizedTypeName.get(
                    ctorInterfaceClassName,
                    elementTypeManager.getTypeName(),
                    firstConstructorType);
        } else {
            return ParameterizedTypeName.get(
                    ctorInterfaceClassName,
                    elementTypeManager.getTypeName());
        }
    }

    /**
     * Contributes methods to the mutator, including `mutate` and `construct` methods for collections.
     *
     * @param mutatorClassBuilder            the builder for the Mutator class
     * @param mutatorTypeName                the name of the Mutator class
     * @param componentName                  the name of the component
     * @param recordMutatorInterfaceTypeName the return type for fluent methods
     */
    @Override
    public void contributeToMutator(
            TypeSpec.Builder mutatorClassBuilder,
            TypeName mutatorTypeName,
            String componentName,
            TypeName recordMutatorInterfaceTypeName
    ) {
        super.contributeToMutator(mutatorClassBuilder, mutatorTypeName, componentName, recordMutatorInterfaceTypeName);

        String fieldName = toFieldName(componentName);

        mutatorClassBuilder
                .addMethod(createMutateMethod(mutatorTypeName, fieldName, componentName, recordMutatorInterfaceTypeName))
                .addMethod(createMutatorSetMethod(mutatorTypeName, fieldName, componentName, recordMutatorInterfaceTypeName));

        if (elementTypeManager.hasConstructor()) {
            mutatorClassBuilder.addMethod(createConstructMethod(mutatorTypeName, componentName, recordMutatorInterfaceTypeName));
        }
    }


    /**
     * Contributes methods to the constructor, including `construct` methods for collections.
     *
     * @param constructorClassBuilder     the builder for the Constructor class
     * @param constructorInterfaceBuilder the builder for the Constructor interface
     * @param mutatorTypeName             the name of the Mutator class
     * @param nextType                    the return type for the next step in the chain
     * @param componentName               the name of the component
     */
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

        String fieldName = toFieldName(componentName);

        constructorClassBuilder
                .addMethod(createConstructorConstructMethodImpl(mutatorTypeName, fieldName, componentName, nextType));

        constructorInterfaceBuilder
                .addMethod(createInterfaceMethod(
                        "construct",
                        createConstructorFunctionParameterType(),
                        componentName,
                        nextType
                ));
    }

    private MethodSpec createMutateMethod(TypeName mutatorTypeName, String fieldName, String componentName, TypeName recordMutatorInterfaceTypeName) {
        CodeBlock.Builder codeBlock = CodeBlock.builder();
        codeBlock.add("$T<$T,$T> factory = ", FUNCTION_CLASS_NAME, typeName, getMutatorInterfaceTypeName());
        addMutatorFactoryCode(codeBlock, 0);
        codeBlock
                .add(";\n")
                .addStatement("$T.this.$N = function.apply(factory.apply(this.$N)).build()", mutatorTypeName, fieldName, fieldName)
                .addStatement("return this");

        return createImplementationMethod(
                "mutate",
                createMutatorFunctionParameterType(),
                codeBlock.build(),
                componentName,
                recordMutatorInterfaceTypeName);
    }

    private MethodSpec createMutatorSetMethod(TypeName mutatorTypeName, String fieldName, String componentName, TypeName returnTypeName) {
        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();

        codeBlockBuilder.add("$T<$T,$T> factory = ", FUNCTION_CLASS_NAME, typeName, getMutatorInterfaceTypeName());
        addMutatorFactoryCode(codeBlockBuilder, 0);
        codeBlockBuilder
                .add(";\n")
                .addStatement("$T.this.$N = function.apply(factory.apply(null)).build()",
                        mutatorTypeName,
                        fieldName);

        codeBlockBuilder.addStatement("return this");

        return createImplementationMethod(
                "set",
                createMutatorFunctionParameterType(),
                codeBlockBuilder.build(),
                componentName,
                returnTypeName
        );
    }

    private MethodSpec createConstructMethod(TypeName mutatorTypeName, String componentName, TypeName returnTypeName) {
        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();
        codeBlockBuilder.add("$T<$T,$T> factory = ", FUNCTION_CLASS_NAME, typeName, getConstructorInterfaceTypeName());
        addConstructorFactoryCode(codeBlockBuilder, 0);
        codeBlockBuilder
                .add(";\n")
                .addStatement("$T.this.$N = function.apply(factory.apply(null)).build()", mutatorTypeName, toFieldName(componentName))
                .addStatement("return this");

        return createImplementationMethod(
                "construct",
                createConstructorFunctionParameterType(),
                codeBlockBuilder.build(),
                componentName,
                returnTypeName);
    }

    /**
     * Creates the ParameterizedTypeName for the mutator function parameter based on whether
     * the element type has a mutator.
     *
     * @return the ParameterizedTypeName representing the mutator function parameter type
     */
    protected ParameterizedTypeName createMutatorFunctionParameterType() {
        if (elementTypeManager.hasMutator()) {
            return ParameterizedTypeName.get(
                    mtorFunctionClassName,
                    elementTypeManager.getTypeName(),
                    elementTypeManager.getMutatorInterfaceTypeName());
        } else {
            return ParameterizedTypeName.get(
                    mtorFunctionClassName,
                    elementTypeManager.getTypeName());
        }
    }

    /**
     * Creates a ParameterizedTypeName representing the parameter type for a constructor function,
     * based on whether the element type has a mutator.
     *
     * @return the ParameterizedTypeName for the constructor function parameter type
     */
    protected ParameterizedTypeName createConstructorFunctionParameterType() {
        if (elementTypeManager.hasConstructor()) {
            return ParameterizedTypeName.get(
                    ctorFunctionClassName,
                    elementTypeManager.getTypeName(),
                    elementTypeManager.getConstructorInterfaceTypeName());
        } else {
            return ParameterizedTypeName.get(
                    ctorFunctionClassName,
                    elementTypeManager.getTypeName());
        }
    }

    @Override
    public void addMutatorFactoryCode(CodeBlock.Builder codeBlockbuilder, int factoryMethodIndex) {
        codeBlockbuilder.add("\nelement$L -> $T.builder(element$L, ", factoryMethodIndex, builderImplementationClassName, factoryMethodIndex);
        elementTypeManager.addMutatorFactoryCode(codeBlockbuilder, factoryMethodIndex + 1);
        codeBlockbuilder.add(")");
    }

    @Override
    public void addConstructorFactoryCode(CodeBlock.Builder codeBlockbuilder, int factoryMethodIndex) {
        codeBlockbuilder.add("\nelement$L -> $T.builder(element$L, ", factoryMethodIndex, builderImplementationClassName, factoryMethodIndex);
        elementTypeManager.addConstructorFactoryCode(codeBlockbuilder, factoryMethodIndex + 1);
        codeBlockbuilder.add(")");
    }

    private MethodSpec createConstructorConstructMethodImpl(TypeName mutatorTypeName, String fieldName, String componentName, TypeName nextType) {
        CodeBlock.Builder codeBlock = CodeBlock.builder();
        codeBlock.add("$T<$T,$T> factory = ", FUNCTION_CLASS_NAME, typeName, getConstructorInterfaceTypeName());
        addConstructorFactoryCode(codeBlock, 0);
        codeBlock
                .add(";\n")
                .addStatement("$T.this.$N = function.apply(factory.apply(null)).build()", mutatorTypeName, fieldName)
                .addStatement("return this");

        return createImplementationMethod(
                "construct",
                createConstructorFunctionParameterType(),
                codeBlock.build(),
                componentName,
                nextType);
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
