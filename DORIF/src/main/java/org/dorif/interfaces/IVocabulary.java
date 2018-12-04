/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dorif.interfaces;

import java.util.Collection;

/**
 *
 * @author arthb
 */
public interface IVocabulary {
    public void add(ITerm term);
    public ITerm get(String term);
    public boolean contains(String term);
    public Collection<ITerm> getTerms();
}
