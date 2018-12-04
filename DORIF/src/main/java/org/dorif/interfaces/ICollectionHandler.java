/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dorif.interfaces;

import java.util.Collection;
import org.dorif.handler.RankedDoc;

/**
 *
 * @author arthb
 */
public interface ICollectionHandler {
    public ITerm createDocTerm(String text);
    public ITerm createGlobalTerm(String text);
    public ICollector getCollector();
    public Collection<IDocument> getDocuments();
    public IFactory getFactory();
    public void setFactory(IFactory factory);
    public long getGlobalNumDocs();
    public void setGlobalNumDocs(long numDocs);
    public void setGlobalVocabulary(IVocabulary global);
    public long getLocalNumDocs();
    public IPreProcessor getPreProcessor();
    public void init();
    public Collection<RankedDoc> query(IQuery query);
    public void startCollecting();
    public void stopCollecting();
    public void updateCollection(Collection<IDocument> newDocuments);
}
