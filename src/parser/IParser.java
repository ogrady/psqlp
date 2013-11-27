package parser;

import exception.ParseException;

public interface IParser<I, T> {
	public T parse(I input) throws ParseException;
}
