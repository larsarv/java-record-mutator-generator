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
 *
 * The processor inspects annotated elements, validates that they are records, and generates corresponding mutator classes.
 */
@SupportedAnnotationTypes("io.github.larsarv.jrmg.api.*")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
@AutoService(Processor.class)
public class AnnotationProcessor extends AbstractProcessor {
    private static final String GENERATE_MUTATOR_CLASS_NAME = GenerateMutator.class.getName();
    private static final ClassName MUTABLERECORDLISTMUTATORIMPL_CLASSNAME = ClassName.get(MutableRecordListMutatorImpl.class);
    private static final ClassName SIMPLELISTMUTATORIMPL_CLASSNAME = ClassName.get(SimpleListMutatorImpl.class);
    private static final ClassName FUNCTION_CLASSNAME = ClassName.get(Function.class);

    /**
     * Constructor for the AnnotationProcessor.
     */
    public AnnotationProcessor() {
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
        String annotationClassName = annotation.getQualifiedName().toString();

        if (GENERATE_MUTATOR_CLASS_NAME.equals(annotationClassName)) {
            TypeElement typeElement = (TypeElement) element;
            // Make sure the annotation is on a record
            if (element.getKind() != ElementKind.RECORD) {
                printMessage(Diagnostic.Kind.ERROR, "GenerateMutator annotation is only valid for records.", element);
                return;
            }
            processGenerateMutator(typeElement);
        }
    }

    private static String toFiledName(String componentName) {
        return componentName.substring(0, 1).toLowerCase(Locale.ROOT) + componentName.substring(1);
    }

    private static String toMethodName(String prefix, String componentName) {
        return prefix + componentName.substring(0, 1).toUpperCase(Locale.ROOT) + componentName.substring(1);
    }

