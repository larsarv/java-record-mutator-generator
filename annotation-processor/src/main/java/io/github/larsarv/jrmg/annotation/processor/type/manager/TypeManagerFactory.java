package io.github.larsarv.jrmg.annotation.processor.type.manager;

import com.palantir.javapoet.TypeName;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;

/**
 * Factory for creating TypeManager instances.
 * <p>
 * Uses a list of registered {@link TypeManagerProvider}s to determine the appropriate
 * {@link TypeManager} for a given type.
 */
public class TypeManagerFactory {
    private final List<TypeManagerProvider> providers = new ArrayList<>();
    private final TypeProviderUtils utils;

    /**
     * Creates a new TypeManagerFactory.
     *
     * @param processingEnv the processing environment
     */
    public TypeManagerFactory(ProcessingEnvironment processingEnv) {
        this.utils = new TypeProviderUtils(processingEnv, this);
    }

    /**
     * Creates and initializes a TypeManagerFactory with default providers.
     *
     * @param processingEnv the processing environment
     * @return a new TypeManagerFactory instance
     */
    public static TypeManagerFactory createTypeManager(ProcessingEnvironment processingEnv) {
        TypeManagerFactory factory = new TypeManagerFactory(processingEnv);
        factory.registerProvider(new RecordTypeManagerProvider());
        factory.registerProvider(new ListTypeManagerProvider());
        factory.registerProvider(new SetTypeManagerProvider());
        factory.registerProvider(new MapTypeManagerProvider());
        return factory;
    }

    private void registerProvider(TypeManagerProvider provider) {
        this.providers.add(provider);
    }

    /**
     * Creates a TypeManager for the given type.
     * <p>
     * Iterates through registered providers to find one that can handle the type.
     * If no provider matches, returns a {@link SimpleTypeManager}.
     *
     * @param type the type to create a manager for
     * @return the appropriate TypeManager
     */
    public TypeManager createTypeManager(TypeMirror type) {
        for (TypeManagerProvider provider : providers) {
            TypeManager manager = provider.create(type, utils);
            if (manager != null) {
                return manager;
            }
        }
        return new SimpleTypeManager(TypeName.get(type));
    }


}