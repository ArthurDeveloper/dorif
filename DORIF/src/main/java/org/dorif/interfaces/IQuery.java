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
public interface IQuery {
    public void setRankingFunction(IRankingFunction rf);
    public void setPreProcessor(IPreProcessor pp);
    public String getText();
    public void setGlobalVocabulary(IVocabulary global);
    public IVocabulary getLocalVocabulary();
    public void setCollectionHandler(ICollectionHandler ch);
    public double getNorma();
    public void prepare();
    public boolean isPrepared();
}
