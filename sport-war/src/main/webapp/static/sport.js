var Browser = {
	Engine: {name: 'unknown', version: ''},
	Platform: {name: (navigator.platform.match(/mac|win|linux/i) || ['other'])[0].toLowerCase()},
	Features: {xpath: !!(document.evaluate), air: !!(window.runtime)}
};

if (window.opera) Browser.Engine = {name: 'presto', version: (document.getElementsByClassName) ? 950 : 925};
else if (window.ActiveXObject) Browser.Engine = {name: 'trident', version: (window.XMLHttpRequest) ? 5 : 4};
else if (!navigator.taintEnabled) Browser.Engine = {name: 'webkit', version: (Browser.Features.xpath) ? 420 : 419};
else if (document.getBoxObjectFor != null) Browser.Engine = {name: 'gecko', version: (document.getElementsByClassName) ? 19 : 18};
Browser.Engine[Browser.Engine.name] = Browser.Engine[Browser.Engine.name + Browser.Engine.version] = true;

Element.addMethods({
    getComputedStyle: function(elt, property){
		if (elt.currentStyle) return elt.currentStyle[property.camelCase()];
        var win = elt.ownerDocument.defaultView || elt.ownerDocument.parentWindow;
		var computed = win.getComputedStyle(elt, null);
		return (computed) ? computed.getPropertyValue([property.dasherize()]) : null;
	},

    getScrolls: function(elt){
		var element = elt, position = {x: 0, y: 0};
		while (element && !isBody(element)){
			position.x += element.scrollLeft;
			position.y += element.scrollTop;
			element = element.parentNode;
		}
		return position;
	},


    getOffsets: function(elt){
		var element =elt, position = {x: 0, y: 0};
		if (isBody(elt)) return position;

		while (element && !isBody(element)){
			position.x += element.offsetLeft;
			position.y += element.offsetTop;

			if (Browser.Engine.gecko){
				if (!borderBox(element)){
					position.x += leftBorder(element);
					position.y += topBorder(element);
				}
				var parent = element.parentNode;
				if (parent && styleString(parent, 'overflow') != 'visible'){
					position.x += leftBorder(parent);
					position.y += topBorder(parent);
				}
			} else if (element != elt && (Browser.Engine.trident || Browser.Engine.webkit)){
				position.x += leftBorder(element);
				position.y += topBorder(element);
			}

			element = element.offsetParent;
			if (Browser.Engine.trident){
				while (element && !element.currentStyle.hasLayout) element = element.offsetParent;
			}
		}
		if (Browser.Engine.gecko && !borderBox(elt)){
			position.x -= leftBorder(elt);
			position.y -= topBorder(elt);
		}
		return position;
	},

    getPosition: function(elt, relative){
		if (isBody(elt)) return {x: 0, y: 0};
		var offset = elt.getOffsets(), scroll = elt.getScrolls();
		var position = {x: offset.x - scroll.x, y: offset.y - scroll.y};
		var relativePosition = (relative && (relative = $(relative))) ? relative.getPosition() : {x: 0, y: 0};
		return {x: position.x - relativePosition.x, y: position.y - relativePosition.y};
	},

    getCoordinates: function(elt, relative){
		if (isBody(elt)) {
            var win = elt.ownerDocument.defaultView || elt.ownerDocument.parentWindow;
            return win.getCoordinates();
        }
		var position = elt.getPosition(relative), size = {x: elt.offsetWidth, y: elt.offsetHeight};
		var obj = {left: position.x, top: position.y, width: size.x, height: size.y};
		obj.right = obj.left + obj.width;
		obj.bottom = obj.top + obj.height;
		return obj;
	},

    getInnerText: function(element) {
        element = $(element);
        return element.innerText && !window.opera ? element.innerText
            : element.innerHTML.stripScripts().unescapeHTML().replace(/[\n\r\s]+/g, ' ');
    }

});

var styleString = Element.getComputedStyle;

function styleNumber(element, style){
	return parseInt(styleString(element, style), 10) || 0;
};

function borderBox(element){
	return styleString(element, '-moz-box-sizing') == 'border-box';
};

function topBorder(element){
	return styleNumber(element, 'border-top-width');
};

function leftBorder(element){
	return styleNumber(element, 'border-left-width');
};

function isBody(element){
	return (/^(?:body|html)$/i).test(element.tagName);
};

function openParticipantsListEditor(buttonList, content) {
    $(document.body).addClassName('editingParticipantsList');
    $(document.body).removeClassName('notEditingParticipantsList');
    $('participant_input').value = '';
    $('participant_input').focus();
    var coords = $('participant_input').getCoordinates();
    delete coords.right;
    delete coords.bottom;
    $('participant_choices').setStyle(coords);
}

