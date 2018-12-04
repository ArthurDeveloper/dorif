/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dorif.vectorial;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import org.dorif.interfaces.IDocument;
import org.dorif.interfaces.ITerm;
import org.dorif.interfaces.IVocabulary;

/**
 *
 * @author Arthur
 */
public class VectorialDocument implements IDocument{

    private final IVocabulary localVocabulary = new VectorialVocabulary();
    private IVocabulary globalVocabulary;
    private String path;
    private String name;
    private double norma;
    private String id;
    public VectorialDocument(String id){
        if(id == null || id.isEmpty())
            throw new IllegalArgumentException("Cannot set a null or empty name for document");
        this.id = id;
    }
    public void setPath(String path){
        if(path == null || path.isEmpty()){
            throw new IllegalArgumentException("Cannot set a null or empty path to a vectorial document");
        }
        this.path = path;
    }
    @Override
    public void setGlobalVocabulary(IVocabulary vocab) {
        this.globalVocabulary = vocab;
    }
    @Override
    public IVocabulary getLocalVocabulary() {
        return this.localVocabulary;
    }
    @Override
    public Collection<ITerm> getTerms() {
        return this.localVocabulary.getTerms();
    }
    @Override
    public String getContent() {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.path)));
            String linha;
            while((linha = reader.readLine()) != null){
                sb.append(linha);
                sb.append("\n");
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File of document: "+this.name+"@<"+this.path+"> was not found!");
        } catch (IOException ex){
            System.out.println("Error while reading file:"+ex.getMessage());
        }
        return sb.toString();
    }
    @Override
    public void setTerms(Collection<ITerm> terms) {
        terms.forEach((term)->{
            this.localVocabulary.add(term);
        });
        this.calcNorma();
    }
    @Override
    public void setNorma(double norma){
        this.norma = norma;
    }
    @Override
    public double getNorma(){
        return this.norma;
    }
    @Override
    public void calcNorma(){
        double sigma = 0;
        for(ITerm term : this.localVocabulary.getTerms()){
            VectorialDocTerm aux = ((VectorialDocTerm)term);
            aux.updateWeights();
            double tfidf = aux.getWeight("TF-IDF").getWeight();
            sigma += tfidf * tfidf;
        }
        this.norma = Math.sqrt(sigma);
    }
    @Override
    public void setName(String name) {
        if(name != null && !name.isEmpty())
            this.name = name;
        else
            throw new IllegalArgumentException("Cannot set a null or empty name for document");
    }
    @Override
    public String getName() {
        return this.name;
    }
    @Override
    public String getPath() {
        return this.path;
    }
    @Override
    public void setID(String id) {
        if(id == null || id.isEmpty())
            throw new IllegalArgumentException("Can't set a null or empty id.");
    }
    @Override
    public String getID() {
        return this.id;
    }
}
