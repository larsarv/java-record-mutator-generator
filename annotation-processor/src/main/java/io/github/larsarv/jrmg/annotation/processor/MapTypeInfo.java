package io.github.larsarv.jrmg.annotation.processor;

import com.palantir.javapoet.*;
import io.github.larsarv.jrmg.api.NestedKeyValueMapMutator;

import javax.lang.model.element.Modifier;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * TypeInfo implementation for Map types that may contain either simple elements,
 * records, lists, sets or nested mutable records as keys and values. This class
 * generates the necessary mutator methods for Maps, handling both simple and complex
 * nested mutation scenarios for both keys and values.
 */
public class MapTypeInfo extends SimpleTypeInfo implements TypeInfo {
    private final static ClassName FUNCTION_CLASS_NAME = ClassName.get(Function.class);

    private final TypeInfo keyTypeInfo;
    private final TypeInfo valueTypeInfo;
    private final TypeName mutatorInterfaceTypeName; // Type of the mutator including generic parameters
    private final ClassName mutatorImplementationClassName; // Mutator implementation class
    private final TypeName mutatorFunctionTypeName; // Function used by the mutate function as parameter

    /**
     * Constructs a MapTypeInfo with the given type information.
     *
     * @param typeName the TypeName of the Map component
     * @param keyTypeInfo the TypeInfo for the keys contained in the map
     * @param valueTypeInfo the TypeInfo for the values contained in the map
     * @param mutatorInterfaceTypeName the TypeName of the mutator interface for this map
     * @param mutatorImplementationClassName the ClassName of the mutator implementation for this map
     * @param mutatorFunctionTypeName the TypeName of the function type used for mutation
     */
    public MapTypeInfo(
            TypeName typeName,
            TypeInfo keyTypeInfo,
            TypeInfo valueTypeInfo,
            TypeName mutatorInterfaceTypeName,
            ClassName mutatorImplementationClassName,
            TypeName mutatorFunctionTypeName
    ) {
        super(typeName);
        this.keyTypeInfo = keyTypeInfo;
        this.valueTypeInfo = valueTypeInfo;
        this.mutatorInterfaceTypeName = mutatorInterfaceTypeName;
        this.mutatorImplementationClassName = mutatorImplementationClassName;
        this.mutatorFunctionTypeName = mutatorFunctionTypeName;
    }

    @Override
    public TypeName getMutatorInterfaceTypeName() {
        return mutatorInterfaceTypeName;
    }

    @Override
    public void contributeToMutator(TypeSpec.Builder mutatorClassBuilder, String componentName, TypeName recordMutatorInterfaceTypeName) {
        super.contributeToMutator(mutatorClassBuilder, componentName, recordMutatorInterfaceTypeName);

        String fieldName = toFiledName(componentName);

        CodeBlock.Builder codeBlockbuilder = CodeBlock.builder();
        codeBlockbuilder.add(
                "$T<$T,$T> factory = ",
                FUNCTION_CLASS_NAME,
                typeName,
                mutatorInterfaceTypeName);
        addMutatorFactoryCode(codeBlockbuilder, 0);
        codeBlockbuilder
                .add(";\n")
                .addStatement("this.$N = mutateFunction.mutate(factory.apply(this.$N)).build()",
                        fieldName, fieldName)
                .addStatement("return this");

        MethodSpec.Builder mutateMethodBuilder = MethodSpec.methodBuilder(toMethodName("mutate", componentName))
                .addModifiers(Modifier.PUBLIC)
                .returns(recordMutatorInterfaceTypeName)
                .addParameter(
                        mutatorFunctionTypeName,
                        "mutateFunction")
                .addCode(codeBlockbuilder.build());

        mutatorClassBuilder.addMethod(mutateMethodBuilder.build());
    }

    @Override
    public void addMutatorFactoryCode(CodeBlock.Builder codeBlockbuilder, int factoryMethodIndex) {
        codeBlockbuilder.add("\nelement$L -> $T.mutator(element$L, ", factoryMethodIndex, mutatorImplementationClassName, factoryMethodIndex);
        keyTypeInfo.addMutatorFactoryCode(codeBlockbuilder, factoryMethodIndex + 1);
        codeBlockbuilder.add(", ");
        valueTypeInfo.addMutatorFactoryCode(codeBlockbuilder, factoryMethodIndex + 2);
        codeBlockbuilder.add(")");
    }
}