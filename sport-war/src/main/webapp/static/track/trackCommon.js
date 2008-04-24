_mPreferMetric = true;
function log(txt) {
    if ("console" in window && "firebug" in console)
        console.log(txt);
}
function updateHeight() {
    $('content').style.height = $('center').clientHeight - $('content').offsetTop + "px";
}
function loaded() {
    updateHeight();
    startMap();
}
window.onresize = updateHeight;
window.onload = function() {
    onLoaded.each(function (f) {
        f();
    });
};
function loadOnStartup(track) {
    onLoaded.push(function() {
        loadTrack(track);
    });
}
function loadTrack(track) {
    editor.loadTrack(eval('[' + track + ']'));
}
var onLoaded = [loaded]
var map;
var editor;