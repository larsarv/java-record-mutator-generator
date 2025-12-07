package io.github.larsarv.jrmg.annotation.processor.type.manager;

import javax.lang.model.type.TypeMirror;

/**
 * Interface for providing TypeManager instances.
 * <p>
 * Implementations of this interface are registered with the {@link TypeManagerFactory}
 * and are responsible for creating {@link TypeManager}s for specific types.
 */
public interface TypeManagerProvider {
    /**
     * Creates a TypeManager for the given type if this provider supports it.
     *
     * @param type  the type to create a manager for
     * @param utils utility class for type analysis
     * @return a TypeManager instance if supported, or null otherwise
     */
    TypeManager create(TypeMirror type, TypeProviderUtils utils);
}