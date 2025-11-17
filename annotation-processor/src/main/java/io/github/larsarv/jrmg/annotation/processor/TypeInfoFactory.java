package io.github.larsarv.jrmg.annotation.processor;

import com.palantir.javapoet.ClassName;
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
import java.util.Map;
import java.util.Set;

/**
 * Factory class for creating TypeInfo instances based on a type.
 * It determines the appropriate TypeInfo implementation to use based on whether the component
 * is a primitive, a record annotated with GenerateMutator, a List, a Set, or a Map.
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

    private static final ClassName CLASS_NAME_MAP_MUTATOR_IMPL = ClassName.get(MapMutatorImpl.class);
    private static final ClassName CLASS_NAME_NESTED_MAP_MUTATOR = ClassName.get(NestedMapMutator.class);
    private static final ClassName CLASS_NAME_SIMPLE_MAP_MUTATOR = ClassName.get(SimpleMapMutator.class);
    private static final ClassName CLASS_NAME_NESTED_MAP_MUTATE_FUNCTION = ClassName.get(NestedMapMutateFunction.class);
    private static final ClassName CLASS_NAME_SIMPLE_MAP_MUTATE_FUNCTION = ClassName.get(SimpleMapMutateFunction.class);


    private final ProcessingEnvironment processingEnv;
    private final TypeElement listTypeElement;
    private final TypeElement setTypeElement;
    private final TypeElement mapTypeElement;

    /**
     * Creates a new TypeInfoFactory with the given processing environment.
     *
     * @param processingEnv the processing environment used for annotation processing
     */
    public TypeInfoFactory(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
        this.listTypeElement = processingEnv.getElementUtils().getTypeElement(List.class.getCanonicalName());
        this.setTypeElement = processingEnv.getElementUtils().getTypeElement(Set .class.getCanonicalName());
        this.mapTypeElement = processingEnv.getElementUtils().getTypeElement(Map.class.getCanonicalName());
    }

    /**
     * Creates a TypeInfo instance for the given type mirror.
     *
     * @param type the TypeMirror representing the type to process
     * @return a TypeInfo instance representing the type, with appropriate mutator and collection handling
     */
    public TypeInfo createTypeInfo(TypeMirror type) {
        TypeName typeName = TypeName.get(type);
        if (type.getKind() == TypeKind.DECLARED) {
            DeclaredType declaredType = (DeclaredType) type;
            Element typeElement = processingEnv.getTypeUtils().asElement(declaredType);
            if (isRecordAnnotatedWithGenerateMutator(typeElement)) {
                // Component is a record annotated with GenerateMutator, add mutate function
                String recordComponentPackageName = processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
                ClassName mutatorClassName = ClassName.get(recordComponentPackageName, typeElement.getSimpleName() + "Mutator");
                String firstConponentName = getFirstRecordComponent(typeElement);
                return new MutableRecordTypeInfo(
                        typeName,
                        mutatorClassName, // TODO Records with Generic arguments?
                        mutatorClassName,
                        firstConponentName);
            } else {
                if (isList(declaredType)) {
                    TypeInfo elementTypeInfo = createTypeInfo(declaredType.getTypeArguments().get(0));
                    if (elementTypeInfo.hasMutator()) {
                        // Component is a list of mutable elements
                        return new CollectionTypeInfo(
                                typeName,
                                elementTypeInfo,
                                CLASS_NAME_NESTED_LIST_MUTATOR,
                                CLASS_NAME_LIST_MUTATOR_IMPL,
                                CLASS_NAME_NESTED_LIST_MUTATE_FUNCTION);
                    } else {
                        // Simple list
                        return new CollectionTypeInfo(
                                typeName,
                                elementTypeInfo,
                                CLASS_NAME_SIMPLE_LIST_MUTATOR,
                                CLASS_NAME_LIST_MUTATOR_IMPL,
                                CLASS_NAME_SIMPLE_LIST_MUTATE_FUNCTION);
                    }
                } else if (isSet(declaredType)) {
                    TypeInfo elementTypeInfo = createTypeInfo(declaredType.getTypeArguments().get(0));
                    if (elementTypeInfo.hasMutator()) {
                        // Component is a set of mutable elements
                        return new CollectionTypeInfo(
                                typeName,
                                elementTypeInfo,
                                CLASS_NAME_NESTED_SET_MUTATOR,
                                CLASS_NAME_SET_MUTATOR_IMPL,
                                CLASS_NAME_NESTED_SET_MUTATE_FUNCTION);
                    } else {
                        // Simple set
                        return new CollectionTypeInfo(
                                typeName,
                                elementTypeInfo,
                                CLASS_NAME_SIMPLE_SET_MUTATOR,
                                CLASS_NAME_SET_MUTATOR_IMPL,
                                CLASS_NAME_SIMPLE_SET_MUTATE_FUNCTION);
                    }
                } else if (isMap(declaredType)) {
                    List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
                    if (typeArguments.size() == 2) {
                        TypeInfo keyTypeInfo = createTypeInfo(typeArguments.get(0));
                        TypeInfo valueTypeInfo = createTypeInfo(typeArguments.get(1));
                        boolean hasValueMutator = valueTypeInfo.hasMutator();
                        
                        if (hasValueMutator) {
                            // Map with mutable values
                            return new MapTypeInfo(
                                    typeName,
                                    keyTypeInfo,
                                    valueTypeInfo,
                                    CLASS_NAME_NESTED_MAP_MUTATOR,
                                    CLASS_NAME_MAP_MUTATOR_IMPL,
                                    CLASS_NAME_NESTED_MAP_MUTATE_FUNCTION);
/*
                            return new MapTypeInfo(
                                    typeName,
                                    valueTypeInfo,
                                    ParameterizedTypeName.get(CLASS_NAME_NESTED_MAP_MUTATOR, // TODO Move into MapTypeInfo
                                            keyTypeInfo.getTypeName(),
                                            valueTypeInfo.getTypeName(),
                                            valueTypeInfo.getMutatorInterfaceTypeName(),
                                            valueTypeInfo.getMutatorInterfaceTypeName()),
                                    CLASS_NAME_MAP_MUTATOR_IMPL,
                                    ParameterizedTypeName.get( // TODO Move into MapTypeInfo
                                            CLASS_NAME_NESTED_MAP_MUTATE_FUNCTION,
                                            keyTypeInfo.getTypeName(),
                                            valueTypeInfo.getTypeName(),
                                            valueTypeInfo.getMutatorInterfaceTypeName(),
                                            valueTypeInfo.getMutatorInterfaceTypeName()));
 */
                        } else {
                            // Simple map
                            return new MapTypeInfo(
                                    typeName,
                                    keyTypeInfo,
                                    valueTypeInfo,
                                    CLASS_NAME_SIMPLE_MAP_MUTATOR,
                                    CLASS_NAME_MAP_MUTATOR_IMPL,
                                    CLASS_NAME_SIMPLE_MAP_MUTATE_FUNCTION);
/*
                            return new MapTypeInfo(
                                    typeName,
                                    valueTypeInfo,
                                    ParameterizedTypeName.get(CLASS_NAME_SIMPLE_MAP_MUTATOR,
                                            keyTypeInfo.getTypeName(),
                                            valueTypeInfo.getTypeName()),
                                    CLASS_NAME_MAP_MUTATOR_IMPL,
                                    ParameterizedTypeName.get(
                                            CLASS_NAME_SIMPLE_MAP_MUTATE_FUNCTION,
                                            keyTypeInfo.getTypeName(),
                                            valueTypeInfo.getTypeName()));

 */
                        }
                    }
                }
            }
        }

        return new SimpleTypeInfo(typeName);
    }

    private String getFirstRecordComponent(Element element) {
        if (element.getKind() == ElementKind.RECORD) {
            TypeElement typeElement = (TypeElement)element;
            if (!typeElement.getRecordComponents().isEmpty()) {
                return typeElement.getRecordComponents().get(0).getSimpleName().toString();
            }
        }
        return null;
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
    private boolean isMap(DeclaredType declaredType) {
        return processingEnv.getTypeUtils().isSameType(mapTypeElement.asType(), declaredType.asElement().asType());
    }

    private boolean hasMutableAsTypeArgument(DeclaredType declaredType) {
        List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
        if (typeArguments.size() == 1) {
            // For List and Set
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
        return false;
    }

}
