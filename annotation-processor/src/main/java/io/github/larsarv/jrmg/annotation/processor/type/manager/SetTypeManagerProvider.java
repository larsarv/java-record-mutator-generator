package io.github.larsarv.jrmg.annotation.processor.type.manager;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.TypeName;
import io.github.larsarv.jrmg.api.*;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

/**
 * TypeManagerProvider for Set types.
 * <p>
 * Creates a {@link CollectionTypeManager} configured for Set types, selecting the appropriate
 * implementation classes based on whether the set elements are mutable (nested records) or simple.
 */
public class SetTypeManagerProvider implements TypeManagerProvider {
    private static final ClassName CLASS_NAME_SET_BUILDER_IMPL = ClassName.get(SetBuilderImpl.class);
    private static final ClassName CLASS_NAME_NESTED_SET_MTOR = ClassName.get(NestedSetMtor.class);
    private static final ClassName CLASS_NAME_SIMPLE_SET_MTOR = ClassName.get(SimpleSetMtor.class);
    private static final ClassName CLASS_NAME_NESTED_SET_MTOR_FUNCTION = ClassName.get(NestedSetMtorFunction.class);
    private static final ClassName CLASS_NAME_SIMPLE_SET_MTOR_FUNCTION = ClassName.get(SimpleSetMtorFunction.class);
    private static final ClassName CLASS_NAME_NESTED_SET_CTOR = ClassName.get(NestedSetCtor.class);
    private static final ClassName CLASS_NAME_SIMPLE_SET_CTOR = ClassName.get(SimpleSetCtor.class);
    private static final ClassName CLASS_NAME_NESTED_SET_CTOR_FUNCTION = ClassName.get(NestedSetCtorFunction.class);
    private static final ClassName CLASS_NAME_SIMPLE_SET_CTOR_FUNCTION = ClassName.get(SimpleSetCtorFunction.class);

    @Override
    public TypeManager create(TypeMirror type, TypeProviderUtils utils) {
        if (type.getKind() == TypeKind.DECLARED) {
            DeclaredType declaredType = (DeclaredType) type;
            if (utils.isSet(declaredType)) {
                TypeManager elementTypeManager = utils.createTypeManager(declaredType.getTypeArguments().get(0));
                return new CollectionTypeManager(
                        TypeName.get(type),
                        elementTypeManager,
                        elementTypeManager.hasMutator() ? CLASS_NAME_NESTED_SET_MTOR : CLASS_NAME_SIMPLE_SET_MTOR,
                        elementTypeManager.hasMutator() ? CLASS_NAME_NESTED_SET_MTOR_FUNCTION : CLASS_NAME_SIMPLE_SET_MTOR_FUNCTION,
                        elementTypeManager.hasConstructor() ? CLASS_NAME_NESTED_SET_CTOR : CLASS_NAME_SIMPLE_SET_CTOR,
                        elementTypeManager.hasConstructor() ? CLASS_NAME_NESTED_SET_CTOR_FUNCTION : CLASS_NAME_SIMPLE_SET_CTOR_FUNCTION,
                        CLASS_NAME_SET_BUILDER_IMPL);
            }
        }
        return null;
    }
}