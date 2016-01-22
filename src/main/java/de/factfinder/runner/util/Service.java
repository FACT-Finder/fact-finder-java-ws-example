package de.factfinder.runner.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.common.gzip.GZIPInInterceptor;

/**
 * Initialize a service.
 *
 * @param <T> the service port type
 */
public class Service<T> {

	T		service;
	Client	client;

	/**
	 * Creates a new Service.
	 *
	 * @param clazz the *PortType class which should get created.
	 * @param endPoint the endpoint url from the service
	 */
	public Service(final Class clazz,
			final String endPoint) {
		final ClientProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setServiceClass(clazz);
		factory.setAddress(endPoint);
		service = (T) factory.create();
		this.client = ClientProxy.getClient(service);
	}

	public Object getResponseContext(final String key) {
		return client.getResponseContext().get(key);
	}

	/**
	 * @return the current *PortType service.
	 */
	public T get() {
		return service;
	}

	/**
	 * @return the client from the service
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * Enable gzip, that gzip can be transmitted between fact-finder and this application.
	 */
	public void enableGZIP() {
		if (service instanceof BindingProvider) {
			Map<String, Object> requestHeaders = new HashMap<>();
			requestHeaders.put("Accept-Encoding", new ArrayList<>(Arrays.asList("gzip")));
			((BindingProvider) service).getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, requestHeaders);

			// encode response from server
			client.getInInterceptors().add(new GZIPInInterceptor());
		}
	}

	/**
	 * @param clazz the *PortType class which should get created.
	 * @param endPoint the endpoint url from the service
	 * @return the *PortType service.
	 */
	public static <T> T get(final Class clazz, final String endPoint) {
		final Service<T> s = new Service<>(clazz, endPoint);
		return s.get();
	}
}
