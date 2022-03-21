package com.apps.admin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class QSTicket {
	
	private static final String CERTIFICATES_PATH = "resources"
						+ System.getProperty("file.separator") + "certificates"
						+ System.getProperty("file.separator");
	// templete 오타 아님
	
	private static final Logger logger = LoggerFactory.getLogger(QSTicket.class);
	
	static {
		disableSslverification();
	}
		
	public static void disableSslverification() {
		
		logger.info("[disableSslverification]");

		try {
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				@Override
				public X509Certificate[] getAcceptedIssuers() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public void checkClientTrusted(X509Certificate[] certs, String authType) {

				}

				@Override
				public void checkServerTrusted(X509Certificate[] certs, String authType) {

				}

			} };

			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());

			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HostnameVerifier allHostVaild = new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			HttpsURLConnection.setDefaultHostnameVerifier(allHostVaild);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
	}
	
	public static String getTicket(String appRoot, String userId, String userDirectory, String host, String pw) {
		
		logger.info("[getTicket] appRoot ::: " + appRoot);
		logger.info("[getTicket] userId ::: " + userId);
		logger.info("[getTicket] userDirectory ::: " + userDirectory);
		logger.info("[getTicket] host ::: " + host);
		logger.info("[getTicket] pw ::: " + pw);
		
		String xrfkey = "7rBHABt65vFflaZ7"; // Xrfkey to prevent cross-site issues
		try {
			
			// qlik 티켓 위치 :
			//   resource 내부 certificates 폴더 안에 인증서가 들어있었음
			//   국중은 인증서 보관 경로 결정한 뒤 넣어두고 아래 certFolder 로직 및 내용 변경하면 될 듯함
			//   ExcelUtil 에서 주석으로 막아놓은 내용인 디렉토리 내 파일 찾아오는 내용 확인하여 변경하면 될 듯
			
			/* 재민씨가 변경해야 할 내용 Start */
			
	    	//String certFolder = new String(appRoot + CERTIFICATES_PATH);
	    	String certFolder = new String(appRoot + System.getProperty("file.separator"));
	    	//logger.info("getTicket [certFolder] ::: " + certFolder);
	    	logger.info("[getTicket] certFolder ::: " + certFolder);
	    	
			//ClassPathResource resource = new ClassPathResource("/certificates/");
			//String certFolder = resource.getURL().toString();
			//int beginIndex = "file:".length();
			//int endIndex = certFolder.length();
			//certFolder = certFolder.substring(beginIndex, endIndex);
			//certFolder = certFolder.replaceAll("%20", " ");
			
			/* 재민씨가 변경해야 할 내용 End */
			
			/************** BEGIN Certificate Acquisition **************/
			String pass = pw;
			String proxyCert = certFolder + "client.jks";
			String rootCert = certFolder + "root.jks";
			String proxyCertPass = pass;
			String proxyCertPassDest = pass;
			String rootCertPass = pass;
			/************** END Certificate Acquisition **************/
			/*
			 * logger.info("ticket.java proxyCert: " + proxyCert);
			 * logger.info("ticket.java pass: (" + pass + ")");
			 * logger.info("ticket.java proxyCertPass: (" + proxyCertPass + ")");
			 */
			logger.info("[getTicket] pass ::: " + pass);
			logger.info("[getTicket] proxyCert ::: " + proxyCert);
			logger.info("[getTicket] rootCert ::: " + rootCert);
			logger.info("[getTicket] proxyCertPass ::: " + proxyCertPass);
			logger.info("[getTicket] proxyCertPassDest ::: " + proxyCertPassDest);
			logger.info("[getTicket] rootCertPass ::: " + rootCertPass);

			/**************
			 * BEGIN Certificate configuration for use in connection
			 **************/
			KeyStore ks = KeyStore.getInstance("JKS");
			ks.load(new FileInputStream(new File(proxyCert)), proxyCertPass.toCharArray());
			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(ks, proxyCertPassDest.toCharArray());
			SSLContext context = SSLContext.getInstance("SSL");
			KeyStore ksTrust = KeyStore.getInstance("JKS");
			ksTrust.load(new FileInputStream(rootCert), rootCertPass.toCharArray());
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(ksTrust);
			context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
//			context.init(null, tmf.getTrustManagers(), null);
			SSLSocketFactory sslSocketFactory = context.getSocketFactory();
			
			
			logger.info("[getTicket] sslSocketFactory ::: " + sslSocketFactory);
			/**************
			 * END Certificate configuration for use in connection
			 **************/

			/************** BEGIN HTTPS Connection **************/
			//String urlStr = "https://" + host + ":4243/qps/ticket?xrfkey=" + xrfkey;
			String urlStr = "https://" + host + ":4243/qps/ticket?xrfkey=" + xrfkey;
			//logger.info("Browsing to: " + urlStr);
			logger.info("[getTicket] ::: Browsing to : " + urlStr);
			URL url = new URL(urlStr);
			
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setSSLSocketFactory(sslSocketFactory);
			connection.setRequestProperty("x-qlik-xrfkey", xrfkey);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestMethod("POST");
			/************** BEGIN JSON Message to Qlik Sense Proxy API **************/

			String body = "{ 'UserId':'" + userId + "','UserDirectory':'" + userDirectory + "',";
			body += "'Attributes': [],";
			body += "}";
			//logger.info("Payload ticket.java :  " + body);
			logger.info("[getTicket] ::: Payload : " + body);
			logger.info("[getTicket] ::: connection : " + connection);
			/************** END JSON Message to Qlik Sense Proxy API **************/

			OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
			
			logger.info("[getTicket] ::: wr : " + wr);
			
			wr.write(body);
			wr.flush(); // Get the response from the QPS BufferedReader

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder builder = new StringBuilder();
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				builder.append(inputLine);
			}
			in.close();
			String data = builder.toString();

			Gson gson = new GsonBuilder().create();
			qsTicket dataObject = gson.fromJson(data, qsTicket.class);
			return dataObject.getTicket();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}

		return null;
	}


	public class qsTicket {
		String UserDirectory;
		String UserId;
		String[] Attributes;
		String Ticket;
		String TargetUri;
		String SessionId;

		public String getTicket() {
			return Ticket;
		}

		public void setTicket(String ticket) {
			Ticket = ticket;
		}

		public String getSession() {
			return SessionId;
		}

		public void setSession(String session) {
			SessionId = session;
		}
	}

}