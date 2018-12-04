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
public interface IFactory {
    public void setPreProcessor(IPreProcessor pp);
    public void setRankingFunction(IRankingFunction rf);
    public void setCollectionHandler(ICollectionHandler ch);
    public ITerm createGlobalTerm(String text);
    public ITerm createDocTerm(String text);
    public IQuery createQuery(String text);
    public IDocument createDocument();
}
