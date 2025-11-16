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
            TypeName mutatorClassName,
            String componentName,
            TypeName recordMutatorInterfaceTypeName
    ) {
        super.contributeToMutator(mutatorClassBuilder, mutatorClassName, componentName, recordMutatorInterfaceTypeName);

        String fieldName = toFiledName(componentName);

        CodeBlock.Builder mutatorCodeBlockbuilder = CodeBlock.builder();
        mutatorCodeBlockbuilder.add("$T<$T,$T> factory = ", FUNCTION_CLASS_NAME, typeName, getMutatorInterfaceTypeName());
        addMutatorFactoryCode(mutatorCodeBlockbuilder, 0);
        mutatorCodeBlockbuilder
                .add(";\n")
                .addStatement("this.$N = mutateFunction.mutate(factory.apply(this.$N)).build()", fieldName, fieldName)
                .addStatement("return this");

        mutatorClassBuilder.addMethod(MethodSpec.methodBuilder(toMethodName("mutate", componentName))
                .addModifiers(Modifier.PUBLIC)
                .returns(recordMutatorInterfaceTypeName)
                .addParameter(
                        createMutatorFunctionParameterType(),
                        "mutateFunction")
                .addCode(mutatorCodeBlockbuilder.build())
                .build());

        CodeBlock.Builder setterCodeBlockbuilder = CodeBlock.builder();
        setterCodeBlockbuilder.add("$T<$T,$T> factory = ", FUNCTION_CLASS_NAME, typeName, getMutatorInterfaceTypeName());
        addMutatorFactoryCode(setterCodeBlockbuilder, 0);
        setterCodeBlockbuilder
                .add(";\n")
                .addStatement("this.$N = mutateFunction.mutate(factory.apply(null)).build()",
                        fieldName)
                .addStatement("return this");

        mutatorClassBuilder.addMethod(MethodSpec.methodBuilder(toMethodName("set", componentName))
                .addModifiers(Modifier.PUBLIC)
                .returns(recordMutatorInterfaceTypeName)
                .addParameter(
                        createMutatorFunctionParameterType(),
                        "mutateFunction")
                .addCode(setterCodeBlockbuilder.build())
                .build());

        // ----------------
        if (elementTypeInfo.getFirstConstructorTypeName() != null) {
            CodeBlock.Builder allCodeBlockbuilder = CodeBlock.builder();
            allCodeBlockbuilder.add("$T<$T,$T> factory = ", FUNCTION_CLASS_NAME, typeName, getConstructorInterfaceTypeName());
            addConstructorFactoryCode(allCodeBlockbuilder, 0);
            allCodeBlockbuilder
                    .add(";\n")
                    .addStatement("this.$N = constructorFunction.mutate(factory.apply(this.$N)).build()", fieldName, fieldName)
                    .addStatement("return this");

            mutatorClassBuilder.addMethod(MethodSpec.methodBuilder(toMethodName("all", componentName))
                    .addModifiers(Modifier.PUBLIC)
                    .returns(recordMutatorInterfaceTypeName)
                    .addParameter(
                            createConstructorFunctionParameterType(),
                            "constructorFunction")
                    .addCode(allCodeBlockbuilder.build())
                    .build());
        }

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
            TypeName mutatorClassName,
            TypeName nextType,
            String componentName
    ) {
        super.contributeToConstructor(constructorClassBuilder, constructorInterfaceBuilder, mutatorClassName, nextType,
                componentName);

        String fieldName = toFiledName(componentName);

        CodeBlock.Builder setCodeBlockbuilder = CodeBlock.builder();
        setCodeBlockbuilder.add("$T<$T,$T> factory = ", FUNCTION_CLASS_NAME, typeName, getMutatorInterfaceTypeName());
        addMutatorFactoryCode(setCodeBlockbuilder, 0);
        setCodeBlockbuilder
                .add(";\n")
                .addStatement("$T.this.$N = mutateFunction.mutate(factory.apply(null)).build()",
                        mutatorClassName,
                        fieldName)
                .addStatement("return this");

        constructorClassBuilder.addMethod(MethodSpec.methodBuilder(toMethodName("set", componentName))
                .addModifiers(Modifier.PUBLIC)
                .returns(nextType)
                .addParameter(
                        createMutatorFunctionParameterType(),
                        "mutateFunction")
                .addCode(setCodeBlockbuilder.build())
                .build());
        constructorInterfaceBuilder.addMethod(MethodSpec.methodBuilder(toMethodName("set", componentName))
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(nextType)
                .addParameter(
                        createMutatorFunctionParameterType(),
                        "mutateFunction")
                .build());

        if (elementTypeInfo.getFirstConstructorTypeName() != null) {
            CodeBlock.Builder constructCodeBlockbuilder = CodeBlock.builder();
            constructCodeBlockbuilder.add("$T<$T,$T> factory = ", FUNCTION_CLASS_NAME, typeName, getConstructorInterfaceTypeName());
            addConstructorFactoryCode(constructCodeBlockbuilder, 0);
            constructCodeBlockbuilder
                    .add(";\n")
                    .addStatement("$T.this.$N = constructFunction.mutate(factory.apply(null)).build()",
                            mutatorClassName,
                            fieldName)
                    .addStatement("return this");

            constructorClassBuilder.addMethod(MethodSpec.methodBuilder(toMethodName("construct", componentName))
                    .addModifiers(Modifier.PUBLIC)
                    .returns(nextType)
                    .addParameter(
                            createConstructorFunctionParameterType(),
                            "constructFunction")
                    .addCode(constructCodeBlockbuilder.build())
                    .build());

            constructorInterfaceBuilder.addMethod(MethodSpec.methodBuilder(toMethodName("construct", componentName))
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .returns(nextType)
                    .addParameter(
                            createConstructorFunctionParameterType(),
                            "constructFunction")
                    .build());
        }
    }

}
