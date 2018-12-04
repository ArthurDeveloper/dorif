/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dorif.vectorial;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.dorif.interfaces.ITerm;
import org.dorif.interfaces.IWeight;

/**
 *
 * @author Arthur
 */
public class VectorialDocTerm implements ITerm{
    private final String text;
    private AtomicLong frequency = new AtomicLong(1);
    private ConcurrentHashMap<String, IWeight> weights;
    
    public VectorialDocTerm(String text){
        if(text == null || text.isEmpty())
            throw new IllegalArgumentException("Cannot create a term without text");
        this.text = text;
        this.weights = new ConcurrentHashMap<>();
        this.weights.put("TF", new TFWeight(1));
        this.weights.put("TF-IDF", new TFIDFWeight());
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public IWeight getWeight(String name) {
        return this.weights.get(name);
    }

    @Override
    public void updateWeights() {
        this.weights.get("TF").updateWeight();
        ((TFIDFWeight)this.weights.get("TF-IDF")).setTF(this.weights.get("TF").getWeight());
    }
    
    public void setIDF(double idf){
        ((TFIDFWeight)this.weights.get("TF-IDF")).setIDF(idf);
        this.updateWeights();
    }

    @Override
    public Set<Map.Entry<String, IWeight>> getWeightSet() {
        return this.weights.entrySet();
    }

    @Override
    public long getFrequency() {
        return this.frequency.get();
    }

    @Override
    public synchronized void incrementFrequency() {
        this.frequency.incrementAndGet();
        this.weights.get("TF").incrementFrequency();
        this.updateWeights();
    }

    @Override
    public synchronized void setFrequency(long frequency) {
        this.frequency.set(frequency);
        ((TFWeight)this.weights.get("TF")).setFrequency(frequency);
        this.updateWeights();
    }
    
}