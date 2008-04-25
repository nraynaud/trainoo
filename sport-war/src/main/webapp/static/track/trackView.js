var distance = 0;
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
    point.getPoint = function() {
        return point;
    }
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
        bounds.extend(m.getPoint())
    });
    this.map.setCenter(bounds.getCenter(), this.map.getBoundsZoomLevel(bounds));
}
Editor.prototype.clearMap = function() {
    this.markers = [];
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
    this.fit();
};