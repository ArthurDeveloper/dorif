/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dorif.vectorial;

import org.dorif.interfaces.IDocument;
import org.dorif.interfaces.IQuery;
import org.dorif.interfaces.IRankingFunction;
import org.dorif.interfaces.ITerm;

/**
 *
 * @author Arthur
 */
public class VectorialRankingFunction implements IRankingFunction{
    @Override
    public double similarity(IQuery query, IDocument doc) {
        double sim = 0;
        for(ITerm term : query.getLocalVocabulary().getTerms()){
            if(doc.getLocalVocabulary().contains(term.getText())){
                sim+= term.getWeight("TF-IDF").getWeight() * doc.getLocalVocabulary().get(term.getText()).getWeight("TF-IDF").getWeight();
            }
        }
        if(sim != 0)
            sim = sim / (query.getNorma()*doc.getNorma());
        return sim;
    }
}
