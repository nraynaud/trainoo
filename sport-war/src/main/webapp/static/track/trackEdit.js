var DELETE_BUTTON = "<div id='deleteButton' title='effacer le point'>X</div>";
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
mapOptions = {draggableCursor: 'crosshair', googleBarOptions:{showOnLoad:true}};
onLoaded.push(myStart);
function myStart() {
    editor = new Editor(map);
    editor.canAppendPoint(true);
    map.enableGoogleBar();
}
function newTrack() {
    editor.clearMap();
}
function Editor(map) {
    this.map = map;
    var editor = this;
    this.addPointCallback = function(overlay, latLng) {
        editor.hideTransientPath();
        editor.addMarker(latLng);
        map.panTo(latLng);
        editor.draw();
    }.bind(this);
    GEvent.addListener(map, 'mouseout', function() {
        editor.hideTransientPath();
    });
    this.insertionEditor = new PointInsertionEditor(map, this);
    this.insertionEditor.canInsertPoint(true);
    this.markers = [];
    this.line = null;
    this.transientPath = null;
}
Editor.prototype.canAppendPoint = function(can) {
    this.hideTransientPath();
    if (can)
        this.canAppendPointHandler = GEvent.addListener(map, 'click', this.addPointCallback);
    else
        GEvent.removeListener(this.canAppendPointHandler);
};
Editor.prototype.canDragMarker = function(can) {
    this.canDragMarker = can;
};
Editor.prototype.startInsertion = function() {
    muteMarkers(this.markers);
};
Editor.prototype.endInsertion = function() {
    vocalMarkers(this.markers, this);
};
Editor.prototype.startMovingMarker = function() {
    muteMouse(this.markers);
};
Editor.prototype.endMovingMarker = function() {
    vocalMouse(this.markers, this);
};
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
        registerEvents(markers, m, editor);
    });
}
function vocalMouse(markers, editor) {
    markers.each(function (m) {
        registerMouseEvents(m, editor);
    });
}
function hideDeleteMarkerButton() {
    var del = $('deleteButton');
    del.hide();
}
function registerMouseEvents(marker, editor) {
    marker.mouseOverHandler = GEvent.addListener(marker, "mouseover", function() {
        var del = $('deleteButton');
        if (del == null) {
            $("map").insert(DELETE_BUTTON);
            del = $('deleteButton');
            GEvent.addListener(editor.map, "move", hideDeleteMarkerButton);
            GEvent.addListener(editor.map, "zoomend", hideDeleteMarkerButton);
            GEvent.addDomListener(del, 'mouseover', function() {
                editor.insertionEditor.canInsertPoint(false);
                editor.showMarkeDeletionPreview(del.marker);
            });
            GEvent.addDomListener(del, 'mouseout', function() {
                editor.insertionEditor.canInsertPoint(true);
            });
            GEvent.addDomListener(del, 'click', function() {
                editor.deleteMarker(del.marker);
                del.hide();
            });
        }
        var pixelPoint = editor.map.fromLatLngToContainerPixel(marker.getPoint());
        var icon = marker.getIcon();
        var xOffset = -icon.iconAnchor.x;
        var yOffset = -icon.iconAnchor.y - del.getDimensions().height;
        del.marker = marker;
        del.setStyle({left:pixelPoint.x + xOffset + 'px', top: pixelPoint.y + yOffset + 'px'});
        del.show();
        marker.setImage($('map_marker_active').src);
        editor.insertionEditor.canInsertPoint(false);
        editor.canAppendPoint(false);
    });
    marker.mouseOutHandler = GEvent.addListener(marker, "mouseout", function() {
        editor.canAppendPoint(true);
        editor.insertionEditor.canInsertPoint(true);
        marker.setImage($('map_marker').src);
    });
    marker.mouseClickHandler = GEvent.addListener(marker, "click", function() {
        if (marker.index == 0)
            editor.insertionEditor.insertAtStart();
        else if (marker.index == editor.markers.length - 1)
            editor.insertionEditor.insertAtEnd();
    });
    marker.mouseDoubleClickHandler = GEvent.addListener(marker, "dblclick", function() {
        editor.addMarker(editor.markers[editor.markers.length - 1].getPoint(), 0);
        editor.draw();
    });
}
function registerEvents(markers, marker, editor) {
    marker.dragHandler = GEvent.addListener(marker, "drag", function() {
        var index = marker.index;
        var poly = [marker.getPoint()];
        if (index > 0) {
            poly.unshift(markers[index - 1].getPoint());
        }
        if (index < markers.length - 1) {
            poly.push(markers[index + 1].getPoint());
        }
        editor.setTransientPath(poly);
    });
    marker.dragStartHandler = GEvent.addListener(marker, "dragstart", function() {
        hideDeleteMarkerButton();
        editor.startMovingMarker();
    });
    marker.dragEndHandler = GEvent.addListener(marker, "dragend", function() {
        editor.hideTransientPath();
        editor.draw();
        editor.endMovingMarker();
        marker.setImage($('map_marker').src);
    });
    registerMouseEvents(marker, editor);
}
function renumberMarkers(markers) {
    for (var i = 0; i < markers.length; i++) {
        markers[i].index = i;
    }
}
Editor.prototype.addMarker = function(point, index) {
    var marker = new GMarker(point, {draggable: true, icon: MARKER_ICON, title: "tirez sur le point pour le déplacer"});
    var insertAt = function(index) {
        this.markers.splice(index, 0, marker);
        renumberMarkers(this.markers);
    }.bind(this);
    if (index == null) {
        index = this.insertionEditor.currentInsertionEnd.index();
    }
    insertAt(index);
    marker.enableDragging();
    map.addOverlay(marker);
    registerEvents(this.markers, marker, this);
};
Editor.prototype.deleteMarker = function (marker) {
    this.hideTransientPath();
    this.markers.splice(marker.index, 1);
    this.map.removeOverlay(marker);
    this.draw();
    renumberMarkers(this.markers);
};
Editor.prototype.showMarkeDeletionPreview = function (marker) {
    if (marker.index > 0 && marker.index + 1 < this.markers.length)
        this.setTransientPath([this.markers[marker.index - 1].getPoint(), this.markers[marker.index + 1].getPoint()]);
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
    this.line = new GPolyline(poly, 'blue', 3, 1);
    map.addOverlay(this.line);
    setValue('trackVar', encodedTrack);
    update('distance', (distance / 1000).toFixed(2) + "km");
    setValue('lengthVar', distance / 1000);
    update('pointsCount', this.markers.length);
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
        map.removeOverlay(m);
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
    this.transientPath = new GPolyline(path, '#D97900', 6, 0.6, {clickable: false});
    map.addOverlay(this.transientPath);
};
function PointInsertionEditor(map, editor) {
    this.editor = editor;
    this.transientMarker = new GMarker(new GLatLng(47.081850, 2.3995035), {
        icon: createHandleIcon(), title:"tirez pour insérer un point", draggable: true});
    map.addOverlay(this.transientMarker);
    this.transientMarker.hide();
    this.insertAtEnd();
    var markerIndex = null;
    var insertionEditor = this;
    this.insertLinePointHandleCallback = function(latLng) {
        if (editor.markers.length > 1) {
            function square(x) {
                return Math.pow(x, 2);
            }

            function computeNearestPointOnSegment(p, p1, p2) {
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
                var point = computeNearestPointOnSegment(latLng, editor.markers[i].getPoint(), editor.markers[i + 1].getPoint());
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

                function distanceLessThan(p1, p2, threshold) {
                    return square(p1.x - p2.x) + square(p1.y - p2.y) < square(threshold);
                }

                if (distanceLessThan(currentPix, handlePix, 30)) {
                    var previousPix = map.fromLatLngToDivPixel(editor.markers[markerIndex].getPoint());
                    var nextPix = map.fromLatLngToDivPixel(editor.markers[markerIndex + 1].getPoint());
                    if (!distanceLessThan(handlePix, previousPix, 15) && !distanceLessThan(handlePix, nextPix, 15)) {
                        insertionEditor.transientMarker.setPoint(pointMin);
                        insertionEditor.transientMarker.show();
                        editor.hideTransientPath();
                        return;
                    }
                }
            }
            insertionEditor.transientMarker.hide();
        }
        if (editor.markers.length > 0 && latLng != null)
            editor.setTransientPath([insertionEditor.currentInsertionEnd.marker().getPoint(), latLng]);
        else
            editor.hideTransientPath();
    };
    GEvent.addListener(this.transientMarker, 'mouseover', function() {
        insertionEditor.transientMarker.setImage($('map_handle_active').src);
        editor.canAppendPoint(false);
    });
    GEvent.addListener(this.transientMarker, 'mouseout', function() {
        insertionEditor.transientMarker.setImage($('map_handle').src);
        editor.canAppendPoint(true);
    });
    GEvent.bind(this.transientMarker, 'dragstart', this, function() {
        editor.canAppendPoint(false);
        this.canInsertPoint(false, true);
        editor.startInsertion();
        this.transientMarker.show();
        insertionEditor.transientMarker.setImage($('map_handle').src);
    });
    GEvent.bind(this.transientMarker, 'dragend', this, function() {
        editor.hideTransientPath();
        editor.addMarker(insertionEditor.transientMarker.getPoint(), markerIndex + 1);
        editor.draw();
        editor.endInsertion();
        this.canInsertPoint(true);
        editor.canAppendPoint(true);
    });
    GEvent.addListener(this.transientMarker, 'drag', function() {
        var poly = [editor.markers[markerIndex].getPoint(),
            insertionEditor.transientMarker.getPoint(),
            editor.markers[markerIndex + 1].getPoint()];
        editor.setTransientPath(poly);
    });
    this.insertLinePointHandleHandler = null;
}
PointInsertionEditor.prototype.canInsertPoint = function(can, keepMarker) {
    this.editor.hideTransientPath();
    if (can) {
        if (this.insertLinePointHandleHandler == null)
            this.insertLinePointHandleHandler = GEvent.addListener(map, 'mousemove', this.insertLinePointHandleCallback);
    } else {
        if (!keepMarker)
            this.transientMarker.hide();
        if (this.insertLinePointHandleHandler != null) {
            GEvent.removeListener(this.insertLinePointHandleHandler);
            this.insertLinePointHandleHandler = null;
        }
    }
};
PointInsertionEditor.prototype.insertAtStart = function() {
    this.currentInsertionEnd = {
        index:function() {
            return 0;
        }.bind(this),
        marker: function() {
            return this.editor.markers[0];
        }.bind(this)
    };
};
PointInsertionEditor.prototype.insertAtEnd = function() {
    this.currentInsertionEnd = {
        index:function() {
            return this.editor.markers.length;
        }.bind(this),
        marker: function() {
            var markers = this.editor.markers;
            return markers[markers.length - 1];
        }.bind(this)
    };
};
