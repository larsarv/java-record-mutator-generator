package io.github.larsarv.jrmg.annotation.processor;

import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.TypeName;
import com.palantir.javapoet.TypeSpec;

import java.io.IOException;

/**
 * Interface for type information used during annotation processing.
 * This interface defines the contract for handling different types of record components
 * when generating mutator classes. Different implementations handle primitives,
 * records, lists, and sets differently.
 */
public interface TypeInfo {
    /**
     * Returns the TypeName of the component type.
     *
     * @return the TypeName representing the component type
     */
    TypeName getTypeName();

    /**
     * Returns the TypeName of the mutator interface for this type, if applicable.
     * For types that don't have nested mutators (like primitives), this returns null.
     *
     * @return the TypeName of the mutator interface, or null if not applicable
     */
    TypeName getMutatorInterfaceTypeName();

    /**
     * Contributes the necessary fields and methods to the mutator class builder
     * for this specific type of component.
     *
     * @param mutatorClassBuilder the builder for the mutator class
     * @param componentName the name of the component being processed
     * @param recordMutatorInterfaceTypeName the type name of the record mutator interface
     */
    void contributeToMutator(TypeSpec.Builder mutatorClassBuilder, String componentName, TypeName recordMutatorInterfaceTypeName);

    /**
     * Adds code to the mutator factory method for this type.
     * This is used when creating functions that can mutate the component.
     *
     * @param codeBlockbuilder the code block builder to append code to
     * @param i the index used for generating unique variable names
     */
    void addMutatorFactoryCode(CodeBlock.Builder codeBlockbuilder, int i);

    /**
     * Contributes the necessary fields and methods to the constructor class and interface builders
     * for this type of component. This method is responsible for defining the interfaces and implementation
     * used for the all() function.
     *
     * @param constructorClassBuilder the builder for the constructor class
     * @param constructorInterfaceBuilder the builder for the constructor interface
     * @param mutatorClassName the TypeName of the mutator class associated with this component
     * @param nextType the TypeName of the next component in the sequence
     * @param componentName the name of the component being processed
     */
    void contributeToConstructor(
            TypeSpec.Builder constructorClassBuilder,
            TypeSpec.Builder constructorInterfaceBuilder,
            TypeName mutatorClassName,
            TypeName nextType,
            String componentName);

}
