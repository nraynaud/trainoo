_mPreferMetric = true;
function log(txt) {
    if ("console" in window && "firebug" in console)
        console.log(txt);
}
function updateHeight() {
    $('content').style.height = $('center').clientHeight - $('content').offsetTop - 10 + "px";
}
function loaded() {
    updateHeight();
    startMap();
}
window.onresize = updateHeight;
window.onload = loaded;
var map;
var editor;
var icon;
function loadTrack(track) {
    editor.loadTrack(eval('[' + track + ']'));
}
function createIcon() {
    var icon = new GIcon();
    icon.image = "http://labs.google.com/ridefinder/images/mm_20_red.png";
    icon.shadow = "http://labs.google.com/ridefinder/images/mm_20_shadow.png";
    icon.iconSize = new GSize(12, 20);
    icon.shadowSize = new GSize(22, 20);
    icon.iconAnchor = new GPoint(6, 20);
    return icon;
}
function createHandleIcon() {
    var icon = new GIcon();
    icon.image = $('map_handle').src;
    icon.iconSize = new GSize(15, 15);
    icon.iconAnchor = new GPoint(8, 8);
    return icon;
}
function startMap() {
    if (GBrowserIsCompatible()) {
        var IGN_PHOTO_TYPE = createGeoMapType(IGN_PHOTO_KEY, 'white', 18);
        var IGN_MAP_TYPE = createGeoMapType(IGN_MAP_KEY, 'black', 16);
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
        icon = createIcon();
        editor = new Editor(map);
        editor.canAppendPoint(true);
        editor.draw();
        new GDraggableObject($('controlPanel'));
        map.setMapType(IGN_PHOTO_TYPE);
    }
}
function newTrack() {
    editor.clearMap();
}
function Editor(map) {
    this.map = map;
    var editor = this
    this.addPointCallback = function(overlay, latLng) {
        editor.addMarker(latLng);
        editor.draw();
    };
    this.insertionEditor = new PointInsertionEditor(map, this);
    this.insertionEditor.canInsertPoint(true);
    this.markers = [];
    this.line = null;
}
Editor.prototype.canAppendPoint = function(can) {
    if (can)
        this.canAppendPointHandler = GEvent.addListener(map, 'click', this.addPointCallback);
    else
        GEvent.removeListener(this.canAppendPointHandler);
}
Editor.prototype.canDragMarker = function(can) {
    this.canDragMarker = can;
}
Editor.prototype.startInsertion = function() {
    muteMarkers(this.markers);
}
Editor.prototype.endInsertion = function() {
    vocalMarkers(this.markers, this);
}
Editor.prototype.startMovingMarker = function() {
    muteMouse(this.markers);
}
Editor.prototype.endMovingMarker = function() {
    vocalMouse(this.markers, this);
}
function muteMarkers(markers) {
    markers.each(function(m) {
        GEvent.removeListener(m.dragHandler);
        GEvent.removeListener(m.dragStartHandler);
        GEvent.removeListener(m.dragEndHandler);
        GEvent.removeListener(m.mouseOverHandler);
        GEvent.removeListener(m.mouseOutHandler);
    });
}
function muteMouse(markers) {
    markers.each(function(m) {
        GEvent.removeListener(m.mouseOverHandler);
        GEvent.removeListener(m.mouseOutHandler);
    });
}
function vocalMarkers(markers, editor) {
    markers.each(function (m) {
        registerEvents(m, editor);
    });
}
function vocalMouse(markers, editor) {
    markers.each(function (m) {
        registerMouseEvents(m, editor);
    });
}
function registerMouseEvents(marker, editor) {
    marker.mouseOverHandler = GEvent.addListener(marker, "mouseover", function() {
        editor.insertionEditor.canInsertPoint(false);
    });
    marker.mouseOutHandler = GEvent.addListener(marker, "mouseout", function() {
        editor.insertionEditor.canInsertPoint(true);
    });
}
function registerEvents(marker, editor) {
    marker.dragHandler = GEvent.addListener(marker, "drag", function() {
        editor.draw();
    });
    marker.dragStartHandler = GEvent.addListener(marker, "dragstart", function() {
        editor.startMovingMarker();
    });
    marker.dragEndHandler = GEvent.addListener(marker, "dragend", function() {
        editor.endMovingMarker();
    });
    registerMouseEvents(marker, editor);
}
Editor.prototype.addMarker = function(point, index) {
    log('lol le point ' + point);
    map.panTo(point);
    var marker = new GMarker(point, {draggable: true});
    if (index == null)
        this.markers.push(marker);
    else
        this.markers.splice(index, 0, marker)
    map.addOverlay(marker);
    marker.enableDragging();
    registerEvents(marker, this);
}
Editor.prototype.draw = function() {
    if (this.line) {
        map.removeOverlay(this.line);
    }
    var poly = [];
    var encodedTrack = "";
    var distance = 0
    for (var i = 0; i < this.markers.length; ++i) {
        var pnt = this.markers[i].getPoint();
        if (poly.length > 0)
            distance += poly[poly.length - 1].distanceFrom(pnt);
        poly.push(pnt);
        encodedTrack += '[' + pnt.lat() + ',' + pnt.lng() + '],';
    }
    this.line = new GPolyline(poly, 'blue', 3, 1);
    map.addOverlay(this.line);
    $('trackVar').setValue(encodedTrack);
    $('distance').update((distance / 1000).toFixed(2) + "km");
    $('lengthVar').setValue(distance / 1000);
    $('pointsCount').update(this.markers.length);
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
function PointInsertionEditor(map, editor) {
    this.myMarker = new GMarker(new GLatLng(47.081850, 2.3995035), {
        icon: createHandleIcon(), title:"insérer un point", draggable: true});
    map.addOverlay(this.myMarker);
    this.myMarker.hide();
    var markerIndex = null;
    this.transientPath = null;
    var insertionEditor = this;
    this.insertLinePointHandleCallback = function(latLng) {
        if (editor.markers.length > 1) {
            function square(x) {
                return Math.pow(x, 2);
            }
            function computeNearestPoint(p, p1, p2) {
                var diffLng = p2.lng() - p1.lng();
                var diffLat = p2.lat() - p1.lat();
                var numerator = ((p.lng() - p1.lng()) * diffLng + (p.lat() - p1.lat()) * diffLat);
                if (numerator <= 0)
                    return null;
                var u = numerator / (square(diffLng) + square(diffLat));
                if (u >= 1)
                    return null;
                var x = p1.lng() + u * diffLng;
                var y = p1.lat() + u * diffLat;
                return (new GLatLng(y, x));
            }
            var distMin = Infinity;
            var pointMin = null;
            for (var i = 0; i < editor.markers.length - 1; i++) {
                var point = computeNearestPoint(latLng, editor.markers[i].getPoint(), editor.markers[i + 1].getPoint());
                if (point != null) {
                    var dist = square(latLng.lng() - point.lng()) + square(latLng.lat() - point.lat());
                    if (dist < distMin) {
                        distMin = dist;
                        pointMin = point;
                        markerIndex = i;
                    }
                }
            }
            if (pointMin != null) {
                var currentPix = map.fromLatLngToDivPixel(latLng);
                var handlePix = map.fromLatLngToDivPixel(pointMin);
                if (square(currentPix.x - handlePix.x) + square(currentPix.y - handlePix.y) < square(30)) {
                    insertionEditor.myMarker.setPoint(pointMin);
                    insertionEditor.myMarker.show();
                    return;
                }
            }
            insertionEditor.myMarker.hide();
        }
    }
    GEvent.addListener(this.myMarker, 'mouseover', function() {
        insertionEditor.myMarker.setImage($('map_handle_active').src);
        editor.canAppendPoint(false);
    });
    GEvent.addListener(this.myMarker, 'mouseout', function() {
        insertionEditor.myMarker.setImage($('map_handle').src);
        editor.canAppendPoint(true);
    });
    GEvent.bind(this.myMarker, 'dragstart', this, function() {
        editor.canAppendPoint(false);
        this.canInsertPoint(false, true);
        editor.startInsertion();
        this.myMarker.show();
        insertionEditor.myMarker.setImage($('map_handle').src);
    });
    GEvent.bind(this.myMarker, 'dragend', this, function() {
        if (insertionEditor.transientPath != null) {
            map.removeOverlay(insertionEditor.transientPath);
            insertionEditor.transientPath = null;
        }
        editor.addMarker(insertionEditor.myMarker.getPoint(), markerIndex + 1);
        editor.draw();
        editor.endInsertion();
        this.canInsertPoint(true);
        editor.canAppendPoint(true);
    });
    GEvent.addListener(this.myMarker, 'drag', function() {
        if (insertionEditor.transientPath != null) {
            map.removeOverlay(insertionEditor.transientPath)
        }
        var poly = [editor.markers[markerIndex].getPoint(),
            insertionEditor.myMarker.getPoint(),
            editor.markers[markerIndex + 1].getPoint()];
        insertionEditor.transientPath = new GPolyline(poly, 'red', 8, 0.8);
        map.addOverlay(insertionEditor.transientPath);
    });
    this.insertLinePointHandleHandler = null;
}
PointInsertionEditor.prototype.canInsertPoint = function(can, keepMarker) {
    if (can) {
        if (this.insertLinePointHandleHandler == null)
            this.insertLinePointHandleHandler = GEvent.addListener(map, 'mousemove', this.insertLinePointHandleCallback);
    } else {
        if (!keepMarker)
            this.myMarker.hide();
        if (this.insertLinePointHandleHandler != null) {
            GEvent.removeListener(this.insertLinePointHandleHandler);
            this.insertLinePointHandleHandler = null;
        }
    }
}
