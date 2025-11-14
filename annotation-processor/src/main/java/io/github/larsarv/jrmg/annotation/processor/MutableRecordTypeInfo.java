package io.github.larsarv.jrmg.annotation.processor;

import com.palantir.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.function.Function;

/**
 * TypeInfo implementation for record types that are annotated with GenerateMutator.
 * This class generates not only basic setter and getter methods but also a mutate method
 * that allows for nested mutation of the record component.
 */
public class MutableRecordTypeInfo extends SimpleTypeInfo implements TypeInfo {
    private final TypeName recordMutatorTypeName;
    private final ClassName recordMutatorClassName;

    /**
     * Constructs a MutableRecordTypeInfo with the given type name and mutator information.
     *
     * @param typeName               the TypeName of the record component
     * @param recordMutatorTypeName  the TypeName of the mutator interface for this record
     * @param recordMutatorClassName the ClassName of the mutator implementation for this record
     */
    public MutableRecordTypeInfo(TypeName typeName, TypeName recordMutatorTypeName, ClassName recordMutatorClassName) {
        super(typeName);
        this.recordMutatorTypeName = recordMutatorTypeName;
        this.recordMutatorClassName = recordMutatorClassName;
    }

    @Override
    public TypeName getMutatorInterfaceTypeName() {
        return recordMutatorTypeName;
    }

    @Override
    public void contributeToMutator(TypeSpec.Builder mutatorClassBuilder, String componentName, TypeName recordMutatorInterfaceTypeName) {
        super.contributeToMutator(mutatorClassBuilder, componentName, recordMutatorInterfaceTypeName);

        String fieldName = toFiledName(componentName);
        mutatorClassBuilder.addMethod(MethodSpec.methodBuilder(toMethodName("mutate", componentName))
                .addModifiers(Modifier.PUBLIC)
                .returns(recordMutatorInterfaceTypeName)
                .addParameter(
                        ParameterizedTypeName.get(
                                ClassName.get(Function.class),
                                recordMutatorTypeName,
                                recordMutatorTypeName),
                        "mutateFunction")
                .addStatement("this.$N = mutateFunction.apply($T.mutator(this.$N)).build()",
                        fieldName,
                        recordMutatorClassName,
                        fieldName)
                .addStatement("return this")
                .build());

        mutatorClassBuilder.addMethod(MethodSpec.methodBuilder(toMethodName("set", componentName))
                .addModifiers(Modifier.PUBLIC)
                .returns(recordMutatorInterfaceTypeName)
                .addParameter(
                        ParameterizedTypeName.get(
                                ClassName.get(Function.class),
                                recordMutatorTypeName,
                                recordMutatorTypeName),
                        "mutatorFunction")
                .addStatement("this.$N = mutatorFunction.apply($T.mutator(null)).build()",
                        fieldName,
                        recordMutatorClassName)
                .addStatement("return this")
                .build());
    }

    @Override
    public void addMutatorFactoryCode(CodeBlock.Builder codeBlockbuilder, int factoryMethodIndex) {
        codeBlockbuilder.add("\nrecord$L -> $T.mutator(record$L)", factoryMethodIndex, recordMutatorClassName, factoryMethodIndex);
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
        constructorClassBuilder.addMethod(MethodSpec.methodBuilder(toMethodName("set", componentName))
                .addModifiers(Modifier.PUBLIC)
                .returns(nextType)
                .addParameter(
                        ParameterizedTypeName.get(
                                ClassName.get(Function.class),
                                recordMutatorTypeName,
                                recordMutatorTypeName),
                        "mutatorFunction")
                .addStatement("$T.this.$N = mutatorFunction.apply($T.mutator(null)).build()",
                        mutatorClassName,
                        fieldName,
                        recordMutatorClassName)
                .addStatement("return this")
                .build());


        constructorInterfaceBuilder.addMethod(MethodSpec.methodBuilder(toMethodName("set", componentName))
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(nextType)
                .addParameter(
                        ParameterizedTypeName.get(
                                ClassName.get(Function.class),
                                recordMutatorTypeName,
                                recordMutatorTypeName),
                        "mutatorFunction")
                .build());
    }

}
