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
public class TFIDFWeight implements IWeight{
    private double tf;
    private double idf;
    private double tfidf;
    public TFIDFWeight(){
        this.tf = 0;
        this.idf = 0;
        this.tfidf = 0;
    }
    @Override
    public double getWeight() {
        return this.tfidf;
    }
    @Override
    public synchronized double updateWeight() {
        this.tfidf = this.tf*this.idf;
        return this.tfidf;
    }
    public synchronized void setTF(double tf){
        this.tf = tf;
        this.updateWeight();
    }
    public synchronized void setIDF(double idf){
        this.idf = idf;
        this.updateWeight();
    }

    @Override
    public void incrementFrequency() {
    }

    @Override
    public void setFrequency(long frequency) {
    }
}
