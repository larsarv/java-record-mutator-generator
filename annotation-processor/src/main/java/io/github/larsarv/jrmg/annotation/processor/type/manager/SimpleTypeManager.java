package io.github.larsarv.jrmg.annotation.processor.type.manager;

import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeName;
import com.palantir.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.util.Locale;

/**
 * TypeManager implementation for simple types (primitives, String, etc.) that don't require
 * nested mutators. This class generates basic setter and getter methods for the component.
 */
public class SimpleTypeManager implements TypeManager {
    /**
     * The type name including generic parameters
     */
    protected TypeName typeName;

    /**
     * Constructs a SimpleTypeManager with the given type name.
     *
     * @param typeName the TypeName of the component
     */
    public SimpleTypeManager(TypeName typeName) {
        this.typeName = typeName;
    }

    /**
     * Converts a component name into a method name by capitalizing the first letter
     * of the component name and prefixing it with the given prefix.
     *
     * @param prefix        the prefix to prepend to the method name
     * @param componentName the name of the component (e.g., "name", "age")
     * @return the generated method name (e.g., "setName", "setAge")
     */
    protected static String toMethodName(String prefix, String componentName) {
        return prefix + componentName.substring(0, 1).toUpperCase(Locale.ROOT) + componentName.substring(1);
    }

    /**
     * Converts a component name into a field name by converting the first character to lowercase
     * and keeping the rest unchanged.
     *
     * @param componentName the name of the component (e.g., "Name", "Age")
     * @return the field name with the first character lowercase (e.g., "name", "age")
     */
    protected static String toFieldName(String componentName) {
        return componentName.substring(0, 1).toLowerCase(Locale.ROOT) + componentName.substring(1);
    }

    @Override
    public TypeName getTypeName() {
        return typeName;
    }

    @Override
    public boolean hasMutator() {
        return false;
    }

    /*
        @Override
        public TypeName getConstructorInterfaceTypeName() {
            return null;
        }
    */
    @Override
    public TypeName getMutatorInterfaceTypeName() {
        return null;
    }

    /**
     * Contributes simple setter and getter methods to the mutator.
     *
     * @param mutatorClassBuilder            the builder for the Mutator class
     * @param mutatorClassName               the name of the Mutator class
     * @param componentName                  the name of the component
     * @param recordMutatorInterfaceTypeName the return type for fluent methods
     */
    @Override
    public void contributeToMutator(
            TypeSpec.Builder mutatorClassBuilder,
            TypeName mutatorClassName,
            String componentName,
            TypeName recordMutatorInterfaceTypeName
    ) {
        String fieldName = toFieldName(componentName);

        MethodSpec setterMethod = MethodSpec.methodBuilder(toMethodName("set", componentName))
                .addModifiers(Modifier.PUBLIC)
                .returns(recordMutatorInterfaceTypeName)
                .addParameter(typeName, "value")
                .addStatement("this.$N = value", fieldName)
                .addStatement("return this")
                .build();

        MethodSpec getterMethod = MethodSpec.methodBuilder(toMethodName("get", componentName))
                .addModifiers(Modifier.PUBLIC)
                .returns(typeName)
                .addStatement("return $N", fieldName)
                .build();

        mutatorClassBuilder
                .addMethod(setterMethod)
                .addMethod(getterMethod);
    }

    @Override
    public void addMutatorFactoryCode(CodeBlock.Builder codeBlockbuilder, int factoryMethodIndex) {
        codeBlockbuilder.add("null");
    }

    @Override
    public boolean hasConstructor() {
        return false;
    }

    @Override
    public TypeName getConstructorInterfaceTypeName() {
        return null;
    }

    @Override
    public void addConstructorFactoryCode(CodeBlock.Builder codeBlockbuilder, int factoryMethodIndex) {
        codeBlockbuilder.add("null");
    }

    /**
     * Contributes simple setter methods to the constructor.
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
        String fieldName = toFieldName(componentName);

        // Implementation method
        constructorClassBuilder.addMethod(MethodSpec.methodBuilder(toMethodName("set", componentName))
                .addModifiers(Modifier.PUBLIC)
                .returns(nextType)
                .addParameter(typeName, "value")
                .addStatement("$T.this.$N = value", mutatorClassName, fieldName)
                .addStatement("return this")
                .build());

        // Interface method
        constructorInterfaceBuilder.addMethod(MethodSpec.methodBuilder(toMethodName("set", componentName))
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(nextType)
                .addParameter(typeName, "value")
                .build());
    }

}
