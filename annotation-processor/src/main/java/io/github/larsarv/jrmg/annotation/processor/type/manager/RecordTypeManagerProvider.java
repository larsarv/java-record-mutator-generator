package io.github.larsarv.jrmg.annotation.processor.type.manager;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.TypeName;

import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

/**
 * TypeManagerProvider for Record types.
 * <p>
 * Creates a {@link RecordWithBuilderTypeManager} if the record is annotated with
 * {@link io.github.larsarv.jrmg.api.GenerateMtor}, {@link io.github.larsarv.jrmg.api.GenerateCtor},
 * or {@link io.github.larsarv.jrmg.api.GenerateCtorAndMtor}.
 */
public class RecordTypeManagerProvider implements TypeManagerProvider {
    @Override
    public TypeManager create(TypeMirror type, TypeProviderUtils utils) {
        if (type.getKind() == TypeKind.DECLARED) {
            DeclaredType declaredType = (DeclaredType) type;
            Element typeElement = utils.getTypeElement(declaredType);

            if (utils.isRecordAnnotatedWithBuilder(typeElement)) {
                String recordComponentPackageName = utils.getPackageName(typeElement);
                ClassName mtorClassName = utils.isRecordAnnotatedWithGenerateMtor(typeElement) ? ClassName.get(recordComponentPackageName, typeElement.getSimpleName() + "Mtor") : null;
                ClassName ctorClassName = utils.isRecordAnnotatedWithGenerateCtor(typeElement) ? ClassName.get(recordComponentPackageName, typeElement.getSimpleName() + "Ctor") : null;
                String firstComponentName = utils.getFirstRecordComponent(typeElement);
                return new RecordWithBuilderTypeManager(
                        TypeName.get(type),
                        mtorClassName,
                        mtorClassName,
                        ctorClassName,
                        ctorClassName,
                        firstComponentName);
            }
        }
        return null;
    }
}