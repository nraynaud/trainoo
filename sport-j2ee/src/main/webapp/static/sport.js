var oldValue = ''
function feedback(field_name, val)
{
    var feedbackElement = field_name + '_feedback';
    if (val.length != 0) {
        if (oldValue != val) {
            new Ajax.Updater(feedbackElement, '/feedback',
            {asynchronous:true, evalScripts:true,
                parameters:'data=' + val + '&type=' + field_name}).updateContent();
        }
    } else {
        Element.update(feedbackElement, '');
    }
    oldValue = val;
}
;


/* MIT licence bablaba */
function charCounter(id, maxlimit) {
    if (!$('counter-' + id)) {
        $(id).insert({after: '<div id="counter-' + id + '"></div>'});
    }
    if ($F(id).length >= maxlimit) {
        $(id).value = $F(id).substring(0, maxlimit);
        $('counter-' + id).addClassName('charcount-limit');
        $('counter-' + id).removeClassName('charcount-safe');
    } else {
        $('counter-' + id).removeClassName('charcount-limit');
        $('counter-' + id).addClassName('charcount-safe');
    }
    $('counter-' + id).update($F(id).length + '/' + maxlimit);

}

function makeItCount(id, maxsize) {
    if ($(id)) {
        Event.observe($(id), 'keyup', function() {
            charCounter(id, maxsize);
        }, false);
        Event.observe($(id), 'keydown', function() {
            charCounter(id, maxsize);
        }, false);
        charCounter(id, maxsize);
    }
}
/* end MIT licence blabla*/
