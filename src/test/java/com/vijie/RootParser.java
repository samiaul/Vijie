package com.vijie;

import com.vijie.core.Root;
import com.vijie.core.Sequence;
import com.vijie.core.errors.BaseParseError;
import com.vijie.core.errors.GenericParseError;
import com.vijie.core.errors.OptionalNotFound;
import com.vijie.core.errors.ParserError;
import com.vijie.core.interfaces.IParser;
import com.vijie.core.interfaces.IToken;

public class RootParser<V, T extends IToken<V>> extends Root<V> {

    private final IParser<T> target;
    private final boolean logError;

    public RootParser(Sequence sequence, IParser<T> target) {
        super(sequence);
        this.target = target;
        this.logError = false;
    }

    public RootParser(IToken<?>[] content, IParser<T> target, boolean logError) {
        super(content);
        this.target = target;
        this.logError = logError;
    }

    public RootParser(String raw, IParser<T> target, boolean logError) {
        super(raw);
        this.target = target;
        this.logError = logError;
    }

    public RootParser(String raw, IParser<T> target) {
        this(raw, target, true);
    }

    @SuppressWarnings("unchecked")
    public T getToken() {
        return (T) this.sequence.getContent()[0];
    }

    @Override
    public V getValue() {
        return this.getToken().getValue();
    }

    @Override
    public void parse() throws BaseParseError {

        try {
            this.sequence.parse(this, this.target);
        } catch (OptionalNotFound error) {
            throw error;
        } catch (GenericParseError error) {
            throw (error instanceof ParserError)?((ParserError) error).getCause():error;
        }

    }
}
