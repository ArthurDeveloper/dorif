/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dorif.interfaces;

import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author arthb
 */
public interface ITerm {
    public String getText();
    public IWeight getWeight(String name);
    public Set<Entry<String,IWeight>> getWeightSet();
    public void updateWeights();
    public long getFrequency();
    public void setFrequency(long frequency);
    public void incrementFrequency();
}
