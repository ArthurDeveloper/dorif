/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dorif.vectorial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import org.dorif.interfaces.ITerm;
import org.dorif.interfaces.IVocabulary;

/**
 *
 * @author Arthur
 */
public class VectorialVocabulary implements IVocabulary{
    private ConcurrentHashMap<String, ITerm> vocab;
    public VectorialVocabulary(){
        this.vocab = new ConcurrentHashMap<>();
    }
    @Override
    public void add(ITerm term) {
        synchronized(term){
            this.vocab.computeIfPresent(term.getText(), (key,value)->{
                value.incrementFrequency();
                return value;
            });
            this.vocab.computeIfAbsent(term.getText(), (key)->{
                return term;
            });
            term.updateWeights();
        }
    }

    @Override
    public ITerm get(String term) {
        return this.vocab.get(term);
    }

    @Override
    public boolean contains(String term) {
        return this.vocab.containsKey(term);
    }

    @Override
    public Collection<ITerm> getTerms() {
        ArrayList<ITerm> result = new ArrayList<>(this.vocab.size());
        Enumeration<ITerm> enume = this.vocab.elements();
        while(enume.hasMoreElements()){
            result.add(enume.nextElement());
        }
        return result;
    }
}