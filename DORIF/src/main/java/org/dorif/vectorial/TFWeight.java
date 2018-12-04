/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dorif.vectorial;

import org.dorif.interfaces.IWeight;

/**
 *
 * @author Arthur
 */
public class TFWeight implements IWeight{
    private double value;
    private long frequency;
    public TFWeight(long initialFrequency){
        this.frequency = initialFrequency;
        if(this.frequency > 0)
            this.value = 1+(Math.log10(frequency)/Math.log10(2));
    }

    @Override
    public double getWeight() {
        return this.value;
    }

    @Override
    public synchronized double updateWeight() {
        if(this.frequency > 0)
            this.value = 1 + (Math.log10(frequency)/Math.log10(2));
        return this.value;
    }

    @Override
    public void incrementFrequency() {
        synchronized(this){
            this.frequency++;
        }
        this.updateWeight();
    }
    
    @Override
    public void setFrequency(long frequency){
        synchronized(this){
            this.frequency = frequency;
        }
        this.updateWeight();
    }
}