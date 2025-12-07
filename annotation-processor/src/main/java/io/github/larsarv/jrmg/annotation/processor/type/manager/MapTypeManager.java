package io.github.larsarv.jrmg.annotation.processor.type.manager;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeName;

/**
 * TypeManager implementation for Map types.
 * <p>
 * This class extends {@link CollectionTypeManager} to handle Map-specific logic,
 * including key and value types, and generating appropriate mutator and constructor interfaces.
 */
public class MapTypeManager extends CollectionTypeManager {
    private final TypeManager keyTypeManager;
    private final TypeManager valueTypeManager;

    /**
     * Constructs a new MapTypeManager.
     *
     * @param typeName                       the TypeName of the map
     * @param keyTypeManager                 the TypeManager for the keys
     * @param valueTypeManager               the TypeManager for the values
     * @param mtorInterfaceClassName         the ClassName for the mutator interface
     * @param mtorFunctionClassName          the ClassName for the mutator function interface
     * @param ctorInterfaceClassName         the ClassName for the constructor interface
     * @param ctorFunctionClassName          the ClassName for the constructor function interface
     * @param builderImplementationClassName the ClassName for the builder implementation
     */
    public MapTypeManager(
            TypeName typeName,
            TypeManager keyTypeManager,
            TypeManager valueTypeManager,
            ClassName mtorInterfaceClassName,
            ClassName mtorFunctionClassName,
            ClassName ctorInterfaceClassName,
            ClassName ctorFunctionClassName,
            ClassName builderImplementationClassName
    ) {
        super(typeName, valueTypeManager, mtorInterfaceClassName, mtorFunctionClassName, ctorInterfaceClassName, ctorFunctionClassName, builderImplementationClassName);
        this.keyTypeManager = keyTypeManager;
        this.valueTypeManager = valueTypeManager;
    }

    @Override
    public TypeName getMutatorInterfaceTypeName() {
        if (valueTypeManager.hasMutator()) {
            return ParameterizedTypeName.get(
                    mtorInterfaceClassName,
                    keyTypeManager.getTypeName(),
                    valueTypeManager.getTypeName(),
                    valueTypeManager.getMutatorInterfaceTypeName());
        } else {
            return ParameterizedTypeName.get(
                    mtorInterfaceClassName,
                    keyTypeManager.getTypeName(),
                    valueTypeManager.getTypeName());
        }
    }

    @Override
    public TypeName getConstructorInterfaceTypeName() {
        if (valueTypeManager.hasConstructor()) {
            return ParameterizedTypeName.get(
                    ctorInterfaceClassName,
                    keyTypeManager.getTypeName(),
                    valueTypeManager.getTypeName(),
                    valueTypeManager.getConstructorInterfaceTypeName());
        } else {
            return ParameterizedTypeName.get(
                    ctorInterfaceClassName,
                    keyTypeManager.getTypeName(),
                    valueTypeManager.getTypeName());
        }
    }

    @Override
    protected ParameterizedTypeName createMutatorFunctionParameterType() {
        if (valueTypeManager.hasMutator()) {
            return ParameterizedTypeName.get(
                    mtorFunctionClassName,
                    keyTypeManager.getTypeName(),
                    valueTypeManager.getTypeName(),
                    valueTypeManager.getMutatorInterfaceTypeName());
        } else {
            return ParameterizedTypeName.get(
                    mtorFunctionClassName,
                    keyTypeManager.getTypeName(),
                    valueTypeManager.getTypeName());
        }
    }

    @Override
    protected ParameterizedTypeName createConstructorFunctionParameterType() {
        if (valueTypeManager.hasConstructor()) {
            return ParameterizedTypeName.get(
                    ctorFunctionClassName,
                    keyTypeManager.getTypeName(),
                    valueTypeManager.getTypeName(),
                    valueTypeManager.getConstructorInterfaceTypeName());
        } else {
            return ParameterizedTypeName.get(
                    ctorFunctionClassName,
                    keyTypeManager.getTypeName(),
                    valueTypeManager.getTypeName());
        }
    }

}
