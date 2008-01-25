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

/** from http://imperialcode.com/js/pngfix.js */
function fixPNGIE() {
    var arVersion = navigator.appVersion.split("MSIE")
    var version = parseFloat(arVersion[1])

    if ((version >= 5.5) && (document.body.filters))
    {
        for (var i = 0; i < document.images.length; i++)
        {
            var img = document.images[i]
            var imgName = img.src.toUpperCase()
            if (imgName.substring(imgName.length - 3, imgName.length) == "PNG")
            {
                var imgID = (img.id) ? "id='" + img.id + "' " : ""
                var imgClass = (img.className) ? "class='" + img.className + "' " : ""
                var imgTitle = (img.title) ? "title='" + img.title + "' " : "title='" + img.alt + "' "
                var imgStyle = "display:inline-block;" + img.style.cssText
                if (img.align == "left") imgStyle = "float:left;" + imgStyle
                if (img.align == "right") imgStyle = "float:right;" + imgStyle
                if (img.parentElement.href) imgStyle = "cursor:hand;" + imgStyle
                img.outerHTML = "<span " + imgID + imgClass + imgTitle
                        + " style=\"" + "width:" + img.width + "px; height:" + img.height + "px;" + imgStyle + ";"
                        + "filter:progid:DXImageTransform.Microsoft.AlphaImageLoader"
                        + "(src=\'" + img.src + "\', sizingMethod='scale');\"></span>"
                i = i - 1
            }
        }
    }
}

/* end from */