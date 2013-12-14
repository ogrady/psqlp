package structure;

import io.IMessageReceiver;
import io.logger.LogMessageType;
import io.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import listener.IBackendListener;
import listener.IListenable;
import listener.INotifier;
import listener.ListenerSet;
import parser.Parser;
import parser.RelOptInfoParser;
import parser.objects.RelOptInfo;
import exception.ParseException;

public class Backend implements IMessageReceiver, IListenable<IBackendListener> {
	public final Logger _logger;
	private final Parser<RelOptInfo> _parser;
	private final ListenerSet<IBackendListener> _listeners;
	// this is very ugly and should be killed in a fire. We are using this list
	// as a global variable for all reloptinfos that were ever parsed. This is
	// not a problem as long as there is only one instance of Backend. But as it
	// isn't a singleton we have no guarantee for this (though it is only
	// instantiated once in this very project!).
	// May Dennis Ritchie have mercy on my soul.
	public static final Map<Integer, ArrayList<RelOptInfo>> _reloptinfos = new HashMap<Integer, ArrayList<RelOptInfo>>();

	public Backend(final Logger logger) {
		_logger = logger;
		_parser = new RelOptInfoParser(_logger);
		_listeners = new ListenerSet<IBackendListener>();
	}

	@Override
	public void receive(final List<String> buffer) {
		try {
			final RelOptInfo roi = _parser.parse(buffer);
			_logger.print(String.format("new RelOptInfo '%s'", roi.toString()),
					LogMessageType.BACKEND);
			_listeners.notify(new INotifier<IBackendListener>() {
				@Override
				public void notify(final IBackendListener listener) {
					listener.onNewRelOptInfo(roi);
				}
			});
			addRelOptInfo(roi);
		} catch (final ParseException e) {
			e.printStackTrace();
		}
	}

	private void addRelOptInfo(final RelOptInfo reloptinfo) {
		final int level = reloptinfo._ids.size();
		ArrayList<RelOptInfo> list = _reloptinfos.get(level);

		if (list == null) {
			final ArrayList<RelOptInfo> newList = new ArrayList<RelOptInfo>();
			_reloptinfos.put(level, newList);
			list = newList;
		}
		assert list != null;
		list.add(reloptinfo);
	}

	@Override
	public ListenerSet<IBackendListener> getListeners() {
		return _listeners;
	}
}
