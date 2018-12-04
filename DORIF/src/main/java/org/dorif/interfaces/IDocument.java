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
public interface IDocument {
    public void calcNorma();
    public String getContent();
    public void setGlobalVocabulary(IVocabulary vocab);
    public String getID();
    public void setID(String id);
    public IVocabulary getLocalVocabulary();
    public String getName();
    public void setName(String name);
    public double getNorma();
    public void setNorma(double norma);
    public String getPath();
    public void setPath(String path);
    public Collection<ITerm> getTerms();
    public void setTerms(Collection<ITerm> terms);
}
