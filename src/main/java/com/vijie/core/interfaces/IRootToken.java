package com.vijie.core.interfaces;

import com.vijie.core.errors.GenericFailedTokenError;

import java.util.List;

/**
 * Represents the root token of the parser tree.
 *
 * @param <V> the type of the value held by this root token
 */
public interface IRootToken<V> extends ICompositeToken<V> {

    List<GenericFailedTokenError> getErrors();

}
