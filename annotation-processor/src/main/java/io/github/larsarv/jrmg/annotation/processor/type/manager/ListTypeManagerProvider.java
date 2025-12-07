package io.github.larsarv.jrmg.annotation.processor.type.manager;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.TypeName;
import io.github.larsarv.jrmg.api.*;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

/**
 * TypeManagerProvider for List types.
 * <p>
 * Creates a {@link CollectionTypeManager} configured for List types, selecting the appropriate
 * implementation classes based on whether the list elements are mutable (nested records) or simple.
 */
public class ListTypeManagerProvider implements TypeManagerProvider {
    private static final ClassName CLASS_NAME_LIST_BUILDER_IMPL = ClassName.get(ListBuildImpl.class);
    private static final ClassName CLASS_NAME_NESTED_LIST_MTOR = ClassName.get(NestedListMtor.class);
    private static final ClassName CLASS_NAME_SIMPLE_LIST_MTOR = ClassName.get(SimpleListMtor.class);
    private static final ClassName CLASS_NAME_NESTED_LIST_MTOR_FUNCTION = ClassName.get(NestedListMtorFunction.class);
    private static final ClassName CLASS_NAME_SIMPLE_LIST_MTOR_FUNCTION = ClassName.get(SimpleListMtorFunction.class);
    private static final ClassName CLASS_NAME_NESTED_LIST_CTOR = ClassName.get(NestedListCtor.class);
    private static final ClassName CLASS_NAME_SIMPLE_LIST_CTOR = ClassName.get(SimpleListCtor.class);
    private static final ClassName CLASS_NAME_NESTED_LIST_CTOR_FUNCTION = ClassName.get(NestedListCtorFunction.class);
    private static final ClassName CLASS_NAME_SIMPLE_LIST_CTOR_FUNCTION = ClassName.get(SimpleListCtorFunction.class);

    @Override
    public TypeManager create(TypeMirror type, TypeProviderUtils utils) {
        if (type.getKind() == TypeKind.DECLARED) {
            DeclaredType declaredType = (DeclaredType) type;
            if (utils.isList(declaredType)) {
                TypeManager elementTypeManager = utils.createTypeManager(declaredType.getTypeArguments().get(0));
                return new CollectionTypeManager(
                        TypeName.get(type),
                        elementTypeManager,
                        elementTypeManager.hasMutator() ? CLASS_NAME_NESTED_LIST_MTOR : CLASS_NAME_SIMPLE_LIST_MTOR,
                        elementTypeManager.hasMutator() ? CLASS_NAME_NESTED_LIST_MTOR_FUNCTION : CLASS_NAME_SIMPLE_LIST_MTOR_FUNCTION,
                        elementTypeManager.hasConstructor() ? CLASS_NAME_NESTED_LIST_CTOR : CLASS_NAME_SIMPLE_LIST_CTOR,
                        elementTypeManager.hasConstructor() ? CLASS_NAME_NESTED_LIST_CTOR_FUNCTION : CLASS_NAME_SIMPLE_LIST_CTOR_FUNCTION,
                        CLASS_NAME_LIST_BUILDER_IMPL);
            }
        }
        return null;
    }
}