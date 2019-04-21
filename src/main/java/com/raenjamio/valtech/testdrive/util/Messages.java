package com.raenjamio.valtech.testdrive.util;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

/**
 * Generador de mensajes de acuerdo a messages.properties
 * @author raenjamio
 *
 */
@Component
public class Messages {

	private final MessageSource messageSource;
	
	private MessageSourceAccessor accessor;

	
	/**
	 * @param messageSource
	 */
	public Messages(MessageSource messageSource) {
		super();
		this.messageSource = messageSource;
	}

	@PostConstruct
	private void init() {
		accessor = new MessageSourceAccessor(messageSource, Locale.ENGLISH);
	}

	public String get(String code, String... args ) {
		return accessor.getMessage(code, args, Locale.ENGLISH);
	}

	public String get(String code) {
		return accessor.getMessage(code, Locale.ENGLISH);
	}
}