package com.mobyapp.segunda_evaluacion.exception;

public class RecursoNoEncontradoException extends Exception{

    public RecursoNoEncontradoException(String message){
        super(message);
    }
    public RecursoNoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }
}