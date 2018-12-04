/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dorif.server;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;

/**
 * Class responsable for encapsulating the DORIF server.
 * A builder method is offered for creating an instance of
 * a DORIF server customized for the user needs.
 * @author Arthur
 */
import org.dorif.exceptions.InvalidServerTypeException;
import org.dorif.exceptions.KeystoreNotSetException;
import org.dorif.handler.DORIFHandler;
import org.dorif.handler.DORIFService;
public class DORIFServer implements Runnable{
    public static final String SIMPLE_BINARY = "UNSECURE_COMPACT";
    public static final String SECURE_BINARY = "SECURE_COMPACT";
    private final DORIFHandler handler;
    private final DORIFService.Processor processor;
    private final int port;
    private final TServerTransport transport;
    private final TThreadPoolServer server;
   
    private DORIFServer(DORIFServerBuilder b) {
        this.processor = b.processor;
        this.port = b.port;
        this.transport = b.transport;
        this.handler = b.handler;
        this.server = b.server;
    }

    @Override
    public void run() {
        try {
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public static class DORIFServerBuilder{
        
        private final DORIFHandler handler;
        private final DORIFService.Processor processor;
        private final int port;
        private TServerTransport transport;
        private final String server_type;
        private TThreadPoolServer server;
        private TSSLTransportParameters params;
        private String keystore;
        private String password;
        
        public DORIFServerBuilder(int port, String server_type, DORIFHandler handler){
            this.handler = handler;
            this.processor = new DORIFService.Processor(handler);
            this.server_type = server_type;
            this.port = port;
        }
        
        public DORIFServerBuilder keystore(String keystore){
            this.keystore = keystore;
            return this;
        }
        
        public DORIFServerBuilder password(String password){
            this.password = password;
            return this;
        }
        /*
            *Creates a new instance of DORIFServer which has been built whithin the other methods
            *@author Arthur Cruz
            *@version 0.1
            *@param
            *@return DORIFServer
            *@exception TTransportException, KeystoreNotSetException, InvalidServerTypeException
        */
        public DORIFServer build() throws TTransportException, KeystoreNotSetException, InvalidServerTypeException{
            switch(this.server_type){
                case "UNSECURE_COMPACT":
                    this.transport = new TServerSocket(this.port);
                    this.server = new TThreadPoolServer(new TThreadPoolServer
                                                            .Args(this.transport)
                                                            .processor(this.processor)
                                                            .protocolFactory(new TCompactProtocol.Factory()));
                    break;
                case "SECURE_COMPACT":
                    if(this.keystore == null || this.password == null || "".equals(this.keystore)){
                        throw new KeystoreNotSetException("A keystore with a password must be specified");
                    }else{
                        this.params = new TSSLTransportParameters();
                        params.setKeyStore(this.keystore, this.password);
                        this.transport = TSSLTransportFactory.getServerSocket(this.port, 0, null, params);
                        this.server = new TThreadPoolServer(new TThreadPoolServer
                                                                .Args(this.transport)
                                                                .processor(this.processor)
                                                                .protocolFactory(new TCompactProtocol.Factory()));
                    }
                    break;
                default:
                    throw new InvalidServerTypeException("Invalid server type informed as parameter");
            }
            return new DORIFServer(this);
        }
    }
}
