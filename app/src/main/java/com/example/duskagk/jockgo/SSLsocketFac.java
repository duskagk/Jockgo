//package com.example.duskagk.jockgo;
//
//import org.apache.http.conn.ConnectTimeoutException;
//import org.apache.http.conn.scheme.LayeredSocketFactory;
//import org.apache.http.params.HttpConnectionParams;
//import org.apache.http.params.HttpParams;
//
//import java.io.IOException;
//import java.net.InetAddress;
//import java.net.InetSocketAddress;
//import java.net.Socket;
//import java.net.SocketImplFactory;
//import java.net.UnknownHostException;
//
//import javax.net.SocketFactory;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLSocket;
//import javax.net.ssl.TrustManager;
//
//public class SSLsocketFac implements SocketFactory,LayeredSocketFactory {
//    private SSLContext sslContext=null;
//
//    private static SSLContext createEasySSLcontext()throws IOException{
//        try{
//            SSLContext context=SSLContext.getInstance("TLS");
//            context.init(null,new TrustManager[]{new EasyX509TrustManager(null)},null);
//            return context;
//        }catch (Exception e){
//            throw new IOException(e.getMessage());
//        }
//    }
//    private SSLContext getSslContext() throws IOException{
//        if(this.sslContext==null){
//            this.sslContext=createEasySSLcontext();
//        }
//        return this.sslContext;
//    }
//    public Socket connectsocket(Socket sock, String host, int port, InetAddress localAddress, int localPort,
//                                HttpParams params)throws IOException,UnknownHostException, ConnectTimeoutException {
//        int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
//        int soTimeout = HttpConnectionParams.getSoTimeout(params);
//        InetSocketAddress remoteAddress=new InetSocketAddress(host,port);
//        SSLSocket sslSock=(SSLSocket)((sock!=null)?sock:createSocket());
//        if ((localAddress != null) || (localPort > 0)) {
//            // we need to bind explicitly
//            if (localPort < 0) {
//                localPort = 0; // indicates "any"
//            }
//            InetSocketAddress isa = new InetSocketAddress(localAddress, localPort);
//            sslSock.bind(isa);
//        }
//        sslSock.connect(remoteAddress, connTimeout);
//        sslSock.setSoTimeout(soTimeout);
//        return sslSock;
//    }
//
//
//}
