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
