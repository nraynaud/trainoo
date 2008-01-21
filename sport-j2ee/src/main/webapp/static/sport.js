var oldValue = ''
function feedback(field_name, val)
{
    var feedbackElement = field_name + '_feedback';
    if (val.length != 0 && oldValue != val) {
        new Ajax.Updater(feedbackElement, '/feedback',
        {asynchronous:true, evalScripts:true,
            parameters:'data=' + val + '&type=' + field_name}).updateContent();
    } else {
        Element.update(feedbackElement, '');
    }
    oldValue = val;
}
;
