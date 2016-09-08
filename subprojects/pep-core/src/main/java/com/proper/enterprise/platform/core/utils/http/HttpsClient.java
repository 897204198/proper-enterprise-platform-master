package com.proper.enterprise.platform.core.utils.http;

import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Collection;

public class HttpsClient extends ClientUtil {

    private OkHttpClient client = new OkHttpClient();

    public static HttpsClient withCertificates(InputStream is, String keyStoreType, String password) throws Exception {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(is);
        if (certificates.isEmpty()) {
            throw new IllegalArgumentException("expected non-empty set of trusted certificates");
        }

        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, password.toCharArray()); // By convention, 'null' creates an empty key store.
        int index = 0;
        for (Certificate certificate : certificates) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }

        X509TrustManager trustManager = trustManagerForCertificates(keyStore, password);
        SSLContext sslContext = SSLContext.getInstance("TLSv1");
        sslContext.init(null, new TrustManager[] {trustManager}, null);
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        HttpsClient hc = new HttpsClient();
        hc.client = new OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustManager)
            .build();
        return hc;
    }

    private static X509TrustManager trustManagerForCertificates(KeyStore keyStore, String password) throws Exception {
        // Use it to build an X509 trust manager.
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
            KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password.toCharArray());
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                + Arrays.toString(trustManagers));
        }
        return (X509TrustManager) trustManagers[0];
    }

    public static HttpsClient withKeyStore(InputStream is, String keyStoreType, String password) throws Exception {
        KeyStore keyStore  = KeyStore.getInstance(keyStoreType);
        keyStore.load(is, password.toCharArray());

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
            KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password.toCharArray());

        SSLContext sslContext = SSLContext.getInstance("TLSv1");
        sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        X509TrustManager trustManager = Platform.get().trustManager(sslSocketFactory);

        HttpsClient hc = new HttpsClient();
        hc.client = new OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustManager)
            .build();
        return hc;
    }

    public ResponseEntity<String> get(String url) throws IOException {
        return perform(client, url, GET, null, null);
    }

    public ResponseEntity<String> post(String url, MediaType type, String data) throws IOException {
        return perform(client, url, POST, type, data);
    }

    public ResponseEntity<String> put(String url, MediaType type, String data) throws IOException {
        return perform(client, url, PUT, type, data);
    }

    public ResponseEntity<String> delete(String url, MediaType type, String data) throws IOException {
        return perform(client, url, DELETE, type, data);
    }

    public ResponseEntity<String> delete(String url) throws IOException {
        return perform(client, url, DELETE, null, null);
    }

}
