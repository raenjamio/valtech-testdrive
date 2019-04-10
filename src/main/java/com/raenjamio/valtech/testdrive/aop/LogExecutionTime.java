package com.raenjamio.valtech.testdrive.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author raenjamio
 * Anotacion que permite interceptar cualquier metodo y loguear 
 * el tiempo de ejecucion
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecutionTime {
 
}