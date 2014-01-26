package com.googlecode.tapestry5cayenne.annotations;

/**
 * Perfectly analogous to the tapestry-hibernate "CommitAfter" annotation.
 * Apply this to a method where you want the current object context to be committed.
 * On failure, the context will roll back and the exception wrapped and rethrown in a runtime exception.
 * 
 * Generally, you should handle committing yourself to ensure that you handle errors in a meaningful way.
 * However, this annotation is useful for rapid prototyping.
 */
public @interface CommitAfter {

}
