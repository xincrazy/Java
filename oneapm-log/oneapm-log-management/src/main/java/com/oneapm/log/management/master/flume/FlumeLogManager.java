package com.oneapm.log.management.master.flume;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import com.oneapm.log.management.master.LogManager;
import com.oneapm.log.management.master.flume.handler.DispatchHandler;
import com.oneapm.log.management.util.LogUtil;

public class FlumeLogManager extends LogManager {

	Server server;
	public static String JETTY_QUEUED_THREAD_POOL_DEFAULT_NAME = "queuedTreadPool";
	public static int JETTY_QUEUED_THREAD_POOL_DEFAULT_MIN_THREAD = 10;
	public static int JETTY_QUEUED_THREAD_POOL_DEFAULT_MAX_THREAD = 200;
	public static int JETTY_SELECT_CHANNEL_CONNECT_DEFAULT_ACCEPTORS = 4;
	public static int JETTY_SELECT_CHANNEL_CONNECT_DEFAULT_MAX_BUFFERS = 2048;
	public static int JETTY_SELECT_CHANNEL_CONNECT_DEFAULT_MAX_IDLE_TIME = 10000;

	public FlumeLogManager() {
		server = new Server();
	}

	public void start() {

		// Add ThreadPool
		QueuedThreadPool queuedThreadPool = new QueuedThreadPool();
		queuedThreadPool.setName(JETTY_QUEUED_THREAD_POOL_DEFAULT_NAME);
		queuedThreadPool
				.setMinThreads(JETTY_QUEUED_THREAD_POOL_DEFAULT_MIN_THREAD);
		queuedThreadPool
				.setMaxThreads(JETTY_QUEUED_THREAD_POOL_DEFAULT_MAX_THREAD);

		server.setThreadPool(queuedThreadPool);

		// Add Connector
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(LOG_MANAGER_DEFAULT_PORT);
		connector.setAcceptors(JETTY_SELECT_CHANNEL_CONNECT_DEFAULT_ACCEPTORS);
		connector
				.setMaxBuffers(JETTY_SELECT_CHANNEL_CONNECT_DEFAULT_MAX_BUFFERS);
		connector
				.setMaxIdleTime(JETTY_SELECT_CHANNEL_CONNECT_DEFAULT_MAX_IDLE_TIME);

		server.addConnector(connector);

		ContextHandlerCollection context = new ContextHandlerCollection();
		HandlerCollection collection = new HandlerCollection();

		addHandles(context, collection);

		// Start server
		try {
			server.start();
			while (server.isStarted()) {
				LogUtil.getLogger().info("log master is starting...");
				break;
			}
			LogUtil.getLogger().info("log master stared...");
			server.join();
		} catch (Exception e) {
			LogUtil.getLogger().error("Failed to start log master!");
		}

	}

	private void addHandles(ContextHandlerCollection context,
			HandlerCollection collection) {
		// Add Handler

		ContextHandler contextHandler = context.addContext("/", "/");
		contextHandler.setHandler(new DispatchHandler());

		Handler defaults = new DefaultHandler();

		collection.setHandlers(new Handler[] { context, defaults });

		server.setHandler(collection);
	}

	public void stop() {
		// TODO Auto-generated method stub

	}

	public void restart() {
		// TODO Auto-generated method stub

	}

	public void deployAgent(PropertiesConfiguration config) {
		// TODO Auto-generated method stub

	}

	public void startAgent(PropertiesConfiguration config) {
		// TODO Auto-generated method stub

	}

	public String healthReporting() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkAgentAlive() {
		// TODO Auto-generated method stub
		return false;
	}

	public static void main(String args[]) {
		if (args == null || args.length == 0) {
			return;
		}

		String method = args[0];

		FlumeLogManager manager = new FlumeLogManager();

		if (method.equals("startServer")) {
			manager.start();
		}
	}

}