function closeParticipantsListEditor(buttonList, content) {
    $(document.body).removeClassName('editingParticipantsList');
    $(document.body).addClassName('notEditingParticipantsList');
}

function removeParticipant(element, userId, workoutId) {
    new Ajax.Request('/workout/removeParticipant', {
        'method': 'post',
        'parameters': {
            'participantId': userId,
            'id': workoutId
        }
    });
    var links = element.select('a');
    for (var i=0; i<links.length; ++i) {
        links[i].observe('click', function(evt) { Event.stop(evt); });
    }
    element.fade({'duration': 0.4});
}

function addParticipant(userName, userId, workoutId, destination) {
    new Ajax.Request('/workout/addParticipant', {
        'method': 'post',
        'parameters': {
            'participantId': userId,
            'id': workoutId
        }
    });
    var block = new Element('li');
    var link = new Element('a', {'href':'/bib/?id='+userId, 'title':'Voir le dossard de '+userName})
        .insert(userName);
    var remover = new Element('a', {'class': 'remover', 'title':'Supprimer de la liste', 'href':'#'})
        .insert('Supprimer');
    remover.observe('click', function(evt) {
        removeParticipant(block, userId, workoutId);
        Event.stop(evt);
    });
    link.insert(name);
    block.insert(link);
    block.insert(remover);
    block.setOpacity(0);
    var elements = destination.select('.userList li');
    for (var i=0; i<elements.length; ++i) {
        if (elements[i].select('a')[0].getInnerText().toLowerCase() > userName.toLowerCase()) {
            elements[i].insert({'before': block});
            break;
        } else if (i == elements.length - 1) {
            elements[i].insert({'after': block});
        }
    }
    block.appear({'duration': 0.4});
}

var ParticipantAutocompleter = Class.create(Ajax.Autocompleter, {

    markPrevious: function($super) {
        if (this.index > 0) this.index--;
        else this.index = this.entryCount-1;
        this.showCurrentEntry();
        this.element.value = this.getEntry(this.index).select('span.name')[0].getInnerText();
    },

    showCurrentEntry: function() {
        var entry = this.getEntry(this.index);
        if (entry.offsetTop < this.update.scrollTop) {
            this.update.scrollTop = entry.offsetTop;
        } else if (entry.offsetTop + entry.getHeight() > this.update.scrollTop + this.update.getHeight()) {
            this.update.scrollTop = entry.offsetTop - this.update.getHeight() + entry.getHeight();
        }
    },

    markNext : function($super) {
        if (this.index < this.entryCount-1) this.index++;
        else this.index = 0;
        this.showCurrentEntry();
        this.element.value = this.getEntry(this.index).select('span.name')[0].getInnerText();
    }
});

function installParticipantsListEditor() {
    if (!Trainoo.isLogged || !Trainoo.isWorkout) return;
    var button = $('editParticipantsList');
    var content = $('participantsList');
    if (button && content) {
        closeParticipantsListEditor(button.up(), content);
        button.href = '#';
        button.observe('click', function(evt) {
            openParticipantsListEditor(button.up(), content);
            Event.stop(evt);
        });
        var applyButton =
            new Element('a', {'href':'#', 'title': 'Terminer', 'class':'button applyButton verboseButton'})
                .insert('Terminer');
        applyButton.observe('click', function(evt) {
            closeParticipantsListEditor(button.up(), content);
            Event.stop(evt);
        });
        button.insert({'after': applyButton});
        $(document.body).insert({'bottom':
            new Element('div', {'id': 'participant_choices', 'class': 'autocomplete'})});
        content.insert({'top':
            new Element('input', {'class': 'text', 'id': 'participant_input'})});
        new ParticipantAutocompleter('participant_input', 'participant_choices', '/feedback',
            {paramName: 'data', minChars: 1, parameters: 'type=participants&workout='+Trainoo.workout.id, updateElement: function(elt) {
                var userName = elt.select('span.name')[0].getInnerText().strip();
                var userId = parseInt(elt.select('span.id')[0].getInnerText().strip(), 10);
                addParticipant(userName, userId, Trainoo.workout.id, content);
                $('participant_input').value = '';
            }});
        elements = content.select('.userList li');
        for (var i=0; i<elements.length; ++i) {
            (function (current) {
                var currentId = parseInt(current.select('a')[0].href.split('id=')[1].split('&')[0], 10);
                if (currentId != Trainoo.user.id) {
                    var remover = new Element('a', {'class': 'remover', 'title':'Supprimer de la liste', 'href':'#'})
                        .insert('Supprimer');
                    remover.observe('click', function(evt) {
                        removeParticipant(current, currentId, Trainoo.workout.id);
                        Event.stop(evt);
                    });
                    current.insert({'bottom': remover});
                }
            })(elements[i]);
        }
    }
}

