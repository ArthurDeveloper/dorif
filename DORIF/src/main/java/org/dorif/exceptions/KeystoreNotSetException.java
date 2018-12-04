/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dorif.exceptions;

/**
 *
 * @author Arthur
 */
public class KeystoreNotSetException extends Exception{
    public KeystoreNotSetException(String message, Throwable cause){
        super(message, cause);
    }
    public KeystoreNotSetException(String message){
        super(message);
    }
}
