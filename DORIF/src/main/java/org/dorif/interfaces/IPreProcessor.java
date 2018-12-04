/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dorif.interfaces;

import java.util.Collection;

public interface IPreProcessor {
    //Responsible for treating the input text, be it a word, a phrase or the full document
    //It should be used by IQuery and ICollector
    public void setFactory(IFactory factory) throws IllegalArgumentException;
    public void process(IDocument input);
    public void process(String input);
    public Collection<ITerm> getProcessedGlobal();
    public Collection<ITerm> getProcessedDoc();
}
