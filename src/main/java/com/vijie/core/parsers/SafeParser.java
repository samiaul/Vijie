package com.vijie.core.parsers;

import com.vijie.core.*;
import com.vijie.core.errors.*;
import com.vijie.core.interfaces.*;

public class SafeParser<T extends ICompositeToken<?>> implements IParser<T> {

    protected final IParser<T> target;
    protected final IParser<?> syncTarget;
    protected final Class<? extends MissingTokenInterrupter> missingInterrupter;

    public SafeParser(IParser<T> target,
                      IParser<?> syncTarget,
                      Class<? extends MissingTokenInterrupter> missingInterrupter) {
        this.target = target;
        this.syncTarget = syncTarget;
        this.missingInterrupter = missingInterrupter;
    }

    public SafeParser(IParser<T> syncTarget) {
        this(syncTarget, null, null);
    }

    public SafeParser(IParser<T> target,
                      IParser<?> syncTarget) {
        this(target, syncTarget, null);
    }

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

        IFailedToken failedToken;

        try {
            return this.target.parse(parent, sequence.copy());
        } catch (Interruption interruption) {
            //System.out.println(this + " " + interruption);
            failedToken = this.getFailedToken(parent, sequence, interruption);
        } catch (GenericInterrupter interrupter) {
            //System.out.println(this + " " + interrupter);
            failedToken = this.getUnknownToken(parent, sequence, interrupter);
        }

        throw new FailedTokenInterruption(failedToken);

    }

    protected IFailedToken parseFailedToken(ICompositeToken<?> parent, Sequence sequence, ICompositeFailedToken failedToken) throws BaseParseError {

        try {
            failedToken.parse();
        } catch (EOFInterrupter _) {
            if (this.missingInterrupter == null) throw failedToken.getInterrupter();
            return this.getMissingToken(parent, sequence);
        }

        return failedToken;
    }

    protected IFailedToken getFailedToken(ICompositeToken<?> parent, Sequence sequence, Interruption interruption) throws BaseParseError {

        ICompositeFailedToken failedToken;

        GenericInterrupter interrupter = interruption.getCause();

        if (interrupter instanceof FatalInterrupter) throw interruption;

        if (this.syncTarget == null && this.missingInterrupter == null) failedToken = getUnexpectedToken(parent, sequence, interrupter);
        else if (this.syncTarget == null) return this.getMissingToken(parent, sequence);
        else failedToken = new FailedToken<>(parent, sequence.copy(), interruption.getToken(), interrupter, this.syncTarget);

        return this.parseFailedToken(parent, sequence, failedToken);
    }

    protected IFailedToken getUnknownToken(ICompositeToken<?> parent, Sequence sequence, GenericInterrupter interrupter) throws BaseParseError {

        ICompositeFailedToken failedToken;

        if (syncTarget == null) failedToken = getUnexpectedToken(parent, sequence, interrupter);
        else failedToken = new UnknownToken<>(parent, sequence.copy(), this.target, interrupter, this.syncTarget);

        return this.parseFailedToken(parent, sequence, failedToken);
    }

    protected IFailedToken getMissingToken(ICompositeToken<?> parent, Sequence sequence) {
        return new MissingToken<>(parent, sequence.getCurrentIndex(), this.target, this.instantiateInterrupter(sequence));
    }

    protected UnexpectedToken getUnexpectedToken(ICompositeToken<?> parent, Sequence sequence, GenericInterrupter interrupter) {
        return new UnexpectedToken(parent, sequence, interrupter, this.target);
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
