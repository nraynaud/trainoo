var SHIFT_KEY = 16;
var SHIFT_KEY_TIP_MESSAGE = $('tip').innerHTML;
var MARKER_ICON = createMarkerIcon();
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
var mapOptions = {draggableCursor: 'crosshair', googleBarOptions:{showOnLoad:true}};
onLoaded.push(myStart);
function myStart() {
    editor = new Editor(map);
    map.enableGoogleBar();
}
function newTrack() {
    editor.clearMap();
}
function Editor(map) {
    this.map = map;
    var editor = this;
    function isKey(event, keyCode) {
        var key = event.which || event.keyCode;
        return key = keyCode;
    }
    this.updateModeCallback = function(event) {
        if (isKey(event, SHIFT_KEY))
            editor.toUpdateMode();
    };
    this.insertionModeKeyCallback = function(event) {
        if (isKey(event, SHIFT_KEY)) {
            editor.toAppendMode();
        }
    };
    this.insertionModeBlurCallback = function() {
        editor.toAppendMode();
    };
    GEvent.addListener(map, 'mouseout', function() {
        editor.hideTransientPath();
    });
    this.markers = [];
    this.line = null;
    this.transientPath = null;
    this.toAppendMode();
    this.draw();
}
Editor.prototype.toUpdateMode = function() {
    document.stopObserving('keydown', this.updateModeCallback);
    document.observe('keyup', this.insertionModeKeyCallback);
    document.body.observe('blur', this.insertionModeBlurCallback);
    this.unInstallSubEditor();
    this.installSubEditor(new UpdateEditor(this));
    log("update");
};
Editor.prototype.toAppendMode = function() {
    document.body.stopObserving('blur', this.insertionModeBlurCallback);
    document.stopObserving('keyup', this.insertionModeKeyCallback);
    document.observe('keydown', this.updateModeCallback);
    this.unInstallSubEditor();
    this.installSubEditor(new AppendEditor(this));
    log("append");
};
Editor.prototype.installSubEditor = function(subEditor) {
    this.subEditor = subEditor;
    this.subEditor.install();
};
Editor.prototype.unInstallSubEditor = function() {
    if (this.subEditor != null)
        this.subEditor.unInstall();
};
function renumberMarkers(markers) {
    for (var i = 0; i < markers.length; i++) {
        markers[i].index = i;
    }
}
Editor.prototype.addMarker = function(point, index) {
    var marker = {};
    marker.marker = new GMarker(point, {draggable: true, icon: MARKER_ICON, title: "tirez sur le point pour le déplacer"});
    marker.getPoint = function() {
        return marker.marker.getPoint();
    };
    if (index == null) {
        this.markers.push(marker);
        marker.index = this.markers.length - 1;
    }
    else {
        this.markers.splice(index, 0, marker);
        renumberMarkers(this.markers);
    }
};
Editor.prototype.deleteMarker = function (marker) {
    this.markers.splice(marker.index, 1);
    this.map.removeOverlay(marker.marker);
    this.draw();
    renumberMarkers(this.markers);
};
function setValue(id, value) {
    var element = $(id);
    if (element)
        element.setValue(value);
}
function update(id, content) {
    var element = $(id);
    if (element)
        element.update(content);
}
Editor.prototype.draw = function() {
    if (this.line) {
        map.removeOverlay(this.line);
    }
    var poly = [];
    var encodedTrack = "";
    var distance = 0;
    for (var i = 0; i < this.markers.length; ++i) {
        var pnt = this.markers[i].getPoint();
        if (poly.length > 0)
            distance += poly[poly.length - 1].distanceFrom(pnt);
        poly.push(pnt);
        if (i > 0)
            encodedTrack += ',';
        encodedTrack += '[' + pnt.lat() + ',' + pnt.lng() + ']';
    }
    this.line = new GPolyline(poly, 'blue', 3, 1, {clickable: false});
    map.addOverlay(this.line);
    setValue('trackVar', encodedTrack);
    update('distance', (distance / 1000).toFixed(2) + "km");
    setValue('lengthVar', distance / 1000);
    var tip = $('tip');

    function updateTip(value) {
        if (tip.innerHTML != value)
            tip.update(value);
    }

    if (this.markers.length > 0) {
        updateTip(SHIFT_KEY_TIP_MESSAGE);
    } else {
        updateTip('Cliquez pour insérer votre point de départ');
    }
    tip.show();
};
Editor.prototype.fit = function() {
    var bounds = new GLatLngBounds();
    this.markers.each(function(m) {
        bounds.extend(m.getPoint());
    });
    this.map.setCenter(bounds.getCenter(), this.map.getBoundsZoomLevel(bounds));
};
Editor.prototype.clearMap = function() {
    this.markers.each(function(m) {
        map.removeOverlay(m.marker);
    });
    this.markers = [];
    this.draw();
};
Editor.prototype.loadTrack = function (track) {
    this.clearMap();
    var editor = this;
    track.each(function(point) {
        editor.addMarker(new GLatLng(point[0], point[1]));
    });
    this.draw();
    this.fit();
};
Editor.prototype.hideTransientPath = function () {
    if (this.transientPath != null) {
        map.removeOverlay(this.transientPath);
        this.transientPath = null;
    }
};
Editor.prototype.setTransientPath = function (path) {
    this.hideTransientPath();
    this.transientPath = new GPolyline(path, '#D97900', 8, 0.8, {clickable: false});
    map.addOverlay(this.transientPath);
};
