package com.luke;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.conscrypt.OpenSSLProvider;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;


public class ConscryptJetty {

    // When true conscrypt will be used, bit dirty making it static
    public static boolean USE_CONSCRYPT = true;
    
    public static void main(String[] args) throws Exception {

        Server server = new Server(45000);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(HelloServlet.class, "/*");

        HttpConfiguration https = new HttpConfiguration();
        https.addCustomizer(new SecureRequestCustomizer());
        SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
        
        if(USE_CONSCRYPT) {
            OpenSSLProvider openSSLProvider = new OpenSSLProvider();
            java.security.Security.addProvider(openSSLProvider);
            sslContextFactory.setProvider("Conscrypt");
        }
        
        
        sslContextFactory.setKeyStorePath("src/test/resources/ssl/server-keystore.jks");
        sslContextFactory.setKeyStorePassword("serverkeypass");
        sslContextFactory.setKeyManagerPassword("serverkeypass");
        sslContextFactory.setTrustStorePath("src/test/resources/ssl/server-truststore.jks");
        sslContextFactory.setTrustStorePassword("servertrustpass");
        sslContextFactory.setNeedClientAuth(false);
        sslContextFactory.setWantClientAuth(false);
        sslContextFactory.setEndpointIdentificationAlgorithm(null);

        ServerConnector sslConnector = new ServerConnector(server,
            sslContextFactory,
            new HttpConnectionFactory(https));
        sslConnector.setPort(0);
        server.setConnectors(new Connector[] { sslConnector });

        server.start();

        try {
            int port = ((ServerConnector) server.getConnectors()[0]).getLocalPort();
            String serverRoot = "https://127.0.0.1:" + port;
            
            System.out.println(serverRoot);
            
            System.out.println("Make non stop requests to the server, run lsof and you should see lots of FIFO open files.");
            
            System.out.println("You could run:");
            System.out.println("src/test/resources/example.sh " + port);
            System.out.println("To run a heap of requests against the server.");
            
            Thread.sleep(10000000);

            
        } finally {
            server.stop();
        }
    }

    public static class HelloServlet extends HttpServlet
    {
        @Override
        protected void doGet( HttpServletRequest request,
            HttpServletResponse response ) throws ServletException, IOException {
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(TEXT);
        }
    }
    
    public static String TEXT = "This is the response\nOK cool, now run lsof do you see lits of FIFO open file handles?";
}
