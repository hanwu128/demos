package com.opentsdb.client;

import java.io.IOException;

import com.opentsdb.client.builder.MetricBuilder;
import com.opentsdb.client.response.SimpleHttpResponse;
import com.opentsdb.client.request.QueryBuilder;
import com.opentsdb.client.response.Response;

public interface Client {

	String PUT_POST_API = "/api/put";

    String QUERY_POST_API = "/api/query";

	/**
	 * Sends metrics from the builder to the KairosDB server.
	 *
	 * @param builder
	 *            metrics builder
	 * @return response from the server
	 * @throws IOException
	 *             problem occurred sending to the server
	 */
	Response pushMetrics(MetricBuilder builder) throws IOException;

	SimpleHttpResponse pushQueries(QueryBuilder builder) throws IOException;
}