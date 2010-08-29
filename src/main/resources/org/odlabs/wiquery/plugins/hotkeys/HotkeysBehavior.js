/*
 * Copyright (c) 2010 WiQuery team
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

(function ($){
	
	/**
	 * Method to bind a shortcut
	 */
	$.fn.hotkeysBind = function(idx, shortcut, scope) {
		var name = "hotkeys_scope_" + idx, self = this, element = this.get(0);
		this.hotkeysUnbind(idx, shortcut, scope);
		
		$.data(element, name, function(event){ 
			window.setTimeout(function() {
				if(self === undefined || self.get(0) === undefined){
					// unbind the shortcut because the element doesn't exist
					self.hotkeysUnbind(idx, shortcut, scope);
					return false;
				}
				
				scope(event);
			}, 0);
			
			event.cancelBubble = true;
			event.returnValue = false;
			
			if (event.stopPropagation) {
				event.stopPropagation();
				event.preventDefault();
			}
			
			return false; 
		});
		$(document).bind("keydown", shortcut, $.data(element, name));
	}
	
	/**
	 * Method to unbind a shortcut
	 */
	$.fn.hotkeysUnbind = function(idx, shortcut, scope) {
		var name = "hotkeys_scope_" + idx, element = this.get(0);
		
		if($.data(element, name)) {
			$(document).unbind("keydown", shortcut, $.data(element, name));
			$.removeData(element, name);
		}
	}
	
})(jQuery);
