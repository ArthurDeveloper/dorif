/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.servidortxt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import org.dorif.interfaces.IDocument;
import org.dorif.interfaces.IPreProcessor;
import org.dorif.interfaces.ITerm;
import org.dorif.interfaces.IFactory;

/**
 *
 * @author Arthur
 */
public class TXTPreProcessor implements IPreProcessor{
    private IFactory factory;
    private HashMap<String,ITerm> globals;
    private ArrayList<ITerm> docs;
    public TXTPreProcessor(){
    }
    public TXTPreProcessor(IFactory termFactory){
        if(termFactory == null)
            throw new IllegalArgumentException("Cannot create processor with null term factory");
        this.factory = termFactory;
    }
    @Override
    public void process(IDocument input) {
        this.process(input.getContent());
    }
    @Override
    public void setFactory(IFactory factory) {
        if(factory == null)
            throw new IllegalArgumentException("Cannot set a null factory");
        this.factory = factory;
    }

    @Override
    public Collection<ITerm> getProcessedGlobal() {
        return this.globals.values();
    }

    @Override
    public Collection<ITerm> getProcessedDoc() {
        return this.docs;
    }

    @Override
    public void process(String input) {
        if(this.factory == null)
            System.out.println("TermFactory not set on PreProcessor");
        if(this.globals != null)
            this.globals.clear();
        else
            this.globals = new HashMap<>(100);
        if(this.docs != null)
            this.docs.clear();
        else
            this.docs = new ArrayList<>(100);
        String saida = input
                .replaceAll("á|à|ã|â", "a")
                .replaceAll("é|è|ê", "e")
                .replaceAll("ó|ò|õ|ô", "o")
                .replaceAll("ú|ù", "u")
                .replaceAll("í|ì","i")
                .replaceAll("[^\\w]", " ")
                .replaceAll("[\\s]", " ")
                .replaceAll("\\s{2,}", " ")
                .toLowerCase();
        String[] words = (saida.trim()+" ").split(" ");
            for(String a : words){
                if(a.length() >= 2){
                    this.globals.putIfAbsent(a,this.factory.createGlobalTerm(a));
                    this.docs.add(this.factory.createDocTerm(a));
                }
            }
    }
}
