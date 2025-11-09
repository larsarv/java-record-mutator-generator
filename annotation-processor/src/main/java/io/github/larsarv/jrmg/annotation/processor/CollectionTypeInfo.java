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
    private final TypeName mutatorInterfaceTypeName; // Type of the mutator including generic parameters
    private final ClassName mutatorImplementationClassName; // Mutator implementation class
    private final ClassName mutatorFunctionClassName; // Function used by the mutate function as parameter



    /**
     * Constructs a CollectionTypeInfo with the given type information.
     *
     * @param typeName the TypeName of the collection component
     * @param elementTypeInfo the TypeInfo for the elements contained in the collection
     * @param mutatorInterfaceTypeName the TypeName of the mutator interface for this collection
     * @param mutatorImplementationClassName the ClassName of the mutator implementation for this collection
     * @param mutatorFunctionClassName the ClassName of the function type used for mutation
     */
    public CollectionTypeInfo(TypeName typeName, TypeInfo elementTypeInfo, TypeName mutatorInterfaceTypeName, ClassName mutatorImplementationClassName, ClassName mutatorFunctionClassName) {
        super(typeName);
        this.elementTypeInfo = elementTypeInfo;
        this.mutatorInterfaceTypeName = mutatorInterfaceTypeName;
        this.mutatorImplementationClassName = mutatorImplementationClassName;
        this.mutatorFunctionClassName = mutatorFunctionClassName;
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
        codeBlockbuilder.add("$T<$T,$T> factory = ", FUNCTION_CLASS_NAME, typeName, mutatorInterfaceTypeName);
        addMutatorFactoryCode(codeBlockbuilder, 0);
        codeBlockbuilder
                .add(";\n")
                .addStatement("this.$N = mutateFunction.mutate(factory.apply(this.$N)).build()", fieldName, fieldName)
                .addStatement("return this");

        MethodSpec.Builder mutateMethodBuilder = MethodSpec.methodBuilder(toMethodName("mutate", componentName))
                .addModifiers(Modifier.PUBLIC)
                .returns(recordMutatorInterfaceTypeName)
                .addParameter(
                        createParameterType(),
                        "mutateFunction")
                .addCode(codeBlockbuilder.build());

        mutatorClassBuilder.addMethod(mutateMethodBuilder.build());
    }

    private ParameterizedTypeName createParameterType() {
        if (elementTypeInfo.getMutatorInterfaceTypeName() == null) {
            return ParameterizedTypeName.get(
                    mutatorFunctionClassName,
                    elementTypeInfo.getTypeName());
        } else {
            return ParameterizedTypeName.get(
                    mutatorFunctionClassName,
                    elementTypeInfo.getTypeName(),
                    elementTypeInfo.getMutatorInterfaceTypeName());
        }
    }

    @Override
    public void addMutatorFactoryCode(CodeBlock.Builder codeBlockbuilder, int factoryMethodIndex) {
        codeBlockbuilder.add("\nelement$L -> $T.mutator(element$L, ", factoryMethodIndex, mutatorImplementationClassName, factoryMethodIndex);
        elementTypeInfo.addMutatorFactoryCode(codeBlockbuilder, factoryMethodIndex + 1);
        codeBlockbuilder.add(")");
    }

}
