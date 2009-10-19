_mPreferMetric = true;
function log(txt) {
    if ("console" in window)
        window.console.log(txt);
}
function updateHeight() {
    $('content').style.height = $('contentWrapper').clientHeight + "px";
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
var onLoaded = [loaded];
var map;
var editor;
var mapOptions;
function startMap() {
    if (GBrowserIsCompatible()) {
        map = new GMap2($("map"), mapOptions);
        map.removeMapType(G_NORMAL_MAP);
        map.setCenter(new GLatLng(47.081850, 2.3995035), 6);
        map.enableScrollWheelZoom();
        map.setMapType(G_HYBRID_MAP);
        map.enableContinuousZoom();
        map.addControl(new GLargeMapControl());
        map.addControl(new GScaleControl(), new GControlPosition(G_ANCHOR_BOTTOM_RIGHT, new GSize(10, 15)));
    }
}