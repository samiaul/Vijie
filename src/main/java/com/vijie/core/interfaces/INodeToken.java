package com.vijie.core.interfaces;

/**
 * Represents a node token in the parser tree.
 *
 * @param <V> the type of the value returned by this token
 */
public interface INodeToken<V> extends IToken<V> {

    /**
     * Get the parent of this node.
     *
     * @return The parent node.
     */
    ICompositeToken<?> getParent();

    /**
     * Get the root node of this node.
     *
     * @return The root node.
     */
    default IRootToken<?> getRoot() {
        if (this.getParent() instanceof IRootToken<?>) return (IRootToken<?>) this.getParent();
        return ((INodeToken<?>) this.getParent()).getRoot();
    }

    /**
     * Finds the nearest parent of the specified type in the token hierarchy.
     *
     * @param <T>   the type of the parent token to find
     * @param clazz the class object representing the type of the parent token
     * @return the nearest parent of the specified type, or {@code null} if no such parent exists
     */
    <T extends IToken<?>> T findParent(Class<T> clazz);

}