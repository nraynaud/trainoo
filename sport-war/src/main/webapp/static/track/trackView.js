var distance = 0;
var startMarker;
var endMarker;
mapOptions = {googleBarOptions:{showOnLoad:true}};
onLoaded.push(myStart);
function myStart() {
    editor = new Editor(map);
    map.setMapType(IGN_MAP_TYPE);
}
function newTrack() {
    editor.clearMap();
}
function Editor(map) {
    this.map = map;
    this.markers = [];
    this.line = null;
}
Editor.prototype.addMarker = function(point) {
    if (this.markers.length > 0)
        distance += this.markers[this.markers.length - 1].distanceFrom(point);
    this.markers.push(point);
}
Editor.prototype.draw = function() {
    if (this.line) {
        map.removeOverlay(this.line);
    }
    this.line = new GPolyline(this.markers, 'blue', 3, 1);
    map.addOverlay(this.line);
}
Editor.prototype.fit = function() {
    var bounds = new GLatLngBounds();
    this.markers.each(function(m) {
        bounds.extend(m)
    });
    this.map.setCenter(bounds.getCenter(), this.map.getBoundsZoomLevel(bounds));
}
Editor.prototype.clearMap = function() {
    this.markers = [];
    if (startMarker)
        map.removeOverlay(startMarker);
    if (endMarker)
        map.removeOverlay(endMarker);
    this.draw()
}
Editor.prototype.loadTrack = function (track) {
    this.clearMap();
    var editor = this;
    track.each(function(point) {
        editor.addMarker(new GLatLng(point[0], point[1]));
    });
    $('distance').update((distance / 1000).toFixed(2) + "km");
    this.draw();
    if (this.markers.length > 0) {
        startMarker = new GMarker(this.markers[0], {clickable: false});
        map.addOverlay(startMarker);
        startMarker.setImage("/static/track/green_MarkerA.png");
        endMarker = new GMarker(this.markers[this.markers.length - 1], {clickable: false});
        map.addOverlay(endMarker);
        endMarker.setImage("/static/track/red_MarkerB.png");
    }
    this.fit();
};