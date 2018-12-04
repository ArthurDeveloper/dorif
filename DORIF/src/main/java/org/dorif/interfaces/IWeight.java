/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dorif.interfaces;

/**
 *
 * @author Arthur
 */
public interface IWeight {
    public double getWeight();
    public double updateWeight();
    public void incrementFrequency();
    public void setFrequency(long frequency);
}
