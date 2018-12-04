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
public interface ICollector extends Runnable{
    public void setFactory(IFactory factory) throws IllegalArgumentException;
    public void notifySubscribers();
    public void startCollecting();
    public void stopCollecting();
    public void subscribeCollectionHandler(ICollectionHandler collectionHandler);
    public void unsubscribeCollectionHandler(ICollectionHandler collectionHandler);
}
