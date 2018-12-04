/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.servidortxt;

import java.security.NoSuchAlgorithmException;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;
import org.dorif.exceptions.InvalidServerTypeException;
import org.dorif.exceptions.KeystoreNotSetException;
import org.dorif.handler.DORIFHandler;
import org.dorif.server.DORIFServer;
import org.dorif.server.DORIFServer.DORIFServerBuilder;
import org.dorif.vectorial.VectorialCollectionHandler;
import org.dorif.vectorial.VectorialFactory;
import org.dorif.vectorial.VectorialRankingFunction;

/**
 *
 * @author Arthur
 */
public class Main {
    public static void main(String[] args){
        String name;
        String ip;
        String root_node_ip;
        int port;
        int root_node_port;
        int num_bits;
        if(args.length == 0){
            name = "Server TXT";
            ip = "localhost";
            root_node_ip = "localhost";
            port = 23334;
            root_node_port= 23333;
            num_bits = 10;
        }else{
            name = args[0];
            ip = args[1];
            port = Integer.parseInt(args[2]);
            root_node_ip = args[3];
            root_node_port = Integer.parseInt(args[4]);
            num_bits = Integer.parseInt(args[5]);
        }
        DORIFHandler txth = null;
        try {
            VectorialFactory vtf = new VectorialFactory();
            TXTCollector txtc = new TXTCollector();
            TXTPreProcessor txtp = new TXTPreProcessor();
            VectorialRankingFunction vrf = new VectorialRankingFunction();
            VectorialCollectionHandler vch = new VectorialCollectionHandler(txtc, txtp, vtf,vrf);
            vch.init();
            txth = new DORIFHandler(name,  ip, port, root_node_ip, root_node_port, num_bits, vch);
        } catch (NoSuchAlgorithmException ex) {
            System.out.print("Falha ao abrir Message Digest");
            System.exit(-1);
        } catch (TException ex) {
            System.out.print(ex.getMessage());
            ex.printStackTrace();
            System.exit(-1);
        }
        DORIFServerBuilder b = new DORIFServerBuilder(port, DORIFServer.SIMPLE_BINARY,txth);
        try {
            DORIFServer servidor = b.build();
            Thread server = new Thread(servidor);
            server.start();
            System.out.println("Servidor Iniciado na porta "+port);
        } catch (TTransportException ex) {
            System.out.println("Erro ao Criar Transporte");
        } catch (KeystoreNotSetException ex) {
            System.out.println(ex.getMessage());
        } catch (InvalidServerTypeException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
    }
}