/*
 * Copyright (c) 2009 WiQuery team
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.odlabs.wiquery.plugins.hotkeys;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.resources.JavascriptResourceReference;
import org.odlabs.wiquery.core.behavior.WiQueryAbstractBehavior;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsStatement;

/**
 * $Id: HotkeysBehavior.java roche.jul $
 * 
 * <p>
 * 	Behavior to bind some key shortcut on your wicket components
 * </p>
 * 
 * <p>
 * 	See http://code.google.com/p/js-hotkeys/ and http://github.com/jeresig/jquery.hotkeys/
 * </p>
 *
 * @author Julien Roche
 * @since 1.0
 */
public class HotkeysBehavior extends WiQueryAbstractBehavior {
	
	/**
	 * Enumeration of possible combinaison keys
	 */
	public enum CombinaisonKeysEnum {
		Alt,
		Ctrl,
		Shift;
	}
	
	/**
	 * Enumeration of possible keys
	 */
	public enum KeysEnum {
		AT	("@"),
		BACKSLASH ("\\"),
		BACKSPACE,
		BRACELEFT	("<"),
		BRACERIGHT	(">"),
		CAPSLOCK,
		CIRCUMFLEX	("^"),
		COLON	(":"),
		COMMA	(","),
		DEL,
		DOLLAR	("$"),
		DOT	("."),
		DOWN,
		END,
		ESC,
		EXCLAMATION ("!"),
		F1,
		F2,
		F3,
		F4,
		F5,
		F6,
		F7,
		F8,
		F9,
		F10,
		F11,
		F12,
		HOME,
		INSERT,
		INTERROGATION	("?"),
		LEFT,
		LEFT_PARENTHESIS	("("),
		MINUS	("-"),
		MULTIPLY	("*"),
		NUMLOCK,
		NUMBER_SIGN	("#"),
		PAGEDOWN,
		PAGEUP,
		PAUSE,
		PERCENT	("%"),
		PIPE	("|"),
		RETURN,
		RIGHT,
		RIGHT_PARENTHESIS	(")"),
		SCROLL,
		SLASH	("/"),
		SPACE,
		TAB,
		UNDERSCORE	("_"),
		UP,
		
		A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,
		
		ZERO 	("0"),
		ONE		("1"),
		TWO		("2"),
		THREE	("3"),
		FOUR	("4"),
		FIVE	("5"),
		SIX		("6"),
		SEVEN	("7"),
		HEIGHT	("8"),
		NINE	("9");
		
		// Properties
		private String specialValue;
		
		KeysEnum() {
			specialValue = null;
		}
		
		KeysEnum(String specialValue){
			this.specialValue = specialValue;
		}

		/**
		 * {@inheritDoc}
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return specialValue == null ? super.toString().toLowerCase() : this.specialValue;
		}
	} 
	
	// Constants
	/** Constant of serialization */
	private static final long serialVersionUID = 5357979078936882283L;
	
	/** JS Hotkey resource */
	private static final JavascriptResourceReference JS_REF = 
		new JavascriptResourceReference(HotkeysBehavior.class, "jquery.hotkeys.min.js");
	
	/** Javascript resource */
	private static final JavascriptResourceReference PLUGIN_REF = 
		new JavascriptResourceReference(HotkeysBehavior.class, "HotkeysBehavior.min.js");
	
	// Properties
	private final JsScopeHotKeys scope;
	private final StringBuffer shortcut;
	private final int idx;
	
	/**
	 * Constructor
	 * @param scope
	 */
	public HotkeysBehavior(JsScopeHotKeys scope, KeysEnum key, CombinaisonKeysEnum combinaison) {
		super();
		
		if(scope == null){
			throw new WicketRuntimeException("scope cannot be null");
			
		} else if(key == null){
			throw new WicketRuntimeException("key cannot be null");
		}
		
		this.scope = scope;
		this.idx = Session.get().nextSequenceValue();
		shortcut = new StringBuffer();
		shortcut.append("'");
		
		if(combinaison != null){
			shortcut.append(combinaison.toString() + "+");
		}
		
		shortcut.append(key.toString() + "'");
	}

	/**
	 * {@inheritDoc}
	 * @see org.odlabs.wiquery.core.behavior.WiQueryAbstractBehavior#contribute(org.odlabs.wiquery.core.commons.WiQueryResourceManager)
	 */
	@Override
	public void contribute(WiQueryResourceManager wiQueryResourceManager) {
		super.contribute(wiQueryResourceManager);
		wiQueryResourceManager.addJavaScriptResource(JS_REF);
		wiQueryResourceManager.addJavaScriptResource(PLUGIN_REF);
	}

	/**
	 * {@inheritDoc}
	 * @see org.odlabs.wiquery.core.behavior.WiQueryAbstractBehavior#statement()
	 */
	@Override
	public JsStatement statement() {
		Component cmp = getComponent();
		return (cmp instanceof Page ? new JsStatement().document() : new JsQuery(cmp).$()).chain(
				"hotkeysBind", Integer.toString(idx), shortcut, scope.render());
	}
}
