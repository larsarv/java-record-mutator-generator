package io.github.larsarv.jrmg.annotation.processor;

import com.google.auto.service.AutoService;
import io.github.larsarv.jrmg.annotation.processor.type.manager.TypeManagerFactory;
import io.github.larsarv.jrmg.api.GenerateCtor;
import io.github.larsarv.jrmg.api.GenerateCtorAndMtor;
import io.github.larsarv.jrmg.api.GenerateMtor;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

/**
 * AnnotationProcessor is a custom annotation processor designed to generate mutator and constructor classes for records annotated with
 * {@link GenerateMtor} and {@link GenerateCtor}. It processes annotations at compile time and generates appropriate
 * implementations based on the annotated record type.
 */
@SupportedAnnotationTypes({
        "io.github.larsarv.jrmg.api.GenerateMtor",
        "io.github.larsarv.jrmg.api.GenerateCtor",
        "io.github.larsarv.jrmg.api.GenerateCtorAndMtor"
})
@AutoService(Processor.class)
public class AnnotationProcessor extends AbstractProcessor {
    private static final String GENERATE_MTOR_CLASS_NAME = GenerateMtor.class.getName();
    private static final String GENERATE_CTOR_CLASS_NAME = GenerateCtor.class.getName();
    private static final String GENERATE_CTOR_AND_MTOR_CLASS_NAME = GenerateCtorAndMtor.class.getName();

    private TypeElement generateMtorTypeElement;
    private TypeElement generateCtorTypeElement;
    private TypeElement generateCtorAndMtorTypeElement;
    private MtorGenerator mtorGenerator;
    private CtorGenerator ctorGenerator;

    /**
     * Default constructor for the AnnotationProcessor.
     * <p>
     * This constructor is required by the service loader mechanism to instantiate the processor.
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
        generateMtorTypeElement = processingEnv.getElementUtils().getTypeElement(GENERATE_MTOR_CLASS_NAME);
        generateCtorTypeElement = processingEnv.getElementUtils().getTypeElement(GENERATE_CTOR_CLASS_NAME);
        generateCtorAndMtorTypeElement = processingEnv.getElementUtils().getTypeElement(GENERATE_CTOR_AND_MTOR_CLASS_NAME);
        TypeManagerFactory factory = TypeManagerFactory.createTypeManager(processingEnv);
        mtorGenerator = new MtorGenerator(processingEnv, factory);
        ctorGenerator = new CtorGenerator(processingEnv, factory);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
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
        if (element.getKind() != ElementKind.RECORD) {
            printMessage(Diagnostic.Kind.ERROR, element.getSimpleName() + " annotation is only valid for records.", element);
            return;
        }
        if (shouldGenerateMtor(annotation)) {
            TypeElement typeElement = (TypeElement) element;
            mtorGenerator.process(typeElement);
        }
        if (shouldGenerateCtor(annotation)) {
            TypeElement typeElement = (TypeElement) element;
            ctorGenerator.process(typeElement);
        }
    }

    private boolean shouldGenerateMtor(TypeElement annotation) {
        return isSameType(annotation, generateMtorTypeElement)
                || isSameType(annotation, generateCtorAndMtorTypeElement);
    }

    private boolean shouldGenerateCtor(TypeElement annotation) {
        return isSameType(annotation, generateCtorTypeElement)
                || isSameType(annotation, generateCtorAndMtorTypeElement);
    }

    private boolean isSameType(TypeElement annotation, TypeElement generateMutatorTypeElement) {
        return processingEnv.getTypeUtils().isSameType(
                annotation.asType(),
                generateMutatorTypeElement.asType());
    }

    /**
     * Prints a message to the messager.
     *
     * @param kind    the message kind
     * @param message the message text
     * @param element the element to link the message to
     */
    void printMessage(Diagnostic.Kind kind, String message, Element element) {
        processingEnv.getMessager().printMessage(kind, message, element);
    }
}