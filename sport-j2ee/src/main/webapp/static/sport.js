function feedback(field_name, val)
{
    if (val.length != 0) {
        new Ajax.Updater(field_name + '_feedback', '/feedback',
        {asynchronous:true, evalScripts:true,
            parameters:'data=' + val + '&type=' + field_name}).updateContent();
    }
};
