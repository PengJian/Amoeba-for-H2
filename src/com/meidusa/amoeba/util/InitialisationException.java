/*
 * 	This program is free software; you can redistribute it and/or modify it under the terms of 
 * the GNU AFFERO GENERAL PUBLIC LICENSE as published by the Free Software Foundation; either version 3 of the License, 
 * or (at your option) any later version. 
 * 
 * 	This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GNU AFFERO GENERAL PUBLIC LICENSE for more details. 
 * 	You should have received a copy of the GNU AFFERO GENERAL PUBLIC LICENSE along with this program; 
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.meidusa.amoeba.util;

public class InitialisationException extends Exception {

	private static final long serialVersionUID = 1L;
	/**
	 * Constructs a <code>InitialisationException</code> with no detail
	 * message.
	 */
	public InitialisationException() {
	}

	/**
	 * Constructs a <code>InitialisationException</code> with the specified
	 * detail message.
	 * 
	 * @param s  the detail message.
	 */
	public InitialisationException(String s) {
		super(s);
	}

	/**
	 * Constructs a <code>InitialisationException</code> with no detail
	 * message.
	 */
	public InitialisationException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a <code>ConfigurationException</code> with the specified
	 * detail message.
	 * 
	 * @param s the detail message.
	 */
	public InitialisationException(String s, Throwable cause) {
		super(s,cause);
	}

}
