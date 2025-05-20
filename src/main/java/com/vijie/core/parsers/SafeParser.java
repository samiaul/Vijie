package com.vijie.core.parsers;

import com.vijie.core.*;
import com.vijie.core.errors.*;
import com.vijie.core.interfaces.*;
import com.vijie.core.safe.FailedToken;
import com.vijie.core.safe.MissingToken;
import com.vijie.core.safe.UnexpectedToken;
import com.vijie.core.safe.UnknownToken;

public class SafeParser<T extends ICompositeToken<?>> implements IParser<T> {

    protected final IParser<T> target;
    protected final IParser<?> syncTarget;
    protected final Class<? extends MissingTokenInterrupter> missingInterrupter;


    /**
     * Create a safe parser for UnexpectedTokens
     */
    public SafeParser(IParser<T> target,
                      IParser<?> syncTarget,
                      Class<? extends MissingTokenInterrupter> missingInterrupter) {
        this.target = target;
        this.syncTarget = syncTarget;
        this.missingInterrupter = missingInterrupter;
    }

    /**
     * Create a safe parser for UnexpectedTokens
     */
    public SafeParser(IParser<T> syncTarget) {
        this(syncTarget, null, null);
    }

    /**
     * Create a safe parser for FailedTokens
     */
    public SafeParser(IParser<T> target,
                      IParser<?> syncTarget) {
        this(target, syncTarget, null);
    }

    /**
     * Create a safe parser for MissingTokens
     */
    public SafeParser(IParser<T> target,
                      Class<MissingTokenInterrupter> missingInterrupter) {
        this(target, null, missingInterrupter);
    }

    @Override
    public Class<? extends T> getType() {
        return this.target.getType();
    }

    @Override
    public T parse(ICompositeToken<?> parent, Sequence sequence) throws BaseParseError {

        IGenericFailedToken failedToken;

        try {
            return this.target.parse(parent, sequence.copy());
        } catch (Interruption interruption) {
            //System.err.println(this + " " + interruption);
            failedToken = this.onInterruption(parent, sequence, interruption);
        } catch (GenericInterrupter interrupter) {
            //System.err.println(this + " " + interrupter);
            failedToken = this.onInterrupter(parent, sequence, interrupter);
        }/* catch (BaseParseError error) {
            System.err.println(this + " " + error);
            throw error;
        }*/

        throw new FailedTokenInterruption(failedToken);

    }

    protected IGenericFailedToken onInterruption(ICompositeToken<?> parent, Sequence sequence, Interruption interruption) throws BaseParseError {

        ICompositeFailedToken failedToken;

        GenericInterrupter interrupter = interruption.getCause();

        if (interrupter instanceof FatalInterrupter) throw interruption;

        if (this.syncTarget != null) failedToken = this.getFailed(parent, sequence, interruption.getCause());
        else if (this.missingInterrupter != null) failedToken = this.getMissing(parent, sequence);
        else failedToken = this.getUnexpected(parent, sequence, interruption.getCause());

        return this.parseToken(parent, sequence, failedToken);
    }

    protected IGenericFailedToken onInterrupter(ICompositeToken<?> parent, Sequence sequence, GenericInterrupter interrupter) throws BaseParseError {

        ICompositeFailedToken failedToken;

        if (syncTarget != null) failedToken = this.getUnknown(parent, sequence, interrupter);
        else failedToken = getUnexpected(parent, sequence, interrupter);

        return this.parseToken(parent, sequence, failedToken);
    }

    protected IGenericFailedToken parseToken(ICompositeToken<?> parent, Sequence sequence, ICompositeFailedToken failedToken) throws BaseParseError {

        try {
            failedToken.parse();
        } catch (EOFInterrupter _) {
            if (this.missingInterrupter == null) throw failedToken.getInterrupter();
            return this.parseToken(parent, sequence, this.getMissing(parent, sequence));

        } catch (GenericInterrupter error) {
            throw new Interruption(failedToken.getInterrupter(), failedToken);
        }

        return failedToken;
    }

    protected IFailedToken<T> getFailed(ICompositeToken<?> parent, Sequence sequence, GenericInterrupter interrupter) {
        return new FailedToken<>(parent, sequence.copy(), this.target, interrupter, this.syncTarget);
    }

    protected IUnknownToken<T> getUnknown(ICompositeToken<?> parent, Sequence sequence, GenericInterrupter interrupter) {
        return new UnknownToken<>(parent, sequence.copy(), this.target, interrupter, this.syncTarget);
    }

    protected UnexpectedToken getUnexpected(ICompositeToken<?> parent, Sequence sequence, GenericInterrupter interrupter) {
        return new UnexpectedToken(parent, sequence.copy(), interrupter, this.target);
    }

    protected MissingToken<T> getMissing(ICompositeToken<?> parent, Sequence sequence) {
        return new MissingToken<>(parent, sequence.copy(), this.target, this.instantiateInterrupter(sequence));
    }

    protected MissingTokenInterrupter instantiateInterrupter(Sequence sequence) {
        try {
            return this.missingInterrupter.getDeclaredConstructor(Sequence.class).newInstance(sequence);
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate MissingTokenInterrupter", e);
        }
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        if (this.syncTarget != null) builder.append(this.syncTarget);
        if (this.missingInterrupter != null) builder.append(builder.isEmpty()?"":"|").append(this.missingInterrupter.getSimpleName());
        if (!builder.isEmpty()) builder.insert(0, "<").append(">");

        return "%s$%s".formatted(target, builder);
    }
}
