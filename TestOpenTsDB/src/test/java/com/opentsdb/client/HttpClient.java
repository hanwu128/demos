package com.opentsdb.client;

import java.io.IOException;

import com.opentsdb.client.builder.MetricBuilder;
import com.opentsdb.client.request.QueryBuilder;
import com.opentsdb.client.response.Response;
import com.opentsdb.client.response.SimpleHttpResponse;

public interface HttpClient extends Client {

	public Response pushMetrics(MetricBuilder builder,
                                ExpectResponse exceptResponse) throws IOException;

	public SimpleHttpResponse pushQueries(QueryBuilder builder,
                                          ExpectResponse exceptResponse) throws IOException;
}