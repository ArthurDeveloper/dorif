/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.servidortxt;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.dorif.interfaces.ICollectionHandler;
import org.dorif.interfaces.ICollector;
import org.dorif.interfaces.IDocument;
import org.dorif.interfaces.IFactory;
import org.dorif.interfaces.IVocabulary;
import org.dorif.vectorial.VectorialDocument;

/**
 *
 * @author arthb
 */
public class TXTCollector implements ICollector{
    private ArrayList<ICollectionHandler> observers = new ArrayList<>();
    private ArrayList<IDocument> doc_buffer = new ArrayList<>(100);
    private ArrayList<String> visited = new ArrayList<>(200);
    private final String root_folder = "./base_livro/";
    private IFactory factory;
    private IVocabulary global;
    private boolean collect = true;
    private final String prefix = "[TXTCollector]";
    private ThreadPoolExecutor executorPool;
    public TXTCollector(){
        this.executorPool = new ThreadPoolExecutor(2, 4, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2));
    }
    @Override
    public void subscribeCollectionHandler(ICollectionHandler collectionHandler) {
        this.observers.add(collectionHandler);
    }

    @Override
    public void unsubscribeCollectionHandler(ICollectionHandler collectionHandler) {
        this.observers.remove(collectionHandler);
    }

    @Override
    public void notifySubscribers() {
        ArrayList<IDocument> temp = new ArrayList<IDocument>();
        temp.addAll(this.doc_buffer);
        this.doc_buffer.clear();
        this.observers.forEach((ich) -> {
            ich.updateCollection(temp);
        });
    }

    @Override
    public void setFactory(IFactory factory) {
        if(factory == null)
            throw new IllegalArgumentException("Cannot set a null factory");
        this.factory = factory;
    }
    @Override
    public void stopCollecting(){
        this.collect = false;
    }
    @Override
    public void startCollecting(){
        this.collect = true;
        this.executorPool.execute(this);
    }
    
    @Override
    public void run() {
        while(this.collect){
            File directory = new File(this.root_folder);
            File[] list = directory.listFiles();
            int counter = 0;
            for(File f:list){
                if (f.isFile() && !this.visited.contains(f.getPath())) {
                    IDocument a = this.factory.createDocument();
                    a.setName(f.getPath());
                    a.setPath(f.getPath());
                    this.visited.add(f.getPath());
                    this.doc_buffer.add(a);
                    counter++;
                }
            }
            if(counter > 0)
                notifySubscribers();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.trace("Stopped Collecting");
    }
    
    public void trace(String trace){
        System.out.println(this.prefix+" "+trace);
    }
}
