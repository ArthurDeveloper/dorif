package org.dorif.handler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.dorif.chord.*;
import org.dorif.interfaces.ICollectionHandler;
import org.dorif.interfaces.IDocument;
import org.dorif.interfaces.IQuery;
import org.dorif.interfaces.ITerm;
import org.dorif.interfaces.IVocabulary;
import org.dorif.vectorial.IDFWeight;
/**
 *
 * @author Arthur
 */
public class DORIFHandler implements DORIFService.Iface, IVocabulary{

    private final MessageDigest md;
    private final BufferedWriter writer;
    private final NumberFormat nfmt;
    protected Node node;
    protected Node root;
    private final static boolean verboseTrace = false;
    private final static boolean traceEnable = true;
    private final static boolean stackTraceEnable = false;
    private static int lastFinger = 1;
    private final static ConcurrentHashMap<String, ITerm> vocabulary = new ConcurrentHashMap<String, ITerm>(1000);
    public static ICollectionHandler cHandler = null;
    
    public DORIFHandler(String name, String ip, int port, String root_node_ip, int root_node_port, int numBits, ICollectionHandler cHandler) throws NoSuchAlgorithmException, UnableToJoinChordException, UnableToSendSelfException, TException{
        DORIFHandler.cHandler = cHandler;
        DORIFHandler.cHandler.setGlobalVocabulary(this);
        this.writer = new BufferedWriter(new OutputStreamWriter(System.out));
        this.nfmt = NumberFormat.getNumberInstance();
        md = MessageDigest.getInstance("SHA-256");
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
        this.node = new Node();
        this.node.setFingerTable(new FingerTable());
        this.node.getFingerTable().setFingers(new ArrayList<>(numBits));
        this.node.getFingerTable().setSuccessorList(new LinkedList<>());
        this.node.setIp(ip);
        this.node.setPort(port);
        this.node.setName(name);
        this.node.setNumBits(numBits);
        this.node.setId(this.hashChave(name+ip+port));
        if(this.node.getIp().equals(root_node_ip) && this.node.getPort() == root_node_port){
            this.trace("Root node for Chord, ID:"+this.node.getId());
            this.join(this.node);
        }else{
            this.trace("Common Node ID: "+this.node.getId());
            TTransport transport = new TSocket(root_node_ip,root_node_port);
            transport.open();
            TProtocol protocol = new TCompactProtocol(transport);
            Chord.Client cli = new Chord.Client(protocol);
            this.root = cli.sendSelf();
            transport.close();
            this.join(this.root);
        }
        ScheduledFuture scheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {
                stabilize();
            } catch (TException ex) {
                System.out.println("Erro ao iniciar Thread de protocolo de estabilização");
                if(DORIFHandler.stackTraceEnable)
                    ex.printStackTrace();
            }
        }, 10, 10, TimeUnit.SECONDS);
        ScheduledFuture scheduledFuture2 = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {
                fixFingers();
            } catch(UnableToFixFingerException utffe){
                this.trace(utffe.getErrMsg());
                utffe.printStackTrace();
            }
        }, 10, 10, TimeUnit.SECONDS);
        ScheduledFuture scheduledFuture3 = scheduledExecutorService.scheduleWithFixedDelay(() -> {
                printServerStatus();
        }, 10, 10, TimeUnit.SECONDS);
        ScheduledFuture scheduledFuture4 = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try{
                long num_docs = this.getTotalNumDocs();
                DORIFHandler.cHandler.setGlobalNumDocs(num_docs);
                if(DORIFHandler.traceEnable)
                    this.trace("Collection document count updated :"+num_docs);
            }catch(TException te){
                if(DORIFHandler.traceEnable)
                    this.trace("Unable to update document count: "+te.getMessage());
            }    
        }, 20, 20, TimeUnit.SECONDS);
    }
    //CHORD RELATED METHOS////////////////////////////////////////////////////////////
    @Override
    public byte ping() throws TException {
        this.trace("GOT PINGED");
        return 1;
    }
    @Override
    public final void join(Node root) throws UnableToJoinChordException, TException {
        node.setPredecessor(null);
        //Criação da FingerTable apontando para si mesmo em todos os Fingers
        Finger aux = new Finger();
        aux.setId(node.getId());
        aux.setIp(node.getIp());
        aux.setPort(node.getPort());
        aux.setName(node.getName());
        for(int i = 0; i < node.getNumBits(); i++){
            node.getFingerTable().getFingers().add(aux);
        }
        this.node.getFingerTable().getSuccessorList().add(aux);
        //Se Common Node
        if( node.getId() != root.getId() ){
                try{ //TODO add retry
                    TTransport transport = new TSocket(root.getIp(),root.getPort());
                    transport.open();
                    TProtocol protocol = new TCompactProtocol(transport);
                    Chord.Client cli = new Chord.Client(protocol);
                    Node x = cli.findSuccessor(node.getId());
                    transport.close();
                    Finger n = node.getFingerTable().getFingers().get(0);
                    n.setId(x.getId());
                    n.setIp(x.getIp());
                    n.setPort(x.getPort());
                    n.setName(x.getName());
                    this.node.getFingerTable().getSuccessorList().set(0, n);
                    return;
                }catch(TException t){
                    if(DORIFHandler.stackTraceEnable)
                        t.printStackTrace();
                    trace("Failed to join chord ring");
                }
            throw new UnableToJoinChordException();
        }
    }
    @Override
    public Node findSuccessor(long id) throws UnableToFindSuccessorException, TException {
        Node l = findPredecessor(id);
        if(l.getFingerTable().getSuccessorList().get(0).getId() == node.getId()){
            return node;
        }else{
            int count = 0;
            long timeout = 0;
            while(true){
                try {
                    TTransport transport = new TSocket(l.getFingerTable().getSuccessorList().get(0).getIp(),
                            l.getFingerTable().getSuccessorList().get(0).getPort());
                    transport.open();
                    TProtocol protocol = new TCompactProtocol(transport);
                    Chord.Client cli = new Chord.Client(protocol);
                    l = cli.sendSelf();
                    transport.close();
                    break;
                } catch (TException e) {
                    if(++count < 5){
                        try{
                            Thread.sleep(timeout);
                            timeout = this.getTimeout(timeout);
                        }catch(InterruptedException ie){
                            ie.printStackTrace();
                        }
                    }else{
                        l.getFingerTable().getSuccessorList().remove(0);
                        if(DORIFHandler.verboseTrace)
                            this.trace("removing dead successor");
                        if(l.getFingerTable().getSuccessorList().isEmpty()){
                            if(DORIFHandler.verboseTrace)
                                this.trace("Unable to find successor for id:"+id);
                            if(l.getId() == this.node.getId())
                                this.node.getFingerTable().getSuccessorList().add(this.toFinger(this.node));
                            throw new UnableToFindSuccessorException();
                        }
                    }
                }  
            }
            return l;
        }
    }
    @Override
    public Node findPredecessor(long id) throws UnableToFindPredecessorException, TException {
        int count = 0;
        long timeout = 0;
        Node aux = node;
        Finger successor = node.getFingerTable().getSuccessorList().get(0);
        while(true){
            try{
                while(!check_interval(id,aux.getId(),true,successor.getId(),false)){ //while(id not in (n',n'.successor])
                    if(aux != node){ 
                        TTransport transport = new TSocket(aux.getIp(),aux.getPort());
                        transport.open();
                        TProtocol protocol = new TCompactProtocol(transport);
                        Chord.Client cli = new Chord.Client(protocol);
                        aux = cli.closestPrecedingFinger(id);
                        transport.close();
                    }else{
                        aux = this.closestPrecedingFinger(id);
                    } //n' = n'.closestPrecedingFinger
                    successor = aux.getFingerTable().getSuccessorList().get(0);
                }
                break;
            }catch(TException ex){
                this.trace(aux.getName()+" @ <"+aux.getIp()+":"+aux.getPort()+"> did not respond closestPrecedingFinger for id: "+id+" at findPredecessor");
                if(++count == 5){
                    this.trace(aux.getName()+" @ <"+aux.getIp()+":"+aux.getPort()+"> didn't respond to the last 5 tries.");
                    throw new UnableToFindPredecessorException();
                }else{
                    try{
                        Thread.sleep(timeout);
                        timeout = this.getTimeout(timeout);
                    }catch(InterruptedException ie){
                        this.trace("Timeout interrupt at findPredecessor");
                    }
                }
            }
        }
        return aux;
    }
    @Override
    public Node closestPrecedingFinger(long id) throws UnableToFindClosestPrecedingFingerException, TException {
        for(int i= node.getNumBits()-1; i >= 0 ; i--){ //Percorre Todas as Linhas da FingerTable de trás para frente
            if(DORIFHandler.verboseTrace)
                this.trace("Searching for closest preceding finger for id:"+id);
            if(check_interval(node.getFingerTable().getFingers().get(i).getId(),node.getId(),true,id,true)){
                if(node.getId() != node.getFingerTable().getFingers().get(i).getId()){ //Se for outro servidor faz requisição via Thrift
                    Finger f = node.getFingerTable().getFingers().get(i);
                    int count = 0;
                    long timeout = 0;
                    while(count++ < 5){
                        try {
                            TTransport transport = new TSocket(f.getIp(),f.getPort());
                            transport.open();
                            TProtocol protocol = new TCompactProtocol(transport);
                            Chord.Client cli = new Chord.Client(protocol);
                            Node a = cli.sendSelf();
                            transport.close();
                            return a;
                        } catch (TException e) {
                            this.trace("Server "+f.getName()+"<"+f.getIp()+":"+f.getPort()+"> did not respond");
                            if(DORIFHandler.verboseTrace)
                                this.trace("Waiting "+timeout+"ms to try again...");
                            try{
                                Thread.sleep(timeout);
                                timeout = this.getTimeout(timeout);
                            } catch (InterruptedException ex) {
                                this.trace("Waiting thread at closest preceding finger interrupted");
                            }
                        }
                    }
                }else{ //Se for o próprio servidor retorna a sí próprio
                    return node;
                }
            } 
        }
        this.trace("No server was able to respond the closest preceding finger for id:"+id);
        throw new UnableToFindClosestPrecedingFingerException();
    }
    @Override
    public void transferKeys(Node n) throws UnableToTransferKeysException, TException {
        
    }
    @Override
    public void stabilize() throws UnableToStabilizeException, TException {
        if(DORIFHandler.verboseTrace)
            this.trace("Starting stabilization protocol");
        //GET SUCESSOR
        Finger x;
        Node a = null;
        TTransport transport = null;
        TProtocol protocol = null;
        Chord.Client cli = null;
        x = node.getFingerTable().getSuccessorList().get(0); //FIRST SUCCESSOR
        if(x.getId() != node.getId()){ //IF NOT ROOT NODE
            int count = 0;
            long timeout = 0;
            while(true){
                try {
                    transport = new TSocket(x.getIp(),x.getPort());
                    transport.open();
                    protocol = new TCompactProtocol(transport);
                    cli = new Chord.Client(protocol);
                    a = cli.sendSelf();
                    transport.close();
                    break;
                } catch (TException e) {
                    this.trace(x.getName()+" @ <"+x.getIp()+":"+x.getPort()+"> did not respond with your info for stabilization");
                    if(++count == 5){
                        this.trace(x.getName()+" @ <"+x.getIp()+":"+x.getPort()+"> didn't respond to the last 5 tries to get it's info for stabilization.");
                        if(DORIFHandler.stackTraceEnable)
                            e.printStackTrace();
                        this.trace("Removing "+x.getName()+" @ <"+x.getIp()+":"+x.getPort()+"> from successor list as it seems offline.");
                        this.node.getFingerTable().getSuccessorList().remove(x);
                        if(this.node.getPredecessor().getId() == x.getId())
                            this.node.setPredecessor(null);
                        if(this.node.getFingerTable().getSuccessorList().size() > 0){
                            x = this.node.getFingerTable().getSuccessorList().get(0);
                            this.trace("Trying with new successor "+x.getName()+" @ <"+x.getIp()+":"+x.getPort()+">");
                            this.node.getFingerTable().getFingers().set(0, x);
                            count = 0;
                        }else{
                            this.trace("There are no more successors in successor list. Setting self as successor");
                            this.node.getFingerTable().getSuccessorList().add(this.toFinger(this.node));
                            this.node.getFingerTable().getFingers().set(0, this.toFinger(this.node));
                            throw new UnableToStabilizeException();
                        }
                    }else{
                        try{
                            this.trace("Retrying get "+x.getName()+" @ <"+x.getIp()+":"+x.getPort()+"> info for stabilization in: "+timeout+"ms");
                            Thread.sleep(timeout);
                            timeout = this.getTimeout(timeout);
                        }catch(InterruptedException ie){
                            this.trace("Timeout interrupet at findPredecessor");
                        }
                    }
                }
            }
        }else{ //IF ROOT NODE
            a = node;
        }
        x = a.getPredecessor(); // ASK SUCCESSOR'S PREDECESSOR
        if(x != null && check_interval(x.getId(),node.getId(),true,node.getFingerTable().getSuccessorList().get(0).getId(),true)){
            //CHECK IF IT SHOULD BE SELF SUCESSOR
            Node b = null;
            try{
                transport = new TSocket(x.getIp(),
                    x.getPort());
                transport.open();
                protocol = new TCompactProtocol(transport);
                cli = new Chord.Client(protocol);
                b = cli.sendSelf();
                transport.close();
            }catch(TException te){
                b = a;
            }
            if(DORIFHandler.verboseTrace)
                this.trace("Determined that "+b.getName()+"("+b.getId()+")<"+b.getIp()+":"+b.getPort()+"> should be this node successor");
            Finger f = new Finger();
            f.setId(b.getId());
            f.setIp(b.getIp());
            f.setPort(b.getPort());
            f.setName(b.getName());
            node.getFingerTable().getFingers().set(0, f);
            node.getFingerTable().getSuccessorList().set(0, f);
        }
        //Notify sucessor of this node's existence
        if(node.getFingerTable().getSuccessorList().get(0).getId() != node.getId()){
            Finger n = node.getFingerTable().getFingers().get(0);
            if(DORIFHandler.verboseTrace)
                this.trace("Notifying "+n.getName()+"("+n.getId()+")<"+n.getIp()+":"+n.getPort()+"> of this node existence");
            transport = new TSocket(node.getFingerTable().getSuccessorList().get(0).getIp(),
                    node.getFingerTable().getSuccessorList().get(0).getPort());
            transport.open();
            protocol = new TCompactProtocol(transport);
            cli = new Chord.Client(protocol);
            cli.notify(node);
            transport.close();
        }
        if(DORIFHandler.verboseTrace)
            trace("ID:"+node.getId()+" Stabilization Protocol Executed!");
    }
    @Override
    public void notify(Node n) throws UnableToNotifyException, TException {
        Finger aux = new Finger();
        aux.setId(n.getId());
        aux.setIp(n.getIp());
        aux.setPort(n.getPort());
        aux.setName(n.getName());
        if(DORIFHandler.verboseTrace)
            this.trace("Got notified by:"+n.getName()+" @ <"+n.getIp()+":"+n.getPort()+">");
        if(this.node.getFingerTable().getSuccessorList().size() < 2){
            if(!this.node.getFingerTable().getSuccessorList().contains(aux))
                this.node.getFingerTable().getSuccessorList().add(aux);
        }
        Node test = null;
        TTransport transport = null;
        TProtocol protocol = null;
        Chord.Client cli = null;
        if(node.getPredecessor() != null){
            try{
                transport = new TSocket(node.getPredecessor().getIp(),
                        node.getPredecessor().getPort());
                transport.open();
                protocol = new TCompactProtocol(transport);
                cli = new Chord.Client(protocol);
                test = cli.sendSelf();
                transport.close();
            }catch(TException te){
                test = null;
                this.node.setPredecessor(null);
            }
        }
        if(node.getPredecessor() == null 
                || check_interval(n.getId(), node.getPredecessor().getId(),true,node.getId(),true
                || test == null)){
            node.setPredecessor(aux);
        }
    }
    @Override
    public void fixFingers() throws UnableToFixFingerException {
        try{
            if(DORIFHandler.verboseTrace)
                this.trace("Fixing this node's finger table");
            int i = DORIFHandler.lastFinger;
            Node x = findSuccessor((int)this.getEntryValue(i+1));
            if(DORIFHandler.verboseTrace)
                this.trace("Found that position "+(i)+" | "+this.getEntryValue(i+1)+" | is under "+x.getName()+"("+x.getId()+")<"+x.getIp()+":"+x.getPort()+"> responsability");
            Finger newfinger = new Finger();
            newfinger.setId(x.getId());
            newfinger.setName(x.getName());
            newfinger.setIp(x.getIp());
            newfinger.setPort(x.getPort());
            node.getFingerTable().getFingers().set(i, newfinger);
            if(i < 5 || this.node.getFingerTable().getSuccessorList().size() < 2)
                if(!this.node.getFingerTable().getSuccessorList().contains(newfinger))
                    this.node.getFingerTable().getSuccessorList().add(newfinger);
            DORIFHandler.lastFinger = (i == this.node.getNumBits()-1)?1:((i+1)%this.node.getNumBits());
        }catch(UnableToFindSuccessorException utfs){
            this.trace("Fix Finger Table Service failed due to not being able to find successor");
            throw new UnableToFixFingerException();
        }catch(TException te){
            this.trace("Fix Finger Table Serivce failed due to a communication error: "+te.getMessage());
            throw new UnableToFixFingerException();
        }
    }
    @Override
    public Node sendSelf() throws UnableToSendSelfException, TException {
        if(this.node == null){
            this.trace("Unable to Send Self");
            throw new UnableToSendSelfException();
        }
        if(DORIFHandler.verboseTrace)
            this.trace("Sending Self");
        return this.node;
    }
    @Override
    public void setPredecessor(Node l) throws UnableToSetPredecessorException, TException {
        if(l != null){
            if(DORIFHandler.verboseTrace)
                this.trace("Setting Predecessor "+l.getName()+"("+l.getId()+")<"+l.getIp()+":"+l.getPort()+">");
            synchronized(node.getPredecessor()){
                node.getPredecessor().setId(l.getId());
                node.getPredecessor().setIp(l.getIp());
                node.getPredecessor().setPort(l.getPort());
                node.getPredecessor().setName(l.getName());
            }
        }else{
            this.node.setPredecessor(null);
        }
    }
    ///////////////////////////////////////////////////////////////////////////////
    
    //DORIF VOCABULARY RELATED METHODS/////////////////////////////////////////////////////////
    @Override
    public void add(ITerm term) {
        //GENERATE KEY NUMBER
        long chave = this.hashChave(term.getText());
        this.trace("Key :" + chave + " generated for term " + term.getText());
        Node successor = null;
        try{    
            successor = this.findSuccessor(chave);
        }catch(UnableToFindSuccessorException utfs){
            this.trace("The Chord Ring of Servers was unable to find the key's "+chave+" successor");
            return;
        }catch(TException te){
            te.printStackTrace();
            this.trace("The term could not be inserted due to: "+te.getMessage());
            return;
        }
        //CHECK IF ITS THIS SERVER RESPONSABILITY
        if(successor.getId() == this.node.getId()){
        //IF IT IS
            DORIFHandler.vocabulary.computeIfPresent(term.getText(), (key,value)->{
                value.incrementFrequency();
                return value;
            });
            DORIFHandler.vocabulary.computeIfAbsent(term.getText(), (key)->{
                return term;
            });
        this.trace("Term "+term.getText()+" added to the global vocabulary, on this server's responsability");
        }else{
        //IF ITS NOT
            try{
                this.addTermo(term.getText());
            }catch(UnableToAddTermException utat){
                this.trace(utat.getErrMsg());
            }catch(TException te){
                te.printStackTrace();
            }
        }
    }

    @Override
    public ITerm get(String term) {
        //GENERATE KEY NUMBER
        long chave = this.hashChave(term);
        this.trace("Key :" + chave + " generated for term " + term);
        Node successor = null;
        try{    
            successor = this.findSuccessor(chave);
        }catch(UnableToFindSuccessorException utfs){
            this.trace("The Chord Ring of Servers was unable to find the key's "+chave+" successor");
            return null;
        }catch(TException te){
            te.printStackTrace();
            this.trace("The term could not be inserted due to: "+te.getMessage());
            return null;
        }
        //CHECK IF ITS THIS SERVER RESPONSABILITY
        if(successor.getId() == this.node.getId()){
        //IF IT IS
            ITerm result = DORIFHandler.cHandler.getFactory().createGlobalTerm(term);
            result.setFrequency(DORIFHandler.vocabulary.get(term).getFrequency());
            return result;
        }else{
        //IF ITS NOT
            try{
                Term aux = this.getTermo(term);
                ITerm result = DORIFHandler.cHandler.getFactory().createGlobalTerm(term);
                result.setFrequency(aux.getFrequency());
                return result;
            }catch(TermNotFoundException utat){
                this.trace(utat.getErrMsg());
                return null;
            }catch(TException te){
                te.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public boolean contains(String term) {
        return DORIFHandler.vocabulary.containsKey(term);
    }

    @Override
    public Collection<ITerm> getTerms() {
        ArrayList<Term> all = new ArrayList<>(1000);
        ArrayList<ITerm> allTerms = new ArrayList<>();
        try{    
            all.addAll(this.getTotalGlobalTerms());
            allTerms.ensureCapacity(all.size());
            all.forEach((termo)->{
                ITerm aux = DORIFHandler.cHandler.getFactory().createGlobalTerm(termo.getTerm());
                aux.setFrequency(termo.getFrequency());
                aux.updateWeights();
                allTerms.add(aux);
            });
        }catch(TException te){
            if(DORIFHandler.traceEnable)
                this.trace("Failed to retrieve all terms from collection");
        }
        return allTerms;
    }
    ////////////////////////////////////////////////////////////////////////////////
    
    //ACCESSORY METHODS////////////////////////////////////////////////////////
    protected final void trace(String trace){
        if(DORIFHandler.traceEnable){
            try {
                this.writer.write("["+(new Timestamp(System.currentTimeMillis()))+"][DORIFServer]["+Thread.currentThread().getName()+"] "+trace+"\n");
                this.writer.flush();
            } catch (IOException e) {
                System.out.println("Unable to log: "+e.getMessage());
                if(DORIFHandler.stackTraceEnable)
                    e.printStackTrace();
            }   
        }
    }
    private boolean check_interval(long x, long a, boolean opena, long b, boolean openb){
        boolean result;
        if(a == b){
            result = !((opena && openb) && x == a);
        }else{
            if( (!opena && x==a) || (!openb && x==b) ){
                result =  true;
            }else if(a < b){
                result =  x > a && x < b;
            }else{
                result =  x > a || (x >= 0 && x < b);
            }
        }
        if(DORIFHandler.verboseTrace)
            this.trace(x+" "+(result?"is":"isn't")+" between "+(opena?"(":"[")+a+","+b+(openb?")":"]"));
        return result;
    }
    protected final long hashChave(String key){
        byte[] hash = this.md.digest(key.getBytes());
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(hash, 0, Long.BYTES);
        buffer.flip();//need flip 
        return (Math.abs(buffer.getLong()) % (1 << this.node.getNumBits()));
    }
    protected final Term toTerm(ITerm term){
        Term t = new Term();
        t.setTerm(term.getText());
        t.setFrequency(term.getFrequency());
        return t;
    }
    protected void printServerStatus(){
        StringBuilder out = new StringBuilder("");
        out.append("\n------Server Status------\n");
        out.append("ID:");
        out.append(this.node.getId());
        out.append("\t\tName:");
        out.append(this.node.getName());
        out.append("\n");
        if(this.node.getPredecessor() != null){
            out.append("Predecessor: ");
            out.append(this.node.getPredecessor().getId());
            out.append("\n");
        }
        else
            out.append("Predecessor: NULL\n");
        out.append("Successor List = { ");
        Finger suc;
        if(!this.node.getFingerTable().getSuccessorList().isEmpty()){
            suc = this.node.getFingerTable().getSuccessorList().get(0);
            out.append(suc.getName());
            out.append(" @ <");
            out.append(suc.getIp());
            out.append(":");
            out.append(suc.getPort());
            out.append(">");
        }
        for(int i = 1; i < this.node.getFingerTable().getSuccessorList().size(); i++){
            suc = this.node.getFingerTable().getSuccessorList().get(i);
            out.append(" ; ");
            out.append(suc.getName());
            out.append(" @ <");
            out.append(suc.getIp());
            out.append(":");
            out.append(suc.getPort());
            out.append(">");
        }
        out.append(" }\n");
        for(int i = 0; i < this.node.getNumBits(); i++){
            List<Finger> aux = this.node.getFingerTable().getFingers();
            out.append("Finger (");
            out.append(i);
            out.append(") |");
            out.append(this.nfmt.format(this.getEntryValue(i+1)));
            out.append("| -> ");
            out.append(this.nfmt.format(aux.get(i).getId()));
            out.append("\n");
        }
        this.trace(out.toString());
    }
    private long getEntryValue(int i){
        return (this.node.getId() + (1 << (i-1))) % (1 << this.node.getNumBits());
    }
    private long getTimeout(long oldTimeout){
        long newTimeout = oldTimeout;
        if(oldTimeout < 500)
            newTimeout += 500;
        else if(oldTimeout < 1000)
            newTimeout += 700;
        else if(oldTimeout < 2000)
            newTimeout += 1000;
        else
            newTimeout = oldTimeout;
        return newTimeout;
    }
    private Finger toFinger(Node x){
        Finger aux = new Finger();
        aux.setId(x.getId());
        aux.setIp(x.getIp());
        aux.setName(x.getName());
        aux.setPort(x.getPort());
        return aux;
    }
    ////////////////////////////////////////////////////////////////////////////////
    
    //DORIF SERVICE METHODS/////////////////////////////////////////////////////////////
    @Override
    public List<RankedDoc> query(String text) throws TException{
        ArrayList<RankedDoc> result = new ArrayList<>(5000);
        result.addAll(this.queryServer(text));
        Node aux = this.findSuccessor(this.node.getId()+1);
        while(aux.getId() != this.node.getId()){
            TTransport transport = null;
            TProtocol protocol = null;
            try{
                if(DORIFHandler.traceEnable)
                    this.trace("Requesting server "+aux.getName()+"@<"+aux.getIp()+":"+aux.getPort()+"> for its query results");
                transport = new TSocket(aux.getIp(), aux.getPort());
                transport.open();
                protocol = new TCompactProtocol(transport);
                DORIFService.Client cli = new DORIFService.Client(protocol);
                result.addAll(cli.queryServer(text));
            }catch(TTransportException tte){
                if(DORIFHandler.traceEnable)
                    this.trace("Failed to get server's query results");
                tte.printStackTrace();
            }catch(TException te){
                if(DORIFHandler.traceEnable)
                    this.trace("Failed to recover server's query results." + te.getMessage());
                te.printStackTrace();
            }
            aux = this.findSuccessor(aux.getId()+1);
        }
        Collections.sort(result, (a1,a2)->{
            double sc1 = a1.getScore();
            double sc2 = a2.getScore();
            if(sc1 > sc2)
                return -1;
            else if(sc2 > sc1)
                return 1;
            else
                return 0;
        });
        return result;
    }
    @Override
    public List<RankedDoc> queryServer(String text) throws TimeOutException, TException {
        IQuery query = DORIFHandler.cHandler.getFactory().createQuery(text);
        query.setGlobalVocabulary(this);
        query.prepare();
        Collection<RankedDoc> temp = DORIFHandler.cHandler.query(query);
        ArrayList<RankedDoc> result = new ArrayList<>(temp.size());
        for(RankedDoc doc : temp){
            doc.setServerId(this.node.getId());
            doc.setServerName(this.node.getName());
            result.add(doc);
        }
        return result;
    }
    @Override
    public ByteBuffer getDocument(long id) throws TException{
        return null;
    }
    @Override
    public void addTermo(String text) throws UnableToAddTermException, TException {
        //GENERATE KEY NUMBER
        long key = this.hashChave(text);
        this.trace("Key :" + key + " generated for term " + text);
        Node successor = null;
        try{    
            successor = this.findSuccessor(key);
        }catch(UnableToFindSuccessorException utfs){
            this.trace("The Chord Ring of Servers was unable to find the key's "+key+" successor");
            return;
        }catch(TException te){
            te.printStackTrace();
            this.trace("The term could not be inserted due to: "+te.getMessage());
            return;
        }
        //CHECK IF ITS THIS SERVER RESPONSABILITY
        if(successor.getId() == this.node.getId()){
        //IF IT IS
            this.add(DORIFHandler.cHandler.createGlobalTerm(text));
        }else{
        //IF ITS NOT
            TTransport transport = null;
            TProtocol protocol = null;
            try{
                this.trace("Term "+text+" is under server "+successor.getName()+"@<"+successor.getIp()+":"+successor.getPort()+"> responsability");
                transport = new TSocket(successor.getIp(), successor.getPort());
                transport.open();
                protocol = new TCompactProtocol(transport);
                DORIFService.Client cli = new DORIFService.Client(protocol);
                this.trace("Sending term to server...");
                cli.addTermo(text);
            }catch(UnableToAddTermException utat){
                this.trace("The term "+text+" could not be added");
                utat.printStackTrace();
            }catch(TTransportException tte){
                this.trace("Failed to send term adding request to server "+successor.getName()+"@<"+successor.getIp()+":"+successor.getPort()+">");
            }catch(TException te){
                this.trace("Failed to add term." + te.getMessage());
                te.printStackTrace();
            }finally{
                if(transport != null)
                    transport.close();
            }
        }
    }
    @Override
    public Term getTermo(String term) throws TermNotFoundException, TException{
        //GENERATE KEY NUMBER
        long key = this.hashChave(term);
        this.trace("Key :" + key + " generated for term " + term);
        Node successor = null;
        try{    
            successor = this.findSuccessor(key);
        }catch(UnableToFindSuccessorException utfs){
            if(DORIFHandler.traceEnable)
                this.trace("The Chord Ring of Servers was unable to find the key's "+key+" successor");
            return null;
        }catch(TException te){
            te.printStackTrace();
            if(DORIFHandler.traceEnable)
                this.trace("The term could not be inserted due to: "+te.getMessage());
            return null;
        }
        //CHECK IF ITS THIS SERVER RESPONSABILITY
        if(successor.getId() == this.node.getId()){
        //IF IT IS
            if(this.contains(term))
                return this.toTerm(DORIFHandler.vocabulary.get(term));
            else
                throw new TermNotFoundException();
        }else{
        //IF ITS NOT
            TTransport transport = null;
            TProtocol protocol = null;
            try{
                if(DORIFHandler.traceEnable)
                    this.trace("Term "+term+" is under server "+successor.getName()+"@<"+successor.getIp()+":"+successor.getPort()+"> responsability");
                transport = new TSocket(successor.getIp(), successor.getPort());
                transport.open();
                protocol = new TCompactProtocol(transport);
                DORIFService.Client cli = new DORIFService.Client(protocol);
                if(DORIFHandler.traceEnable)
                    this.trace("Requesting term to server...");
                Term result = cli.getTermo(term);
                if(result == null)
                    throw new TermNotFoundException();
                return result;
            }catch(TermNotFoundException tnfe){
                if(DORIFHandler.traceEnable)
                    this.trace(tnfe.getErrMsg());
                throw new TermNotFoundException();
            }catch(TTransportException tte){
                if(DORIFHandler.traceEnable)
                    this.trace("Failed to send term revery request to server "+successor.getName()+"@<"+successor.getIp()+":"+successor.getPort()+">");
                throw tte;
            }catch(TException te){
                if(DORIFHandler.traceEnable)
                    this.trace("Failed to recover term." + te.getMessage());
                throw te;
            }finally{
                if(transport != null)
                    transport.close();
            }
        }
    }
    @Override
    public boolean containsTermo(String term){
        Node aux = null;
        try {
            aux = this.findSuccessor(this.hashChave(term));
        } catch (UnableToFindSuccessorException ex) {
            if(DORIFHandler.traceEnable)
                this.trace(ex.getErrMsg());
            return false;
        } catch (TException te){
            if(DORIFHandler.traceEnable)
                this.trace(te.getMessage());
            return false;
        }
        if(aux.getId() == this.node.getId()){
            return DORIFHandler.vocabulary.containsKey(term);
        }else{
            TTransport transport = null;
            TProtocol protocol = null;
            try{
                if(DORIFHandler.traceEnable)
                    this.trace("Term "+term+" is under server "+aux.getName()+"@<"+aux.getIp()+":"+aux.getPort()+"> responsability.");
                transport = new TSocket(aux.getIp(), aux.getPort());
                transport.open();
                protocol = new TCompactProtocol(transport);
                DORIFService.Client cli = new DORIFService.Client(protocol);
                if(DORIFHandler.traceEnable)
                    this.trace("verifying if the server contains the term in its vocabulary");
                return cli.containsTermo(term);
            }catch(TTransportException tte){
                if(DORIFHandler.traceEnable)
                    this.trace("Failed to verify if server contains term "+aux.getName()+"@<"+aux.getIp()+":"+aux.getPort()+">");
                tte.printStackTrace();
                return false;
            }catch(TException te){
                if(DORIFHandler.traceEnable)
                    this.trace("Failed to recover term." + te.getMessage());
                te.printStackTrace();
                return false;
            }
        }
    }
    @Override
    public List<Term> getServerGlobalTerms() throws TException {
        ArrayList<Term> result = new ArrayList<>(DORIFHandler.vocabulary.size());
        Iterator<ITerm> ite = DORIFHandler.vocabulary.values().iterator();
        while(ite.hasNext()){
            result.add(this.toTerm(ite.next()));
        }
        return result;
    }
    @Override
    public List<Term> getTotalGlobalTerms() throws TException {
        ArrayList<Term> result = new ArrayList<>(5000);
        result.addAll(this.getServerGlobalTerms());
        Node aux = this.findSuccessor(this.node.getId()+1);
        while(aux.getId() != this.node.getId()){
            TTransport transport = null;
            TProtocol protocol = null;
            try{
                if(DORIFHandler.traceEnable)
                    this.trace("Requesting server "+aux.getName()+"@<"+aux.getIp()+":"+aux.getPort()+"> for its terms");
                transport = new TSocket(aux.getIp(), aux.getPort());
                transport.open();
                protocol = new TCompactProtocol(transport);
                DORIFService.Client cli = new DORIFService.Client(protocol);
                result.addAll(cli.getServerGlobalTerms());
            }catch(TTransportException tte){
                if(DORIFHandler.traceEnable)
                    this.trace("Failed to get server's terms");
                tte.printStackTrace();
            }catch(TException te){
                if(DORIFHandler.traceEnable)
                    this.trace("Failed to recover server's terms." + te.getMessage());
                te.printStackTrace();
            }
            aux = this.findSuccessor(aux.getId()+1);
        }
        return result;
    }
    @Override
    public List<GlobalTermInfo> getServerGlobalTermsInfo() throws TException {
        ArrayList<GlobalTermInfo> result = new ArrayList<>(5000);
        Iterator<ITerm> ite = DORIFHandler.vocabulary.values().iterator();
        long num_docs = this.getTotalNumDocs();
        while(ite.hasNext()){
            ITerm aux = ite.next();
            GlobalTermInfo inf = new GlobalTermInfo();
            inf.setId(this.hashChave(aux.getText()));
            inf.setFrequency(aux.getFrequency());
            inf.setTerm(aux.getText());
            ((IDFWeight)aux.getWeight("IDF")).setNumDocs(num_docs);
            aux.updateWeights();
            inf.setIdf(aux.getWeight("IDF").getWeight());
            result.add(inf);
        }
        return result;
    }
    @Override
    public List<DocTermInfo> getServerDocTermsInfo() throws TException {
        ArrayList<DocTermInfo> result = new ArrayList<>(5000);
        Iterator<IDocument> ite = DORIFHandler.cHandler.getDocuments().iterator();
        while(ite.hasNext()){
            IDocument doc = ite.next();
            doc.getTerms().forEach((termo)->{
                DocTermInfo info = new DocTermInfo();
                info.setDocName(doc.getName());
                info.setFrequency(termo.getFrequency());
                info.setId(this.hashChave(termo.getText()));
                info.setTerm(termo.getText());
                info.setTf(termo.getWeight("TF").getWeight());
                info.setTfidf(termo.getWeight("TF-IDF").getWeight());
                result.add(info);
            });
        }
        return result;
    }
    @Override
    public List<GlobalTermInfo> getTotalGlobalTermsInfo() throws TException {
        ArrayList<GlobalTermInfo> result = new ArrayList<>(5000);
        result.addAll(this.getServerGlobalTermsInfo());
        Node aux = this.findSuccessor(this.node.getId()+1);
        while(aux.getId() != this.node.getId()){
            TTransport transport = null;
            TProtocol protocol = null;
            try{
                if(DORIFHandler.traceEnable)
                    this.trace("Requesting server "+aux.getName()+"@<"+aux.getIp()+":"+aux.getPort()+"> for its global terms info");
                transport = new TSocket(aux.getIp(), aux.getPort());
                transport.open();
                protocol = new TCompactProtocol(transport);
                DORIFService.Client cli = new DORIFService.Client(protocol);
                result.addAll(cli.getServerGlobalTermsInfo());
            }catch(TTransportException tte){
                if(DORIFHandler.traceEnable)
                    this.trace("Failed to get server's global terms info");
                tte.printStackTrace();
            }catch(TException te){
                if(DORIFHandler.traceEnable)
                    this.trace("Failed to recover server's global tems info." + te.getMessage());
                te.printStackTrace();
            }
            aux = this.findSuccessor(aux.getId()+1);
        }
        return result;
    }
    @Override
    public List<DocTermInfo> getTotalDocTermsInfo() throws TException {
        ArrayList<DocTermInfo> result = new ArrayList<>(5000);
        result.addAll(this.getServerDocTermsInfo());
        Node aux = this.findSuccessor(this.node.getId()+1);
        while(aux.getId() != this.node.getId()){
            TTransport transport = null;
            TProtocol protocol = null;
            try{
                if(DORIFHandler.traceEnable)
                    this.trace("Requesting server "+aux.getName()+"@<"+aux.getIp()+":"+aux.getPort()+"> for its documents terms info");
                transport = new TSocket(aux.getIp(), aux.getPort());
                transport.open();
                protocol = new TCompactProtocol(transport);
                DORIFService.Client cli = new DORIFService.Client(protocol);
                result.addAll(cli.getServerDocTermsInfo());
            }catch(TTransportException tte){
                if(DORIFHandler.traceEnable)
                    this.trace("Failed to get server's documents terms info");
                tte.printStackTrace();
            }catch(TException te){
                if(DORIFHandler.traceEnable)
                    this.trace("Failed to recover server's document terms info." + te.getMessage());
                te.printStackTrace();
            }
            aux = this.findSuccessor(aux.getId()+1);
        }
        return result;
    }
     @Override
    public List<DocInfo> getServerDocInfos() throws TException {
        ArrayList<DocInfo> result = new ArrayList<>(1000);
        DocInfo di;
        for(IDocument doc : DORIFHandler.cHandler.getDocuments()){
            di  = new DocInfo();
            di.setId(doc.getID());
            di.setName(doc.getName());
            di.setNorma(doc.getNorma());
            di.setPath(doc.getPath());
            di.setServerId(this.node.getId());
            di.setServerName(this.node.getName());
            result.add(di);
        }
        return result;
    }
    @Override
    public List<DocInfo> getTotalDocInfo() throws TException {
        ArrayList<DocInfo> result = new ArrayList<>(5000);
        result.addAll(this.getServerDocInfos());
        Node aux = this.findSuccessor(this.node.getId()+1);
        while(aux.getId() != this.node.getId()){
            TTransport transport = null;
            TProtocol protocol = null;
            try{
                if(DORIFHandler.traceEnable)
                    this.trace("Requesting server "+aux.getName()+"@<"+aux.getIp()+":"+aux.getPort()+"> for its documents infos");
                transport = new TSocket(aux.getIp(), aux.getPort());
                transport.open();
                protocol = new TCompactProtocol(transport);
                DORIFService.Client cli = new DORIFService.Client(protocol);
                result.addAll(cli.getServerDocInfos());
            }catch(TException te){
                if(DORIFHandler.traceEnable)
                    this.trace("Failed to request server "+aux.getName()+"@<"+aux.getIp()+":"+aux.getPort()+"> for its documents infos");
                te.printStackTrace();
            }
            aux = this.findSuccessor(aux.getId()+1);
        }
        return result;
    }
    @Override
    public long getServerNumDocs(){
        if(DORIFHandler.traceEnable)
            this.trace("Requested for this server's document count");
        return DORIFHandler.cHandler.getLocalNumDocs();
    }
    @Override
    public long getTotalNumDocs() throws TException{
        if(DORIFHandler.traceEnable)
            this.trace("Requested for Total Document Count");
        long result = 0;
        result+= DORIFHandler.cHandler.getLocalNumDocs();
        try{
            Node aux = this.findSuccessor(this.node.getId()+1);
            TTransport transport = null;
            TProtocol protocol = null;
            DORIFService.Client cli = null;
            while(aux.getId() != this.node.getId()){
                transport = new TSocket(aux.getIp(), aux.getPort());
                transport.open();
                protocol = new TCompactProtocol(transport);
                cli = new DORIFService.Client(protocol);
                result += cli.getServerNumDocs();
                aux = this.findSuccessor(aux.getId()+1);
            }
        }catch(TException te){
            if(DORIFHandler.traceEnable)
                this.trace("Failed to recover document count from all the servers");
            throw te;
        }
        if(DORIFHandler.verboseTrace)
            this.trace("Foundo Total Document Count: "+result);
        return result;
    }
    //////////////////////////////////////////////////////////////////////////////
    //DORIFAdmin Methods/////////////////////////////////////////////////////////
    @Override
    public void startCollecting() throws TException {
        DORIFHandler.cHandler.startCollecting();
    }
    @Override
    public void stopCollecting() throws TException {
        DORIFHandler.cHandler.stopCollecting();
    }
    
}
