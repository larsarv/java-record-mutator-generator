package io.github.larsarv.jrmg.annotation.processor;

import com.palantir.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.Locale;

/**
 * TypeInfo implementation for simple types (primitives, String, etc.) that don't require
 * nested mutators. This class generates basic setter and getter methods for the component.
 */
public class SimpleTypeInfo implements TypeInfo {
    /**
     * The type name including generic parameters
     */
    protected TypeName typeName;

    /**
     * Constructs a SimpleTypeInfo with the given type name.
     *
     * @param typeName the TypeName of the component
     */
    public SimpleTypeInfo(TypeName typeName) {
        this.typeName = typeName;
    }

    @Override
    public TypeName getTypeName() {
        return typeName;
    }

    @Override
    public TypeName getMutatorInterfaceTypeName() {
        return null;
    }

    @Override
    public void contributeToMutator(TypeSpec.Builder mutatorClassBuilder, String componentName, TypeName recordMutatorInterfaceTypeName) {
        String fieldName = toFiledName(componentName);
        FieldSpec field = FieldSpec.builder(
                typeName,
                fieldName,
                Modifier.PRIVATE).build();

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
                .addField(field)
                .addMethod(setterMethod)
                .addMethod(getterMethod);
    }

    @Override
    public void addMutatorFactoryCode(CodeBlock.Builder codeBlockbuilder, int factoryMethodIndex) {
        codeBlockbuilder.add("null");
    }

    /**
     * Converts a component name into a method name by capitalizing the first letter
     * of the component name and prefixing it with the given prefix.
     *
     * @param prefix the prefix to prepend to the method name
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
    protected static String toFiledName(String componentName) {
        return componentName.substring(0, 1).toLowerCase(Locale.ROOT) + componentName.substring(1);
    }

}
