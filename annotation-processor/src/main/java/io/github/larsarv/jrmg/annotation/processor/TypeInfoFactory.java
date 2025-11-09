package io.github.larsarv.jrmg.annotation.processor;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeName;
import io.github.larsarv.jrmg.api.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Set;

/**
 * Factory class for creating TypeInfo instances based on a type.
 * It determines the appropriate TypeInfo implementation to use based on whether the component
 * is a primitive, a record annotated with GenerateMutator, a List, or a Set.
 */
public class TypeInfoFactory {
    private static final ClassName CLASS_NAME_LIST_MUTATOR_IMPL = ClassName.get(ListMutatorImpl.class);
    private static final ClassName CLASS_NAME_NESTED_LIST_MUTATOR = ClassName.get(NestedListMutator.class);
    private static final ClassName CLASS_NAME_SIMPLE_LIST_MUTATOR = ClassName.get(SimpleListMutator.class);
    private static final ClassName CLASS_NAME_NESTED_LIST_MUTATE_FUNCTION = ClassName.get(NestedListMutateFunction.class);
    private static final ClassName CLASS_NAME_SIMPLE_LIST_MUTATE_FUNCTION = ClassName.get(SimpleListMutateFunction.class);

    private static final ClassName CLASS_NAME_SET_MUTATOR_IMPL = ClassName.get(SetMutatorImpl.class);
    private static final ClassName CLASS_NAME_NESTED_SET_MUTATOR = ClassName.get(NestedSetMutator.class);
    private static final ClassName CLASS_NAME_SIMPLE_SET_MUTATOR = ClassName.get(SimpleSetMutator.class);
    private static final ClassName CLASS_NAME_NESTED_SET_MUTATE_FUNCTION = ClassName.get(NestedSetMutateFunction.class);
    private static final ClassName CLASS_NAME_SIMPLE_SET_MUTATE_FUNCTION = ClassName.get(SimpleSetMutateFunction.class);


    private final ProcessingEnvironment processingEnv;
    private final TypeElement listTypeElement;
    private final TypeElement setTypeElement;

    /**
     * Creates a new TypeInfoFactory with the given processing environment.
     *
     * @param processingEnv the processing environment used for annotation processing
     */
    public TypeInfoFactory(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
        this.listTypeElement = processingEnv.getElementUtils().getTypeElement(List.class.getCanonicalName());
        this.setTypeElement = processingEnv.getElementUtils().getTypeElement(Set .class.getCanonicalName());
    }

    /**
     * Creates a TypeInfo instance for the given type mirror.
     *
     * @param type the TypeMirror representing the type to process
     * @return a TypeInfo instance representing the type, with appropriate mutator and collection handling
     */
    public TypeInfo createTypeInfo(TypeMirror type) {
        if (type.getKind() == TypeKind.DECLARED) {
            DeclaredType declaredType = (DeclaredType) type;
            Element typeElement = processingEnv.getTypeUtils().asElement(declaredType);
            if (isRecordAnnotatedWithGenerateMutator(typeElement)) {
                // Component is a record annotated with GenerateMutator, add mutate function
                String recordComponentPackageName = processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
                ClassName mutatorClassName = ClassName.get(recordComponentPackageName, typeElement.getSimpleName() + "Mutator");
                return new MutableRecordTypeInfo(
                        TypeName.get(type),
                        mutatorClassName, // TODO Records with Generic arguments?
                        mutatorClassName);
            } else {
                if (isList(declaredType)) {
                    if (hasMutableAsTypeArgument(declaredType)) {
                        // Component is a list of mutable elements
                        TypeInfo elementTypeInfo = createTypeInfo(declaredType.getTypeArguments().get(0));
                        return new CollectionTypeInfo(
                                TypeName.get(type),
                                elementTypeInfo,
                                ParameterizedTypeName.get(CLASS_NAME_NESTED_LIST_MUTATOR, elementTypeInfo.getTypeName(), elementTypeInfo.getMutatorInterfaceTypeName()),
                                CLASS_NAME_LIST_MUTATOR_IMPL,
                                CLASS_NAME_NESTED_LIST_MUTATE_FUNCTION);
                    } else {
                        // Simple list
                        TypeInfo elementTypeInfo = createTypeInfo(declaredType.getTypeArguments().get(0));
                        return new CollectionTypeInfo(
                                TypeName.get(type),
                                elementTypeInfo,
                                ParameterizedTypeName.get(CLASS_NAME_SIMPLE_LIST_MUTATOR, elementTypeInfo.getTypeName()),
                                CLASS_NAME_LIST_MUTATOR_IMPL,
                                CLASS_NAME_SIMPLE_LIST_MUTATE_FUNCTION);
                    }
                } else if (isSet(declaredType)) {
                    if (hasMutableAsTypeArgument(declaredType)) {
                        // Component is a set of mutable elements
                        TypeInfo elementTypeInfo = createTypeInfo(declaredType.getTypeArguments().get(0));
                        return new CollectionTypeInfo(
                                TypeName.get(type),
                                elementTypeInfo,
                                ParameterizedTypeName.get(CLASS_NAME_NESTED_SET_MUTATOR, elementTypeInfo.getTypeName(), elementTypeInfo.getMutatorInterfaceTypeName()),
                                CLASS_NAME_SET_MUTATOR_IMPL,
                                CLASS_NAME_NESTED_SET_MUTATE_FUNCTION);
                    } else {
                        // Simple set
                        TypeInfo elementTypeInfo = createTypeInfo(declaredType.getTypeArguments().get(0));
                        return new CollectionTypeInfo(
                                TypeName.get(type),
                                elementTypeInfo,
                                ParameterizedTypeName.get(CLASS_NAME_SIMPLE_SET_MUTATOR, elementTypeInfo.getTypeName()),
                                CLASS_NAME_SET_MUTATOR_IMPL,
                                CLASS_NAME_SIMPLE_SET_MUTATE_FUNCTION);
                    }
                }
            }
        }

        return new SimpleTypeInfo(TypeName.get(type));
    }

    private static boolean isRecordAnnotatedWithGenerateMutator(Element typeElement) {
        return typeElement.getAnnotation(GenerateMutator.class) != null &&
                typeElement.getKind() == ElementKind.RECORD;
    }
    private boolean isList(DeclaredType declaredType) {
        return processingEnv.getTypeUtils().isSameType(listTypeElement.asType(), declaredType.asElement().asType());
    }
    private boolean isSet(DeclaredType declaredType) {
        return processingEnv.getTypeUtils().isSameType(setTypeElement.asType(), declaredType.asElement().asType());
    }

    private boolean hasMutableAsTypeArgument(DeclaredType declaredType) {
        List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
        if (typeArguments.size() != 1) {
            return false;
        }
        TypeMirror elementType = typeArguments.get(0);
        Element element = processingEnv.getTypeUtils().asElement(elementType);
        if (isRecordAnnotatedWithGenerateMutator(element)) {
            return true;
        }

        if (elementType.getKind() != TypeKind.DECLARED) {
            return false;
        }
        DeclaredType declaredElementType = (DeclaredType)elementType;
        return isList(declaredElementType) || isSet(declaredElementType);
    }

}
