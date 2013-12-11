package structure;

import io.IMessageReceiver;
import io.logger.LogMessageType;
import io.logger.Logger;

import java.util.List;

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

	public Backend(final Logger logger) {
		_logger = logger;
		_parser = new RelOptInfoParser(_logger);
		_listeners = new ListenerSet<IBackendListener>();
	}

	public Tree<?> getTree() {
		TreeNode<Integer> node = null;
		TreeNode<Integer> left = null;
		TreeNode<Integer> right = null;
		for (int i = 0; i < 20; i++) {
			node = new TreeNode<Integer>(i + 1);
			right = new TreeNode<Integer>(-i + 1);
			if (left != null) {
				node.setLeftChild(left);
				left.setParent(node);
			}
			right.setParent(node);

			node.setRightChild(right);
			left = node;
		}
		return new Tree<Integer>(node);
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
		} catch (final ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ListenerSet<IBackendListener> getListeners() {
		return _listeners;
	}
}
