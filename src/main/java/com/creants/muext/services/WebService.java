package com.creants.muext.services;

import java.util.Arrays;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.creants.creants_2x.socket.util.ConverterUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * @author LamHM
 *
 */
public class WebService {
	private static final String KEY = "2|WqRVclir6nj4pk3PPxDCzqPTXl3J";
	private static final String GRAPH_API = "http://api.creants.net:9393/internal/";
	private static WebService instance;


	public static WebService getInstance() {
		if (instance == null) {
			instance = new WebService();
		}
		return instance;
	}


	private WebService() {
	}


	public String verify(String token) {
		WebResource webResource = Client.create().resource(GRAPH_API + "verify");

		MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
		formData.add("key", KEY);
		formData.add("token", token);

		ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, formData);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		return response.getEntity(String.class);
	}


	public String getUser(String uid, String key) {
		WebResource webResource = Client.create().resource(GRAPH_API + "user");
		MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
		formData.add("uid", uid);

		ClientResponse response = webResource.accept(MediaType.TEXT_PLAIN).header("key", KEY).post(ClientResponse.class,
				formData);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		return response.getEntity(String.class);
	}


	public static void main(String[] args) {
		// String user = WebService.getInstance().verify(
		// "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6IjI4NiIsImV4cCI6MTQ5Mzk3OTE1NSwiaXNzIjoiYXV0aDAiLCJ0dGwiOjg2NDAwMDAwMH0.o_IRbuqVe1MkJQ0GjB_Xmoch1x12_vc1i2fltlTTNC4");
		// System.out.println(user.toString());

		byte[] convertLong2Bytes = ConverterUtil.convertString2Bytes("Xin chào bà con");
		System.out.println(Arrays.toString(convertLong2Bytes));
		System.out.println(ConverterUtil.convertBytes2String(convertLong2Bytes));

		System.out.println(ConverterUtil.convertBytes2String(new byte[]{88, 105, 110, 32, 99, 104, (byte) 224, 111, 32, 98, (byte) 224, 32, 99, 111, 110}));
	}
}
