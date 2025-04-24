package com.vijie.core;

import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.INodeToken;
import com.vijie.core.parsers.Char;

import javax.lang.model.type.NullType;

import static org.apache.commons.text.StringEscapeUtils.escapeJava;

public final class EOF extends Token<NullType> implements INodeToken<NullType> {

    /**
     * The index of the Eof in the text.
     */
    private int index;

    /**
     * The parent composite node of the Eof.
     */
    private ICompositeToken<?> parent;


    public EOF(int index) {
        this.index = index;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ICompositeToken<?> getParent() {
        return this.parent;
    }

    /**
     * Set the parent node of this token.
     *
     * @param parent The parent node to set.
     */
    public void setParent(ICompositeToken<?> parent) {
        this.parent = parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRaw() {
        return "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIndex() {
        return this.index;
    }

    /**
     * Set the index of this token.
     *
     * @param index The index to set.
     */
    void setIndex(int index) {
        this.index = index;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLength() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDepth() {
        if (this.parent == null) return 0;
        return this.parent.getDepth() + 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NullType getValue() {
        return null;
    }

    @Override
    public String toString() {
        return "EOF@%d".formatted(index);
    }

    public String getSequenceRepr() {
        return this.toString();
    }
}

