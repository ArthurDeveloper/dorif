/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dorif.vectorial;

import java.util.concurrent.atomic.AtomicLong;
import org.dorif.interfaces.ICollectionHandler;
import org.dorif.interfaces.IDocument;
import org.dorif.interfaces.ITerm;
import org.dorif.interfaces.IFactory;
import org.dorif.interfaces.IPreProcessor;
import org.dorif.interfaces.IQuery;
import org.dorif.interfaces.IRankingFunction;

/**
 *
 * @author Arthur
 */
public class VectorialFactory implements IFactory{
    private final AtomicLong idGenerator = new AtomicLong(0);
    private IRankingFunction rf;
    private IPreProcessor pp;
    private ICollectionHandler ch;
    @Override
    public ITerm createGlobalTerm(String text) {
        return new VectorialGlobalTerm(text);
    }
    @Override
    public ITerm createDocTerm(String text) {
        return new VectorialDocTerm(text);
    }

    @Override
    public IDocument createDocument() {
        return new VectorialDocument(Long.toString(idGenerator.getAndIncrement()));
    }

    @Override
    public IQuery createQuery(String text) {
        VectorialQuery query = new VectorialQuery(this.rf,this.pp,text);
        query.setCollectionHandler(ch);
        return query;
    }

    @Override
    public void setPreProcessor(IPreProcessor pp) {
        if(pp == null)
            throw new IllegalArgumentException("Cannot set a null PreProcessor in a factory");
        this.pp = pp;
    }

    @Override
    public void setRankingFunction(IRankingFunction rf) {
        if(rf == null)
            throw new IllegalArgumentException("Cannot set a null ranking function in a factory");
        this.rf = rf;
    }

    @Override
    public void setCollectionHandler(ICollectionHandler ch) {
        if(ch == null)
            throw new IllegalArgumentException("Cannot set null collection handler");
        this.ch = ch;
    }
}
