package io.github.larsarv.jrmg.annotation.processor.type.manager;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.TypeName;
import io.github.larsarv.jrmg.api.*;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.List;

/**
 * TypeManagerProvider for Map types.
 * <p>
 * Creates a {@link MapTypeManager} configured for Map types, selecting the appropriate
 * implementation classes based on whether the map values are mutable (nested records) or simple.
 */
public class MapTypeManagerProvider implements TypeManagerProvider {
    private static final ClassName CLASS_NAME_MAP_BUILDER_IMPL = ClassName.get(MapBuilderImpl.class);
    private static final ClassName CLASS_NAME_NESTED_MAP_MTOR = ClassName.get(NestedMapMtor.class);
    private static final ClassName CLASS_NAME_SIMPLE_MAP_MTOR = ClassName.get(SimpleMapMtor.class);
    private static final ClassName CLASS_NAME_NESTED_MAP_MTOR_FUNCTION = ClassName.get(NestedMapMtorFunction.class);
    private static final ClassName CLASS_NAME_SIMPLE_MAP_MTOR_FUNCTION = ClassName.get(SimpleMapMtorFunction.class);
    private static final ClassName CLASS_NAME_NESTED_MAP_CTOR = ClassName.get(NestedMapCtor.class);
    private static final ClassName CLASS_NAME_SIMPLE_MAP_CTOR = ClassName.get(SimpleMapCtor.class);
    private static final ClassName CLASS_NAME_NESTED_MAP_CTOR_FUNCTION = ClassName.get(NestedMapCtorFunction.class);
    private static final ClassName CLASS_NAME_SIMPLE_MAP_CTOR_FUNCTION = ClassName.get(SimpleMapCtorFunction.class);

    @Override
    public TypeManager create(TypeMirror type, TypeProviderUtils utils) {
        if (type.getKind() == TypeKind.DECLARED) {
            DeclaredType declaredType = (DeclaredType) type;
            if (utils.isMap(declaredType)) {
                List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
                if (typeArguments.size() == 2) {
                    TypeManager keyTypeManager = utils.createTypeManager(typeArguments.get(0));
                    TypeManager valueTypeManager = utils.createTypeManager(typeArguments.get(1));
                    return new MapTypeManager(
                            TypeName.get(type),
                            keyTypeManager,
                            valueTypeManager,
                            valueTypeManager.hasMutator() ? CLASS_NAME_NESTED_MAP_MTOR : CLASS_NAME_SIMPLE_MAP_MTOR,
                            valueTypeManager.hasMutator() ? CLASS_NAME_NESTED_MAP_MTOR_FUNCTION : CLASS_NAME_SIMPLE_MAP_MTOR_FUNCTION,
                            valueTypeManager.hasConstructor() ? CLASS_NAME_NESTED_MAP_CTOR : CLASS_NAME_SIMPLE_MAP_CTOR,
                            valueTypeManager.hasConstructor() ? CLASS_NAME_NESTED_MAP_CTOR_FUNCTION : CLASS_NAME_SIMPLE_MAP_CTOR_FUNCTION,
                            CLASS_NAME_MAP_BUILDER_IMPL);
                }
            }
        }
        return null;
    }
}