_mPreferMetric = true;
var DELETE_BUTTON = "<div id='deleteButton' title='effacer le point'>X</div>"
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
var onLoaded = [loaded]
var map;
var editor;
var MARKER_ICON = createMarkerIcon();
function loadOnStartup(track) {
    onLoaded.push(function() {
        loadTrack(track);
    });
}
function loadTrack(track) {
    editor.loadTrack(eval('[' + track + ']'));
}
function createHandleIcon() {
    var icon = new GIcon();
    icon.image = $('map_handle').src;
    icon.iconSize = new GSize(15, 15);
    icon.iconAnchor = new GPoint(8, 8);
    return icon;
}
function createMarkerIcon() {
    var icon = new GIcon();
    icon.image = $('map_marker').src;
    icon.iconSize = new GSize(15, 15);
    icon.iconAnchor = new GPoint(8, 8);
    return icon;
}
function startMap() {
    if (GBrowserIsCompatible()) {
        G_SATELLITE_MAP.getName = function() {
            return 'google';
        }
        map = new GMap2($("map"), {googleBarOptions:{showOnLoad:true}});
        map.addMapType(IGN_MAP_TYPE);
        map.addMapType(IGN_PHOTO_TYPE);
        map.removeMapType(G_NORMAL_MAP);
        map.setCenter(new GLatLng(47.081850, 2.3995035), 10);
        map.enableScrollWheelZoom();
        var mapTypeControl = new GHierarchicalMapTypeControl();
        mapTypeControl.addRelationship(IGN_PHOTO_TYPE, IGN_MAP_TYPE, "carte");
        map.addControl(mapTypeControl);
        map.enableContinuousZoom();
        map.enableGoogleBar();
        map.addControl(new GLargeMapControl());
        map.addControl(new GScaleControl(), new GControlPosition(G_ANCHOR_BOTTOM_RIGHT, new GSize(10, 15)));
        editor = new Editor(map);
        map.setMapType(IGN_PHOTO_TYPE);
    }
}
function newTrack() {
    editor.clearMap();
}
function Editor(map) {
    this.map = map;
    this.markers = [];
    this.line = null;
}
function renumberMarkers(markers) {
    for (var i = 0; i < markers.length; i++) {
        markers[i].index = i;
    }
}
Editor.prototype.addMarker = function(point, index) {
    var marker = new GMarker(point, {clickable: false});
    if (index == null) {
        this.markers.push(marker);
        marker.index = this.markers.length - 1;
    }
    else {
        this.markers.splice(index, 0, marker);
        renumberMarkers(this.markers);
    }
}
Editor.prototype.draw = function() {
    if (this.line) {
        map.removeOverlay(this.line);
    }
    var poly = [];
    var distance = 0
    for (var i = 0; i < this.markers.length; ++i) {
        var pnt = this.markers[i].getPoint();
        if (poly.length > 0)
            distance += poly[poly.length - 1].distanceFrom(pnt);
        poly.push(pnt);
    }
    this.line = new GPolyline(poly, 'blue', 3, 1);
    $('distance').update((distance / 1000).toFixed(2) + "km");
    map.addOverlay(this.line);
}
Editor.prototype.fit = function() {
    var bounds = new GLatLngBounds();
    this.markers.each(function(m) {
        bounds.extend(m.getPoint())
    });
    this.map.setCenter(bounds.getCenter(), this.map.getBoundsZoomLevel(bounds));
}
Editor.prototype.clearMap = function() {
    this.markers.each(function(m) {
        map.removeOverlay(m);
    });
    this.markers = [];
    this.draw()
}
Editor.prototype.loadTrack = function (track) {
    this.clearMap();
    var editor = this;
    track.each(function(point) {
        editor.addMarker(new GLatLng(point[0], point[1]));
    });
    this.draw();
    this.fit();
};