document.observe("dom:loaded", installParticipantsListEditor);

function fixEditViewUnderIE() {
    if (Browser.Engine.trident) {
        $$('.workoutBlock dl, .bibBlock dl').each(
            function (elem) {
                elem.innerHTML += ' ';
            }
        );
    }
}

document.observe("dom:loaded", fixEditViewUnderIE);

var oldValue = ''
function feedback(field_name, val)
{
    var feedbackElement = 'feedback';
    if (val.length != 0) {
        if (oldValue != val + field_name) {
            new Ajax.Updater(feedbackElement, '/feedback',
            {asynchronous:true, evalScripts:true,
                parameters:'data=' + val + '&type=' + field_name}).updateContent();
        }
    } else {
        Element.update(feedbackElement, '');
    }
    oldValue = val + field_name;
    return true;
}
function showWorkoutToolTip(event, tip, fieldName, val) {
    var info = '<div id="info" style="clear:left;"><span id="tip" style="height:1em;">&nbsp;</span><br><span class="feedback" id="feedback">&nbsp;</span></div>';
    showToolTip(event, info);
    $('tip').update(tip);
    feedback(fieldName, val);
}
function clickableRow(row) {
    var workoutLink = $('a_' + row.id.substring(3));
    var href = workoutLink.href;
    var childValue = workoutLink.firstChild.nodeValue;
    Element.update(workoutLink.parentNode, childValue);
    row.observe('mouseover', function() {
        row.addClassName('current');
        window.status = href;
    });
    row.observe('mouseout', function() {
        window.status = "";
        row.removeClassName('current');
    });
    row.observe('click', function() {
        window.location = href;
    });
}
/* MIT licence bablaba */
function charCounter(id, maxlimit, minLimit) {
    var counterId = 'counter-' + id;
    if (!$(counterId)) {
        $(id).insert({after: '<span class="charcount"><span id="' + counterId + '"></span><a id="a-' + counterId + '" class="tooltipRaiser">?</a></span>'});
        $('a-' + counterId).observe("mouseover", function(event) {
            $('a-' + counterId).observe("mouseout", function() {
                hideToolTip();
            });
            showToolTip(event, "Limites du nombre de caractères&nbsp;:<br><b>minimum / actuel / maximum</b>");
        });
    }
    if ($F(id).length >= maxlimit || minLimit != null && minLimit > $F(id).length && $F(id).length > 0) {
        $(id).value = $F(id).substring(0, maxlimit);
        $(counterId).addClassName('charcount-limit');
        $(counterId).removeClassName('charcount-safe');
    } else {
        $(counterId).removeClassName('charcount-limit');
        $(counterId).addClassName('charcount-safe');
    }
    $(counterId).update((minLimit != null ? minLimit + '/' : '' ) + $F(id).length + '/' + maxlimit);
}
function makeItCount(id, maxsize, minsize) {
    if ($(id)) {
        Event.observe($(id), 'keyup', function() {
            charCounter(id, maxsize, minsize);
        }, false);
        Event.observe($(id), 'keydown', function() {
            charCounter(id, maxsize, minsize);
        }, false);
        charCounter(id, maxsize, minsize);
    }
}
/* end MIT licence blabla*/

/************************************************************************************************************
 Ajax dynamic list
 Copyright (C) September 2005  DTHMLGoodies.com, Alf Magne Kalleland

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA

 Dhtmlgoodies.com., hereby disclaims all copyright interest in this script
 written by Alf Magne Kalleland.

 Alf Magne Kalleland, 2006
 Owner of DHTMLgoodies.com

 ************************************************************************************************************/
function showToolTip(e, content) {
    if ($('bubble_tooltip') == null) {
        var string = '<div id="bubble_tooltip"><div class="bubble_top">&nbsp;</div><div class="bubble_middle"><div id="bubble_tooltip_content"></div></div><div class="bubble_bottom"></div></div>';
        $('body').insert(string);
    }
    if (document.all)
        e = event;
    $('bubble_tooltip_content').update(content);
    var tooltip = $('bubble_tooltip');
    tooltip.setStyle({visibility: 'visible'});
    var element = Event.element(e);
    var absoluteY = 0;
    var absoluteX = element.offsetWidth * 0.75;
    while (element != null) {
        absoluteY += element.offsetTop;
        absoluteX += element.offsetLeft;
        element = element.offsetParent;
    }
    var leftPos = absoluteX - tooltip.offsetWidth / 2;
    if (leftPos < 0)leftPos = 0;
    tooltip.setStyle({left: leftPos + 'px'});
    tooltip.setStyle({top: absoluteY - tooltip.offsetHeight + 'px'});
}
function hideToolTip()
{
    document.getElementById('bubble_tooltip').setStyle({visibility: 'hidden'});
    return true;
}
/***** END LGPL ****/
