package io.github.larsarv.jrmg.annotation.processor.type.manager;

import io.github.larsarv.jrmg.api.GenerateCtor;
import io.github.larsarv.jrmg.api.GenerateCtorAndMtor;
import io.github.larsarv.jrmg.api.GenerateMtor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Utility class for TypeManagerProviders.
 * <p>
 * Provides helper methods for type analysis, annotation checking, and accessing the processing environment.
 */
public class TypeProviderUtils {
    private final ProcessingEnvironment processingEnv;
    private final TypeManagerFactory typeManagerFactory;

    private final TypeElement listTypeElement;
    private final TypeElement setTypeElement;
    private final TypeElement mapTypeElement;

    /**
     * Constructs a new TypeProviderUtils.
     *
     * @param processingEnv      the processing environment
     * @param typeManagerFactory the factory for creating TypeManagers
     */
    public TypeProviderUtils(ProcessingEnvironment processingEnv, TypeManagerFactory typeManagerFactory) {
        this.processingEnv = processingEnv;
        this.typeManagerFactory = typeManagerFactory;

        this.listTypeElement = processingEnv.getElementUtils().getTypeElement(List.class.getCanonicalName());
        this.setTypeElement = processingEnv.getElementUtils().getTypeElement(Set.class.getCanonicalName());
        this.mapTypeElement = processingEnv.getElementUtils().getTypeElement(Map.class.getCanonicalName());
    }

    /**
     * Creates a TypeManager for the given type using the factory.
     *
     * @param type the type to create a manager for
     * @return the TypeManager
     */
    public TypeManager createTypeManager(TypeMirror type) {
        return typeManagerFactory.createTypeManager(type);
    }

    /**
     * Gets the name of the first component of a record.
     *
     * @param element the record element
     * @return the name of the first component, or null if not a record or empty
     */
    public String getFirstRecordComponent(Element element) {
        if (element.getKind() == ElementKind.RECORD) {
            TypeElement typeElement = (TypeElement) element;
            if (!typeElement.getRecordComponents().isEmpty()) {
                return typeElement.getRecordComponents().get(0).getSimpleName().toString();
            }
        }
        return null;
    }

    /**
     * Checks if a record is annotated with @GenerateMtor or @GenerateCtorAndMtor.
     *
     * @param typeElement the element to check
     * @return true if annotated, false otherwise
     */
    public boolean isRecordAnnotatedWithGenerateMtor(Element typeElement) {
        return (typeElement.getAnnotation(GenerateMtor.class) != null ||
                typeElement.getAnnotation(GenerateCtorAndMtor.class) != null) &&
                typeElement.getKind() == ElementKind.RECORD;
    }

    /**
     * Checks if a record is annotated with @GenerateCtor or @GenerateCtorAndMtor.
     *
     * @param typeElement the element to check
     * @return true if annotated, false otherwise
     */
    public boolean isRecordAnnotatedWithGenerateCtor(Element typeElement) {
        return (typeElement.getAnnotation(GenerateCtor.class) != null ||
                typeElement.getAnnotation(GenerateCtorAndMtor.class) != null) &&
                typeElement.getKind() == ElementKind.RECORD;
    }

    /**
     * Checks if a record is annotated with any of the generator annotations.
     *
     * @param typeElement the element to check
     * @return true if annotated, false otherwise
     */
    public boolean isRecordAnnotatedWithBuilder(Element typeElement) {
        return (typeElement.getAnnotation(GenerateMtor.class) != null ||
                typeElement.getAnnotation(GenerateCtor.class) != null ||
                typeElement.getAnnotation(GenerateCtorAndMtor.class) != null) &&
                typeElement.getKind() == ElementKind.RECORD;
    }

    /**
     * Checks if the given type is a List.
     *
     * @param declaredType the type to check
     * @return true if it is a List
     */
    public boolean isList(DeclaredType declaredType) {
        return processingEnv.getTypeUtils().isSameType(listTypeElement.asType(), declaredType.asElement().asType());
    }

    /**
     * Checks if the given type is a Set.
     *
     * @param declaredType the type to check
     * @return true if it is a Set
     */
    public boolean isSet(DeclaredType declaredType) {
        return processingEnv.getTypeUtils().isSameType(setTypeElement.asType(), declaredType.asElement().asType());
    }

    /**
     * Checks if the given type is a Map.
     *
     * @param declaredType the type to check
     * @return true if it is a Map
     */
    public boolean isMap(DeclaredType declaredType) {
        return processingEnv.getTypeUtils().isSameType(mapTypeElement.asType(), declaredType.asElement().asType());
    }

    /**
     * Gets the element corresponding to a declared type.
     *
     * @param declaredType the declared type
     * @return the element
     */
    public Element getTypeElement(DeclaredType declaredType) {
        return processingEnv.getTypeUtils().asElement(declaredType);
    }

    /**
     * Gets the package name of an element.
     *
     * @param typeElement the element
     * @return the package name
     */
    public String getPackageName(Element typeElement) {
        return processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
    }
}
