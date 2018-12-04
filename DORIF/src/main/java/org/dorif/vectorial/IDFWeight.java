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
public class IDFWeight implements IWeight{
    private long frequency = 0;
    private long numDocs = 0;
    private double value = 0;

    @Override
    public double getWeight() {
        return this.value;
    }

    @Override
    public synchronized double updateWeight() {
        if(this.frequency > 0 && this.numDocs > 0)
            this.value = Math.log10((double)this.numDocs/(double)this.frequency)/Math.log10(2);
        else
            this.value = 0;
        return this.value;
    }
    
    public void setNumDocs(long numDocs){
        this.numDocs = numDocs;
    }

    @Override
    public void incrementFrequency() {
        synchronized(this){
            this.frequency++;
        }
        this.updateWeight();
    }

    @Override
    public void setFrequency(long frequency) {
        synchronized(this){    
            this.frequency = frequency;
        }
        this.updateWeight();
    }

}
