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
public class InvalidServerTypeException extends Exception{
    public InvalidServerTypeException(String message, Throwable cause){
        super(message, cause);
    }
    public InvalidServerTypeException(String message){
        super(message);
    }
}
