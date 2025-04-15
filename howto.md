# Vijie

### Tokens

A `Token` represents one or more characters as a single value.

- **Glyph**: The simplest token, representing a one-to-one mapping of a character to a token.
- **Composite Token**: A token that can contain other tokens.
- **Node Token**: A token that can be nested within other tokens.
- **Root**: A token that serves as the root of a tree structure and can contain other tokens.

_Base tokens are described below._

### Sequence

A `Sequence` is a list of tokens with a pointer that moves as tokens are parsed.  
When a `Composite` token is parsed, all its subsequent tokens are fused and removed from the parent sequence.

### Parsers

A `Parser` is a class that processes one or more tokens or parsers and produces a parsed token based on its implementation.

- **Factory**: The simplest parser. It processes a single token and its parameters, attempting to parse it within a given sequence.
- **Char**: A parser designed for glyphs.
- **Any**: A multi-parser that attempts to parse a sequence using a list of parsers. The first successful parser determines the result.
- **Optional**: A parser that attempts to parse a sequence using another parser. If parsing fails, it is ignored by other parsers.

### Exceptions

#### Internal Errors

- **TokenInstantiationError**: Raised when a token cannot be instantiated, typically due to missing or invalid parameters or constructors.
- **EOFError**: Raised when attempting to retrieve the current token from the sequence while the pointer is at the end of the sequence.

#### Parse Errors

Parse errors occur when a token cannot be parsed. These errors are initially caught in the sequence, encapsulated in a `ParserError`, and may propagate up the parsing hierarchy. If not caught, the root token can print a stack trace of all tokens and errors involved in the parsing process.

- **GenericParseError**: The abstract base class for parse errors.
- **ParserError**: A general encapsulating error raised when a token cannot be parsed. It includes references to the target parser and the error, enabling detailed stack tracing.
- **MultiParseError**: Raised when multiple parsers fail to parse a sequence. It contains a list of all errors encountered.
- **EOFParseError**: Raised when the parser unexpectedly reaches the end of the sequence.
- **OptionalNotFound**: Raised when an **Optional** parser fails to parse its target. This error is typically caught and ignored by the parent token.

_Other errors may be specific to individual tokens or parsers._

#### Interrupters

Interrupters are special errors that halt the parsing process without failing the current tokens. These tokens are merged even if incomplete, making interrupters useful for dynamic parsing and maintaining the sequence's state.

- **GenericInterrupter**: The abstract base class for interrupters.
- **PendingInterrupter**: Stops the parsing process but treats all remaining tokens as part of the current token.
- **EOFInterrupter**: Raised when the parser unexpectedly reaches the end of the sequence.

### BASE TOKENS