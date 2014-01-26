package org.panther.tap5cay3;

public class GenericBox<E> {
	// Private variable
	private E content;

	// Constructor
	public GenericBox(E content) {
		this.content = content;
	}

	public E getContent() {
		return content;
	}

	public void setContent(E content) {
		this.content = content;
	}

	public String toString() {
		return content + " (" + content.getClass().getName() + ")";
	}
}
