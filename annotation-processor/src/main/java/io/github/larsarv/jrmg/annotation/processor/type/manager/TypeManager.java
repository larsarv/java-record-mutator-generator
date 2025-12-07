package io.github.larsarv.jrmg.annotation.processor.type.manager;

import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.TypeName;
import com.palantir.javapoet.TypeSpec;

/**
 * Interface for managing type-specific code generation logic.
 * <p>
 * Implementations of this interface handle the generation of mutator and constructor methods
 * for different types of record components (e.g., primitives, nested records, collections).
 */
public interface TypeManager {
    /**
     * Returns the full TypeName with generic arguments, if any.
     *
     * @return the TypeName representing the type
     */
    TypeName getTypeName();

    /**
     * Checks if the type has a corresponding mutator.
     *
     * @return true if a mutator exists, false otherwise
     */
    boolean hasMutator();

    /**
     * Returns the TypeName of the mutator interface.
     *
     * @return the mutator interface TypeName, or null if none exists
     */
    TypeName getMutatorInterfaceTypeName();

    /**
     * Adds code to the mutator factory method.
     *
     * @param codeBlockBuilder   the builder for the code block
     * @param factoryMethodIndex the index of the factory method
     */
    void addMutatorFactoryCode(CodeBlock.Builder codeBlockBuilder, int factoryMethodIndex);

    /**
     * Contributes methods to the generated Mutator class.
     *
     * @param mutatorClassBuilder            the builder for the Mutator class
     * @param mutatorClassName               the name of the Mutator class
     * @param componentName                  the name of the component
     * @param recordMutatorInterfaceTypeName the return type for fluent methods
     */
    void contributeToMutator(
            TypeSpec.Builder mutatorClassBuilder,
            TypeName mutatorClassName,
            String componentName,
            TypeName recordMutatorInterfaceTypeName);


    /**
     * Checks if the type has a corresponding constructor.
     *
     * @return true if a constructor exists, false otherwise
     */
    boolean hasConstructor();

    /**
     * Returns the TypeName of the constructor interface.
     *
     * @return the constructor interface TypeName, or null if none exists
     */
    TypeName getConstructorInterfaceTypeName();

    /**
     * Adds code to the constructor factory method.
     *
     * @param codeBlockBuilder   the builder for the code block
     * @param factoryMethodIndex the index of the factory method
     */
    void addConstructorFactoryCode(CodeBlock.Builder codeBlockBuilder, int factoryMethodIndex);

    /**
     * Contributes methods to the generated Constructor class.
     *
     * @param constructorClassBuilder     the builder for the Constructor class
     * @param constructorInterfaceBuilder the builder for the Constructor interface
     * @param mutatorClassName            the name of the Mutator class
     * @param nextType                    the return type for the next step in the chain
     * @param componentName               the name of the component
     */
    void contributeToConstructor(
            TypeSpec.Builder constructorClassBuilder,
            TypeSpec.Builder constructorInterfaceBuilder,
            TypeName mutatorClassName,
            TypeName nextType,
            String componentName);

}
