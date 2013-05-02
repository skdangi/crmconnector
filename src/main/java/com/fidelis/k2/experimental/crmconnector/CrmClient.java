package com.fidelis.k2.experimental.crmconnector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.params.AuthPNames;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public class CrmClient {

	public static void main(String args[]) {
		new CrmClient().connect();
	}
	
	public void connect() {
		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			httpclient.getAuthSchemes().register("ntlm",
					new NTLMSchemeFactory());
			NTCredentials creds = new NTCredentials("xxx", "xxx",
					"", "");
			List<String> authpref = new ArrayList<String>();
			authpref.add(AuthPolicy.NTLM);
			httpclient.getParams().setParameter(AuthPNames.TARGET_AUTH_PREF,
					authpref);

			httpclient.getCredentialsProvider().setCredentials(AuthScope.ANY,
					creds);

			HttpHost target = new HttpHost("devcrm", 80, "http");

			// Make sure the same context is used to execute logically related
			// requests
			HttpContext localContext = new BasicHttpContext();

			// Execute a cheap method first. This will trigger NTLM
			// authentication
			HttpGet httpget = new HttpGet(
					"/K2CRM/XRMServices/2011/OrganizationData.svc/AccountSet(guid'f7bf86ff-f580-e211-8074-005056970153')");

			HttpResponse response = httpclient.execute(target, httpget,
					localContext);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(response.getEntity().getContent())));
			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}
			httpclient.getConnectionManager().shutdown();

		} catch (ClientProtocolException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}