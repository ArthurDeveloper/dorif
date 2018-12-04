/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dorif.interfaces;

/**
 *
 * @author arthb
 */
public interface IRankingFunction {
    //Computes the similarity grade. It should be supplied to a IQuery object
    //Strategy Pattern
    public double similarity(IQuery query, IDocument doc);
}
