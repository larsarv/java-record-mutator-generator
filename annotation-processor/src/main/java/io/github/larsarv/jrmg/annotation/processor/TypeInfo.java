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
     * Returns the full TypeName with generic arguments, if any.
     *
     * @return the TypeName representing the type
     */
    TypeName getTypeName();

    /**
     * Indicates whether this type has a mutator.
     * @return true if a mutator exists for this type, false otherwise
     */
    boolean hasMutator();
    /**
     * Returns the TypeName (including generic arguments if any) of the mutator
     * interface for this type, if applicable.
     * For types that don't have mutators (like primitives), this returns null.
     *
     * @return the TypeName of the mutator interface, or null if not applicable
     */
    TypeName getMutatorInterfaceTypeName();

    /**
     * Returns the TypeName (including generic arguments if any) of the first constructor for this type.
     * For types that don't have mutators (like primitives), this returns null.

     * @return the TypeName of the first constructor, or null if no constructor exists
     */
    TypeName getFirstConstructorTypeName();
    /**
     * Returns the TypeName (including generic arguments if any) of the last constructor for this type.
     * For types that don't have constructors (like primitives), this returns null.
     *
     * @return the TypeName of the last constructor, or null if no constructor exists
     */
    TypeName getLastConstructorTypeName();
    /**
     * Contributes the necessary code to the mutator class builder for this type.
     * This method is responsible for adding fields, methods, or other constructs
     * that enable mutation of the component represented by this type info.
     *
     * @param mutatorClassBuilder the builder for the mutator class being generated
     * @param mutatorClassName the fully qualified name of the mutator class including generic arguments.
     * @param componentName the name of the component being mutated
     * @param recordMutatorInterfaceTypeName the TypeName of the mutator interface including any generic arguments
     */
    void contributeToMutator(
            TypeSpec.Builder mutatorClassBuilder,
            TypeName mutatorClassName,
            String componentName,
            TypeName recordMutatorInterfaceTypeName);

    /**
     * Adds code to the mutator factory method for this type.
     * This is used when creating functions that can mutate the component.
     *
     * @param codeBlockBuilder the code block builder to append code to
     * @param factoryMethodIndex the index used for generating unique variable names to avoid name conflicts
     */
    void addMutatorFactoryCode(CodeBlock.Builder codeBlockBuilder, int factoryMethodIndex);
    /**
     * Adds code to the constructor factory method for this type.
     * This is used when creating functions that can construct the component.
     *
     * @param codeBlockBuilder the code block builder to append code to
     * @param factoryMethodIndex the index used for generating unique variable names to avoid name conflicts
     */
    void addConstructorFactoryCode(CodeBlock.Builder codeBlockBuilder, int factoryMethodIndex);
    /**
     * Contributes the necessary fields and methods to the constructor class and interface builders
     * for this type. This method is responsible for defining the interfaces and implementation
     * used for the 'construct' functions.
     *
     * @param constructorClassBuilder the builder for the constructor class
     * @param constructorInterfaceBuilder the builder for the constructor interface
     * @param mutatorClassName the fully qualified name of the mutator class including generic arguments
     * @param nextType the TypeName of the next component in the sequence including generic arguments
     * @param componentName the name of the component being processed
     */
    void contributeToConstructor(
            TypeSpec.Builder constructorClassBuilder,
            TypeSpec.Builder constructorInterfaceBuilder,
            TypeName mutatorClassName,
            TypeName nextType,
            String componentName);

}
