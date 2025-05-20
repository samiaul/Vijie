package com.vijie.core;

import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.INodeToken;
import com.vijie.core.interfaces.IToken;


/**
 * Represents an abstract node token in the parsing process.
 *
 * @param <V> the type of the value returned by this token
 */
public abstract class NodeToken<V> extends CompositeToken<V> implements INodeToken<V> {

    /**
     * The parent node of this token.
     */
    protected ICompositeToken<?> parent;

    /**
     * Constructs a token with the specified parent node and sequence.
     *
     * @param parent the parent node
     * @param sequence the sequence associated with this edge
     */
    protected NodeToken(ICompositeToken<?> parent, Sequence sequence) {
        super(sequence);
        this.parent = parent;

        //System.out.println(this.getParseRepr());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ICompositeToken<?> getParent() {
        return this.parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDepth() {
        return this.parent.getDepth() + 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends IToken<?>> T findParent(Class<T> clazz) {
        if (clazz.isInstance(this.parent)) return (T) this.parent;
        if (this.parent instanceof INodeToken<?> node) return node.findParent(clazz);
        return null;
    }

    /**
     * Generates a string representation of the node for debugging purposes.
     * The representation includes the depth of the node, a split of the raw sequence
     * at the pointer index, and the string representation of the node itself.
     *
     * @return a formatted string representing the node's parse state
     */
    protected String getParseRepr() {

        int pointerIndex = this.getSequence().getPointerIndex();

        if (this.getRoot().getRaw().isEmpty()) return "%s".formatted(this.getClass().getSimpleName());

        return "%s\"%s_%s\" %s".formatted(
                "\t".repeat(this.getDepth()),
                this.getRoot().getRaw().substring(0, pointerIndex),
                this.getRoot().getRaw().substring(pointerIndex),
                this.toString()
        );
    }

}