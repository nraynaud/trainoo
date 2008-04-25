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
function startMap() {
    if (GBrowserIsCompatible()) {
        G_SATELLITE_MAP.getName = function() {
            return 'google';
        }
        map = new GMap2($("map"), {draggableCursor: 'crosshair', googleBarOptions:{showOnLoad:true}});
        map.addMapType(IGN_MAP_TYPE);
        map.addMapType(IGN_PHOTO_TYPE);
        map.removeMapType(G_NORMAL_MAP);
        map.setCenter(new GLatLng(47.081850, 2.3995035), 6);
        map.enableScrollWheelZoom();
        var mapTypeControl = new GHierarchicalMapTypeControl();
        mapTypeControl.addRelationship(IGN_PHOTO_TYPE, IGN_MAP_TYPE, "carte");
        map.addControl(mapTypeControl);
        map.enableContinuousZoom();
        map.addControl(new GLargeMapControl());
        map.addControl(new GScaleControl(), new GControlPosition(G_ANCHOR_BOTTOM_RIGHT, new GSize(10, 15)));
    }
}