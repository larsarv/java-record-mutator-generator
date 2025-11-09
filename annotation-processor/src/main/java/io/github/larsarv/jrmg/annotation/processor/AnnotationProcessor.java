package io.github.larsarv.jrmg.annotation.processor;

import com.google.auto.service.AutoService;
import com.palantir.javapoet.*;
import io.github.larsarv.jrmg.api.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Function;

/**
 * AnnotationProcessor is a custom annotation processor designed to generate mutator classes for records annotated with
 * {@link io.github.larsarv.jrmg.api.GenerateMutator}. It processes annotations at compile time and generates appropriate
 * mutator implementations based on the annotated record type.
 * <p>
 * The generated mutator has setters and getters for the record's component and supports mutation for three types of
 * record components:
 * <ul>
 * <li>Components that are records annotated with {@link io.github.larsarv.jrmg.api.GenerateMutator}</li>
 * <li>Components that are of type {@code java.util.List} with elements that are records annotated with
 * {@link io.github.larsarv.jrmg.api.GenerateMutator} through a {@code MutableRecordListMutator}</li>
 * <li>Components that are of type {@code java.util.List} with elements that are of other types through a
 * {@code SimpleListMutator}</li>
 * </ul>
 * <p>
 * The processor inspects annotated elements, validates that they are records, and generates corresponding mutator classes.
 */
@SupportedAnnotationTypes("io.github.larsarv.jrmg.api.*")
@AutoService(Processor.class)
public class AnnotationProcessor extends AbstractProcessor {
    private static final String GENERATE_MUTATOR_CLASS_NAME = GenerateMutator.class.getName();

    private TypeElement generateMutatorTypeElement;
    private TypeInfoFactory mutatorTypeInfoFactory;

    /**
     * Constructor for the AnnotationProcessor.
     */
    public AnnotationProcessor() {
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        generateMutatorTypeElement = processingEnv.getElementUtils().getTypeElement(GENERATE_MUTATOR_CLASS_NAME);
        mutatorTypeInfoFactory = new TypeInfoFactory(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //printMessage(Diagnostic.Kind.NOTE,"Java Record Mutator Generator annotation processor invoked.");

        for (TypeElement annotation : annotations) {
            if (annotation.getKind() != ElementKind.ANNOTATION_TYPE) {
                continue;
            }

            Set<? extends Element> annotatedElements
                    = roundEnv.getElementsAnnotatedWith(annotation);

            for (Element element : annotatedElements) {
                processElement(annotation, element);
            }

        }

        return true;
    }

    private void processElement(TypeElement annotation, Element element) {
        if (processingEnv.getTypeUtils().isSameType(
                annotation.asType(),
                generateMutatorTypeElement.asType())
        ) {
            TypeElement typeElement = (TypeElement) element;
            // Make sure the annotation is on a record
            if (element.getKind() != ElementKind.RECORD) {
                printMessage(Diagnostic.Kind.ERROR, "GenerateMutator annotation is only valid for records.", element);
                return;
            }
            processGenerateMutator(typeElement);
        }
    }

    private void processGenerateMutator(TypeElement recordElement) {

        PackageElement recordElementPackageElement = processingEnv.getElementUtils().getPackageOf(recordElement);
        String recordElementPackageName = recordElementPackageElement.getQualifiedName().toString();
        ClassName mutatorClassName = ClassName.get(recordElementPackageName, recordElement.getSimpleName() + "Mutator");
        ClassName recordClassName = ClassName.get(recordElement);

        TypeSpec.Builder mutatorClassBuilder = TypeSpec.classBuilder(mutatorClassName)
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(Mutator.class), recordClassName))
                .addModifiers(Modifier.PUBLIC);


        addConstructor(mutatorClassBuilder, recordElement, recordClassName);
        addComponentMethods(mutatorClassBuilder, recordElement, mutatorClassName);
        addFactoryMethods(mutatorClassBuilder, mutatorClassName, recordClassName);
        addBuildMethod(recordElement, mutatorClassBuilder, recordClassName);

        JavaFile javaFile = JavaFile.builder(recordElementPackageName, mutatorClassBuilder.build())
                .build();

        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            printMessage(Diagnostic.Kind.ERROR, e.getMessage(), recordElement);
        }
    }

    private void addComponentMethods(TypeSpec.Builder mutatorClassBuilder, TypeElement recordElement, TypeName mutatorClassName) {
        for (RecordComponentElement recordComponentElement : recordElement.getRecordComponents()) {
            String componentName = recordComponentElement.getSimpleName().toString();

            TypeInfo typeInfo = mutatorTypeInfoFactory.createTypeInfo(recordComponentElement.asType());
            typeInfo.contributeToMutator(mutatorClassBuilder, componentName, mutatorClassName);
        }
    }

    private static void addBuildMethod(
            TypeElement recordElement,
            TypeSpec.Builder mutatorClassBuilder,
            ClassName recordClassName
    ) {
        List<String> fieldNameList = creteFieldNameList(recordElement);
        mutatorClassBuilder.addMethod(MethodSpec.methodBuilder("build")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(recordClassName)
                .addStatement("return new $T($L)", recordClassName, String.join(", ", fieldNameList))
                .build());
    }

    private static void addFactoryMethods(
            TypeSpec.Builder mutatorClassBuilder,
            ClassName mutatorClassName,
            ClassName recordClassName
    ) {
        // No argument factory method
        mutatorClassBuilder.addMethod(MethodSpec.methodBuilder("mutator")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(mutatorClassName)
                .addStatement("return new $T(null)", mutatorClassName)
                .build());

        // Factory method
        mutatorClassBuilder.addMethod(MethodSpec.methodBuilder("mutator")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(recordClassName, "value")
                .returns(mutatorClassName)
                .addStatement("return new $T(value)", mutatorClassName)
                .build());
    }

    private static void addConstructor(
            TypeSpec.Builder mutatorClassBuilder,
            TypeElement recordElement,
            ClassName recordClassName
    ) {
        CodeBlock.Builder constructorCodeBuilder = CodeBlock.builder();
        constructorCodeBuilder.beginControlFlow("if (value != null)");
        for (RecordComponentElement recordComponentElement : recordElement.getRecordComponents()) {
            String componentName = recordComponentElement.getSimpleName().toString();
            String fieldName = toFiledName(componentName);

            constructorCodeBuilder.addStatement("this.$N = value.$N()", fieldName, componentName);
        }
        constructorCodeBuilder.endControlFlow();

        mutatorClassBuilder.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addParameter(recordClassName, "value")
                .addCode(constructorCodeBuilder.build())
                .build());
    }

    private static List<String> creteFieldNameList(TypeElement recordElement) {
        List<String> fieldList = new ArrayList<>();
        for (RecordComponentElement recordComponentElement : recordElement.getRecordComponents()) {
            String componentName = recordComponentElement.getSimpleName().toString();
            String fieldName = toFiledName(componentName);
            fieldList.add("this." + fieldName);
        }
        return fieldList;
    }

    private static String toFiledName(String componentName) {
        return componentName.substring(0, 1).toLowerCase(Locale.ROOT) + componentName.substring(1);
    }

    void printMessage(Diagnostic.Kind kind, String message) {
        processingEnv.getMessager().printMessage(kind, message);
    }

    void printMessage(Diagnostic.Kind kind, String message, Element element) {
        processingEnv.getMessager().printMessage(kind, message, element);
    }
}