    private void processGenerateMutator(TypeElement recordElement) {

        PackageElement recordElementPackageElement = processingEnv.getElementUtils().getPackageOf(recordElement);
        String recordElementPackageName = recordElementPackageElement.getQualifiedName().toString();
        ClassName mutatorClassName = ClassName.get(recordElementPackageName, recordElement.getSimpleName() + "Mutator");
        ClassName recordClassName = ClassName.get(recordElement);

        TypeSpec.Builder mutatorClassBuilder = TypeSpec.classBuilder(mutatorClassName)
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(RecordMutator.class), recordClassName))
                .addModifiers(Modifier.PUBLIC);

        CodeBlock.Builder constructorCodeBuilder = CodeBlock.builder();
        constructorCodeBuilder.beginControlFlow("if (value != null)");

        List<String> fieldList = new ArrayList<>();
        for (RecordComponentElement recordComponentElement : recordElement.getRecordComponents()) {
            TypeName componentType = TypeName.get(recordComponentElement.asType());
            String componentName = recordComponentElement.getSimpleName().toString();
            String fieldName = toFiledName(componentName);

            fieldList.add("this." + fieldName);
            FieldSpec field = FieldSpec.builder(
                    componentType,
                    fieldName,
                    Modifier.PRIVATE).build();

            constructorCodeBuilder.addStatement("this.$N = value.$N()", fieldName, componentName);

            MethodSpec setterMethod = MethodSpec.methodBuilder(toMethodName("set", componentName))
                    .addModifiers(Modifier.PUBLIC)
                    .returns(mutatorClassName)
                    .addParameter(componentType, "value")
                    .addStatement("this.$N = value", fieldName)
                    .addStatement("return this")
                    .build();

            MethodSpec getterMethod = MethodSpec.methodBuilder(toMethodName("get", componentName))
                    .addModifiers(Modifier.PUBLIC)
                    .returns(componentType)
                    .addStatement("return $N", fieldName)
                    .build();

            mutatorClassBuilder
                    .addField(field)
                    .addMethod(setterMethod)
                    .addMethod(getterMethod);


            TypeMirror recordComponentType = recordComponentElement.asType();
            if (recordComponentType.getKind() == TypeKind.DECLARED) {
                DeclaredType declaredType = (DeclaredType) recordComponentType;
                Element typeElement = processingEnv.getTypeUtils().asElement(declaredType);
                if (typeElement.getAnnotation(GenerateMutator.class) != null) {
                    // Component is a record annotated with GenerateMutator, add mutate function
                    String recordComponentPackageName = processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
                    ClassName recordComponentClassName = ClassName.get(recordComponentPackageName, typeElement.getSimpleName() + "Mutator");

                    ParameterizedTypeName parameterType = ParameterizedTypeName.get(ClassName.get(Function.class), recordComponentClassName, recordComponentClassName);
                    MethodSpec mutateMethod = MethodSpec.methodBuilder(toMethodName("mutate", componentName))
                            .addModifiers(Modifier.PUBLIC)
                            .returns(mutatorClassName)
                            .addParameter(parameterType, "mutateFunction")
                            .addStatement("this.$N = mutateFunction.apply(new $T(this.$N)).build()", fieldName, recordComponentClassName, fieldName)
                            .addStatement("return this")
                            .build();

                    mutatorClassBuilder.addMethod(mutateMethod);
                } else {
                    TypeElement listTypeElement = processingEnv.getElementUtils().getTypeElement(List.class.getCanonicalName());
                    if (processingEnv.getTypeUtils().isSameType(listTypeElement.asType(), declaredType.asElement().asType())) {
                        if (declaredType.getTypeArguments().size() == 1
                                && declaredType.getTypeArguments().getFirst().getKind() == TypeKind.DECLARED
                                && processingEnv.getTypeUtils().asElement(declaredType.getTypeArguments().getFirst()).getAnnotation(GenerateMutator.class) != null) {
                            // Component is a list of elements annotated with GenerateMutator, add modifier function
                            TypeName typeName = TypeName.get(declaredType.getTypeArguments().getFirst());
                            Element listElementTypeElement = processingEnv.getTypeUtils().asElement(declaredType.getTypeArguments().getFirst());
                            String listElementPackageName = processingEnv.getElementUtils().getPackageOf(listElementTypeElement).getQualifiedName().toString();
                            ClassName listElementMutatorClassName = ClassName.get(listElementPackageName, listElementTypeElement.getSimpleName() + "Mutator");
                            ClassName listElementClassName = ClassName.get(listElementPackageName, listElementTypeElement.getSimpleName().toString());

                            MethodSpec mutateMethod = MethodSpec.methodBuilder(toMethodName("mutate", componentName))
                                    .addModifiers(Modifier.PUBLIC)
                                    .returns(mutatorClassName)
                                    .addParameter(
                                            ParameterizedTypeName.get(
                                                    ClassName.get(MutableRecordListMutateFunction.class),
                                                    typeName, listElementMutatorClassName),
                                            "mutateFunction")
                                    // Function<InvoiceLineItem,InvoiceLineItemMutator>
                                    .addStatement("$T<$T,$T> constructor = $T::new", FUNCTION_CLASSNAME, listElementClassName, listElementMutatorClassName, listElementMutatorClassName)
                                    .addStatement("this.$N = mutateFunction.mutate(new $T<>(this.$N, constructor)).build()", fieldName, MUTABLERECORDLISTMUTATORIMPL_CLASSNAME, fieldName)
                                    .addStatement("return this")
                                    .build();

                            mutatorClassBuilder.addMethod(mutateMethod);

                        } else {
                            // Simple list
                            TypeName typeName = TypeName.get(declaredType.getTypeArguments().getFirst());
                            Element listElementTypeElement = processingEnv.getTypeUtils().asElement(declaredType.getTypeArguments().getFirst());
                            String listElementPackageName = processingEnv.getElementUtils().getPackageOf(listElementTypeElement).getQualifiedName().toString();
                            ClassName listElementClassName = ClassName.get(listElementPackageName, listElementTypeElement.getSimpleName().toString());

                            MethodSpec mutateMethod = MethodSpec.methodBuilder(toMethodName("mutate", componentName))
                                    .addModifiers(Modifier.PUBLIC)
                                    .returns(mutatorClassName)
                                    .addParameter(
                                            ParameterizedTypeName.get(
                                                    ClassName.get(SimpleListMutateFunction.class),
                                                    typeName),
                                            "mutateFunction")
                                    // Function<InvoiceLineItem,InvoiceLineItemMutator>
                                    .addStatement("this.$N = mutateFunction.mutate(new $T<>(this.$N)).build()", fieldName, SIMPLELISTMUTATORIMPL_CLASSNAME, fieldName)
                                    .addStatement("return this")
                                    .build();

                            mutatorClassBuilder.addMethod(mutateMethod);

                        }
                    }
                }
            }
        }
        constructorCodeBuilder.endControlFlow();

        // Simple no args constructor
        mutatorClassBuilder.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .build());
        // Constructor taking value
        mutatorClassBuilder.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(recordClassName, "value")
                .addCode(constructorCodeBuilder.build())
                .build());

        mutatorClassBuilder.addMethod(MethodSpec.methodBuilder("build")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(recordClassName)
                .addStatement("return new $T($L)", recordClassName, String.join(", ", fieldList))
                .build());

        JavaFile javaFile = JavaFile.builder(recordElementPackageName, mutatorClassBuilder.build())
                .build();

        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            printMessage(Diagnostic.Kind.ERROR, e.getMessage(), recordElement);
        }
    }


    void printMessage(Diagnostic.Kind kind, String message) {
        processingEnv.getMessager().printMessage(kind, message);
    }

    void printMessage(Diagnostic.Kind kind, String message, Element element) {
        processingEnv.getMessager().printMessage(kind, message, element);
    }
}
