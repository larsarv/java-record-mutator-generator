package io.github.larsarv.jrmg.annotation.processor;

import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.TypeName;
import com.palantir.javapoet.TypeSpec;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Utility class for common code generation tasks.
 * <p>
 * Provides helper methods for writing Java files, handling package names,
 * converting names, and managing fields for generated classes.
 */
public final class GeneratorUtils {
    private final ProcessingEnvironment processingEnv;

    /**
     * Constructs a new GeneratorUtils.
     *
     * @param processingEnv the processing environment
     */
    public GeneratorUtils(ProcessingEnvironment processingEnv) {
        // Utility class
        this.processingEnv = processingEnv;
    }

    /**
     * Writes a generated Java file to the filer.
     *
     * @param packageName the package name of the class
     * @param typeSpec    the TypeSpec of the class to write
     * @param element     the element associated with the file (for error reporting)
     */
    public void writeJavaFile(String packageName, TypeSpec typeSpec, Element element) {
        JavaFile javaFile = JavaFile.builder(packageName, typeSpec).build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            printMessage(Diagnostic.Kind.ERROR, e.getMessage(), element);
        }
    }

    /**
     * Prints a message to the messager.
     *
     * @param kind    the message kind
     * @param message the message text
     * @param element the element to link the message to
     */
    public void printMessage(Diagnostic.Kind kind, String message, Element element) {
        processingEnv.getMessager().printMessage(kind, message, element);
    }

    /**
     * Gets the package name of a type element.
     *
     * @param recordElement the type element
     * @return the package name
     */
    public String getPackageName(TypeElement recordElement) {
        PackageElement recordElementPackageElement = processingEnv.getElementUtils().getPackageOf(recordElement);
        return recordElementPackageElement.getQualifiedName().toString();
    }

    /**
     * Converts a component name to a field name (camelCase).
     *
     * @param componentName the component name
     * @return the field name
     */
    public String toFieldName(String componentName) {
        return componentName.substring(0, 1).toLowerCase(Locale.ROOT) + componentName.substring(1);
    }

    /**
     * Creates a list of field names for a record, prefixed with "this.".
     *
     * @param recordElement the record element
     * @return a list of field access strings
     */
    public List<String> createFieldNameList(TypeElement recordElement) {
        List<String> fieldList = new ArrayList<>();
        for (RecordComponentElement recordComponentElement : recordElement.getRecordComponents()) {
            String componentName = recordComponentElement.getSimpleName().toString();
            String fieldName = toFieldName(componentName);
            fieldList.add("this." + fieldName);
        }
        return fieldList;
    }

    /**
     * Adds private fields to a class builder corresponding to the record components.
     *
     * @param classBuilder  the class builder to add fields to
     * @param recordElement the record element containing the components
     */
    public void addFields(TypeSpec.Builder classBuilder, TypeElement recordElement) {
        for (RecordComponentElement recordComponentElement : recordElement.getRecordComponents()) {
            String componentName = recordComponentElement.getSimpleName().toString();
            String fieldName = toFieldName(componentName);
            TypeName typeName = TypeName.get(recordComponentElement.asType());

            FieldSpec field = FieldSpec.builder(
                    typeName,
                    fieldName,
                    Modifier.PRIVATE).build();

            classBuilder.addField(field);
        }
    }
}