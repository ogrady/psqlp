package io;

/**
 * InputReceivers receive strings from {@link ContinuousInputStream}s that
 * listen to files (pretty much like redirecting the input from UNIX-like tail
 * -f)
 * 
 * @author Daniel
 * 
 */
public interface IInputReceiver {
	/**
	 * Receives a single line of input from the {@link ContinuousInputStream}.<br>
	 * The line itself is guaranteed to be not null. But is otherwise
	 * unprocessed (like empty strings or dangling whitespaces etc)
	 * 
	 * @param line
	 *            the unprocessed, not-null line of input
	 */
	public void receive(String line);
}
