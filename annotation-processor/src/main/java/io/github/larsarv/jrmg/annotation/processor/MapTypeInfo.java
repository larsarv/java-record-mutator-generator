package io.github.larsarv.jrmg.annotation.processor;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeName;

/**
 * Represents type information for a Map collection, extending {@link CollectionTypeInfo}.
 * This class encapsulates metadata required to generate type-safe map mutator and constructor interfaces
 * and functions, including key and value type information, mutator interfaces, and function types.
 * It overrides parent methods to handle map-specific type parameterization.
 *
 * @see CollectionTypeInfo
 * @see TypeInfo
 * @see ClassName
 * @see ParameterizedTypeName
 */
public class MapTypeInfo extends CollectionTypeInfo {
    private final ClassName mutatorInterfaceClassName;
    private final ClassName mutatorFunctionClassName;
    private final TypeInfo keyTypeInfo;
    private final TypeInfo valueTypeInfo;
    /**
     * Constructs a CollectionTypeInfo with the given type information.
     *
     * @param typeName                       the TypeName of the collection component
     * @param keyTypeInfo                    the TypeInfo for the key contained in the collection
     * @param valueTypeInfo                  the TypeInfo for the value contained in the collection
     * @param mutatorInterfaceClassName      the ClassName of the mutator interface for this map
     * @param mutatorImplementationClassName the ClassName of the mutator implementation for this map
     * @param mutatorFunctionClassName       the ClassName of the function type used for mutation
     */
    public MapTypeInfo(
            TypeName typeName,
            TypeInfo keyTypeInfo,
            TypeInfo valueTypeInfo,
            ClassName mutatorInterfaceClassName,
            ClassName mutatorImplementationClassName,
            ClassName mutatorFunctionClassName
    ) {
        super(typeName, valueTypeInfo, mutatorInterfaceClassName, mutatorImplementationClassName, mutatorFunctionClassName);
        this.mutatorInterfaceClassName = mutatorInterfaceClassName;
        this.mutatorFunctionClassName = mutatorFunctionClassName;
        this.keyTypeInfo = keyTypeInfo;
        this.valueTypeInfo = valueTypeInfo;
    }

    @Override
    public TypeName getMutatorInterfaceTypeName() {
        if (valueTypeInfo.hasMutator()) {
            return ParameterizedTypeName.get(
                    mutatorInterfaceClassName,
                    keyTypeInfo.getTypeName(),
                    valueTypeInfo.getTypeName(),
                    valueTypeInfo.getMutatorInterfaceTypeName(),
                    valueTypeInfo.getMutatorInterfaceTypeName());
        } else {
            return ParameterizedTypeName.get(
                    mutatorInterfaceClassName,
                    keyTypeInfo.getTypeName(),
                    valueTypeInfo.getTypeName());
        }
    }

    @Override
    public TypeName getFirstConstructorTypeName() {
        if (!valueTypeInfo.hasMutator()) {
            return null;
        }
        return ParameterizedTypeName.get(
                mutatorInterfaceClassName,
                keyTypeInfo.getTypeName(),
                valueTypeInfo.getTypeName(),
                valueTypeInfo.getFirstConstructorTypeName(),
                valueTypeInfo.getLastConstructorTypeName());
    }

    @Override
    public TypeName getLastConstructorTypeName() {
        if (!valueTypeInfo.hasMutator()) {
            return null;
        }
        return ParameterizedTypeName.get(
                mutatorInterfaceClassName,
                keyTypeInfo.getTypeName(),
                valueTypeInfo.getTypeName(),
                valueTypeInfo.getFirstConstructorTypeName(),
                valueTypeInfo.getLastConstructorTypeName());
    }

    /**
     * Returns the TypeName of the constructor interface for this collection type.
     *
     * @return the TypeName of the constructor interface
     */
    public TypeName getConstructorInterfaceTypeName() {
        if (valueTypeInfo.hasMutator()) {
            return ParameterizedTypeName.get(
                    mutatorInterfaceClassName,
                    keyTypeInfo.getTypeName(),
                    valueTypeInfo.getTypeName(),
                    valueTypeInfo.getFirstConstructorTypeName(),
                    valueTypeInfo.getLastConstructorTypeName());
        } else {
            return ParameterizedTypeName.get(
                    mutatorInterfaceClassName,
                    keyTypeInfo.getTypeName(),
                    valueTypeInfo.getTypeName());
        }
    }

    @Override
    protected ParameterizedTypeName createMutatorFunctionParameterType() {
        if (valueTypeInfo.hasMutator()) {
            return ParameterizedTypeName.get(
                    mutatorFunctionClassName,
                    keyTypeInfo.getTypeName(),
                    valueTypeInfo.getTypeName(),
                    valueTypeInfo.getMutatorInterfaceTypeName(),
                    valueTypeInfo.getMutatorInterfaceTypeName());
        } else {
            return ParameterizedTypeName.get(
                    mutatorFunctionClassName,
                    keyTypeInfo.getTypeName(),
                    valueTypeInfo.getTypeName());
        }
    }

    @Override
    protected ParameterizedTypeName createConstructorFunctionParameterType() {
        if (valueTypeInfo.hasMutator()) {
            return ParameterizedTypeName.get(
                    mutatorFunctionClassName,
                    keyTypeInfo.getTypeName(),
                    valueTypeInfo.getTypeName(),
                    valueTypeInfo.getFirstConstructorTypeName(),
                    valueTypeInfo.getLastConstructorTypeName());
        } else {
            return ParameterizedTypeName.get(
                    mutatorFunctionClassName,
                    keyTypeInfo.getTypeName(),
                    valueTypeInfo.getTypeName());
        }
    }

}
