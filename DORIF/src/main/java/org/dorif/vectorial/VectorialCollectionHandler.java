/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dorif.vectorial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import org.dorif.handler.RankedDoc;
import org.dorif.interfaces.ICollectionHandler;
import org.dorif.interfaces.ICollector;
import org.dorif.interfaces.IDocument;
import org.dorif.interfaces.IPreProcessor;
import org.dorif.interfaces.IQuery;
import org.dorif.interfaces.ITerm;
import org.dorif.interfaces.IFactory;
import org.dorif.interfaces.IRankingFunction;
import org.dorif.interfaces.IVocabulary;

/**
 *
 * @author arthb
 */
public class VectorialCollectionHandler implements ICollectionHandler{
    private final String prefix = "[VectorialCollectionHandler] ";
    private ICollector collector;
    private IVocabulary globalVocabulary;
    private IPreProcessor preprocessor;
    private ConcurrentHashMap<String,IDocument> documents;
    private IFactory factory;
    private LinkedBlockingQueue<IDocument> doc_buffer;
    private AtomicLong numDocs = new AtomicLong(0);
    private IRankingFunction rf;
    public VectorialCollectionHandler(ICollector collector, IPreProcessor preprocessor, IFactory factory, IRankingFunction rf){
        this.collector = collector;
        this.preprocessor = preprocessor;
        this.documents = new ConcurrentHashMap<>(1000);
        this.factory = factory;
        this.collector.setFactory(factory);
        this.preprocessor.setFactory(factory);
        if(rf == null)
            throw new IllegalArgumentException("Cannot create a collection handler without a ranking function.");
        this.rf = rf;
        this.factory.setCollectionHandler(this);
        this.factory.setPreProcessor(preprocessor);
        this.factory.setRankingFunction(rf);
    }
    @Override
    public void init(){
        this.collector.subscribeCollectionHandler(this);
    }
    @Override
    public void updateCollection(Collection<IDocument> newDocuments) {
        this.doc_buffer = new LinkedBlockingQueue<>(newDocuments.size());
        this.doc_buffer.addAll(newDocuments);
        while(this.doc_buffer.size() > 0){
            IDocument aux = this.doc_buffer.poll();
            this.preprocessor.process(aux);
            Collection<ITerm> global = this.preprocessor.getProcessedGlobal();
            global.forEach((term) -> {
                this.globalVocabulary.add(term);
            });
            aux.setGlobalVocabulary(this.globalVocabulary);
            aux.setTerms(this.preprocessor.getProcessedDoc());
            this.documents.put(aux.getID(),aux);
            this.numDocs.incrementAndGet();
        }
        this.atualizaPesosDocs();
        this.trace("Collection Updated. "+newDocuments.size()+" documents included.");
    }
    
    private void trace(String trace){
        System.out.println(this.prefix+" "+trace);
    }
    
    public void setVocabulary(IVocabulary vocab){
        if(vocab == null)
            throw new IllegalArgumentException("Its not possible to set a null globalVocabulary");
        this.globalVocabulary = vocab;
    }

    @Override
    public Collection<RankedDoc> query(IQuery query) {
        ArrayList<RankedDoc> result = new ArrayList<>(2000);
        for(IDocument doc : this.documents.values()){
            double sim = 0;
            sim += this.rf.similarity(query, doc);
            RankedDoc aux = new RankedDoc();
            aux.setName(doc.getName());
            aux.setDesc("");
            aux.setId(doc.getID());
            aux.setScore(sim);
            result.add(aux);
        }
        //Que
        return result;
    }

    @Override
    public ITerm createGlobalTerm(String text) {
        return this.factory.createGlobalTerm(text);
    }
    
    @Override
    public ITerm createDocTerm(String text){
        return this.factory.createGlobalTerm(text);
    }

    @Override
    public IPreProcessor getPreProcessor() {
        return this.preprocessor;
    }

    @Override
    public ICollector getCollector() {
        return this.collector;
    }

    @Override
    public IFactory getFactory() {
        return this.factory;
    }

    @Override
    public void setFactory(IFactory factory) {
        this.factory = factory;
    }

    @Override
    public void setGlobalVocabulary(IVocabulary global) {
        if(global == null)
            throw new IllegalArgumentException("Its not possible to set a null globalVocabulary");
        this.globalVocabulary = global;
    }

    @Override
    public void startCollecting() {
        this.collector.startCollecting();
    }

    @Override
    public void stopCollecting() {
        this.collector.stopCollecting();
    }

    @Override
    public long getGlobalNumDocs() {
        return this.numDocs.get();
    }
    
    public void atualizaPesosDocs(){
        Thread t = new Thread(() -> {
            for(ITerm termo : this.globalVocabulary.getTerms()){
                VectorialGlobalTerm vgt = ((VectorialGlobalTerm)termo);
                ((IDFWeight)vgt.getWeight("IDF")).setNumDocs(this.getGlobalNumDocs());
                vgt.updateWeights();
                for(IDocument doc : this.documents.values()){
                    VectorialDocument vdoc = ((VectorialDocument)doc);
                    if(vdoc.getLocalVocabulary().contains(vgt.getText())){
                        VectorialDocTerm vdt = ((VectorialDocTerm)vdoc.getLocalVocabulary().get(vgt.getText()));
                        ((TFIDFWeight)vdt.getWeight("TF-IDF")).setIDF(vgt.getWeight("IDF").getWeight());
                        vdt.updateWeights();
                    }
                    vdoc.calcNorma();
                }
            }
        });
        t.start();
    }

    @Override
    public Collection<IDocument> getDocuments() {
        return this.documents.values();
    }

    @Override
    public void setGlobalNumDocs(long numDocs) {
        this.numDocs.set(numDocs);
        this.atualizaPesosDocs();
    }

    @Override
    public long getLocalNumDocs() {
        return (long)this.documents.size();
    }
}