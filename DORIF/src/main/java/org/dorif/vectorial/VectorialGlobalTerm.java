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
public class VectorialGlobalTerm implements ITerm{
    private final String text;
    private AtomicLong frequency = new AtomicLong(1);
    private ConcurrentHashMap<String, IWeight> weights;
    
    public VectorialGlobalTerm(String text){
        if(text == null || text.isEmpty())
            throw new IllegalArgumentException("Cannot create a term without text");
        this.text = text;
        this.weights = new ConcurrentHashMap<>();
        this.weights.put("IDF", new IDFWeight());
        this.weights.forEach((key,value)->{
            value.incrementFrequency();
        });
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
        this.weights.get("IDF").updateWeight();
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
    public void incrementFrequency() {
        this.frequency.incrementAndGet();
        this.weights.forEach((key,value)->{
            value.incrementFrequency();
        });
    }

    @Override
    public void setFrequency(long frequency) {
        this.frequency.set(frequency);
        this.weights.forEach((key,value)->{
            value.setFrequency(frequency);
        });
    }
}