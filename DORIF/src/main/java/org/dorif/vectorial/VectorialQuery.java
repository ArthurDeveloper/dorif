/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dorif.vectorial;

import org.dorif.interfaces.ICollectionHandler;
import org.dorif.interfaces.IPreProcessor;
import org.dorif.interfaces.IQuery;
import org.dorif.interfaces.IRankingFunction;
import org.dorif.interfaces.ITerm;
import org.dorif.interfaces.IVocabulary;

/**
 *
 * @author Arthur
 */
public class VectorialQuery implements IQuery{
    private IRankingFunction rf;
    private IPreProcessor pp;
    private IVocabulary global;
    private final String text;
    private IVocabulary local;
    private boolean isPrepared = false;
    private double norma;
    private ICollectionHandler ch;
    public VectorialQuery(IRankingFunction rf, IPreProcessor pp, String text){
        if(rf == null)
            throw new IllegalArgumentException("Cannot create a query without a Ranking Function");
        this.rf = rf;
        if(pp == null)
            throw new IllegalArgumentException("Cannot create a query without a Pre Processor");
        this.pp = pp;
        if(text == null || text.isEmpty())
            throw new IllegalArgumentException("Cannot create query without a text");
        this.text = text;
        this.local = new VectorialVocabulary();
    }
    @Override
    public void setRankingFunction(IRankingFunction rf) {
        if(rf == null)
            throw new IllegalArgumentException("Cannot create a query without a Ranking Function");
        this.rf = rf;
    }

    @Override
    public void setPreProcessor(IPreProcessor pp) {
        if(pp == null)
            throw new IllegalArgumentException("Cannot set a null pre processor.");
        this.pp = pp;
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public void setGlobalVocabulary(IVocabulary global) {
        if(global == null)
            throw new IllegalArgumentException("Cannot set a null global vocabulary");
        this.global = global;
    }

    @Override
    public void prepare() {
        if(this.isPrepared)
            return;
        if(this.global == null)
            throw new IllegalStateException("Global vocabulary not set in query.");
        if(this.pp == null)
            throw new IllegalStateException("Pre processor not set in query.");
        if(this.rf == null)
            throw new IllegalArgumentException("Ranking funcion not set in query.");
        this.pp.process(this.text);
        for(ITerm term : this.pp.getProcessedDoc()){
            this.local.add(term);
        }
        for(ITerm term : this.local.getTerms()){
            VectorialDocTerm aux = ((VectorialDocTerm)term);
            if(this.global.get(term.getText()) != null){
                VectorialGlobalTerm glob = ((VectorialGlobalTerm)this.global.get(aux.getText()));
                ((IDFWeight)glob.getWeight("IDF")).setNumDocs(this.ch.getGlobalNumDocs());
                glob.updateWeights();
                aux.setIDF(glob.getWeight("IDF").getWeight());
                aux.updateWeights();
            }
        }
        //Calculate query's normalization
        double sigma = 0;
        for(ITerm term : this.local.getTerms()){
            sigma+= term.getWeight("TF-IDF").getWeight() * term.getWeight("TF-IDF").getWeight();
        }
        this.norma = Math.sqrt(sigma);
        this.isPrepared = true;
    }

    @Override
    public boolean isPrepared() {
        return this.isPrepared;
    }

    @Override
    public IVocabulary getLocalVocabulary() {
        return this.local;
    }

    @Override
    public double getNorma() {
        return this.norma;
    }

    @Override
    public void setCollectionHandler(ICollectionHandler ch) {
        if(ch == null)
            throw new IllegalArgumentException("Cannot set a null collection handler");
        this.ch = ch;
    }
    
}
