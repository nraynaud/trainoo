var DELETE_BUTTON = "<div id='deleteButton' title='effacer le point'>X</div>"
function UpdateEditor(editor) {
    this.editor = editor;
}
function hideDeleteMarkerButton() {
    var del = $('deleteButton');
    if (del)
        del.hide();
}
UpdateEditor.prototype.install = function() {
    var editor = this.editor;
    if (editor.markers.length > 0) {
        this.showMarker(editor.markers[0]);
        if (editor.markers.length > 1)
            this.showMarker(editor.markers[editor.markers.length - 1]);
    }
    this.vocalMarkerFollowMouse();
}
UpdateEditor.prototype.unInstall = function() {
    log('enter uninstall update')
    this.muteMarkerFollowMouse();
    hideDeleteMarkerButton();
    var map = this.editor.map;
    this.editor.markers.each(function(m) {
        /*
        m.listeners.each(function(listener) {
            GEvent.removeListener(listener);
        });*/
        map.removeOverlay(m.marker);
    });
}
UpdateEditor.prototype.showMarker = function(m) {
    var marker = m.marker
    var subeditor = this;
    var editor = this.editor;
    editor.map.addOverlay(marker);
    marker.enableDragging();
    if (m.listeners == null) {
        m.listeners = [];
        function addListener(type, callback) {
            m.listeners.push(GEvent.addListener(marker, type, callback));
        }
        addListener("dragstart", function() {
            log('drag')
            hideDeleteMarkerButton();
        });
        addListener("dragend", function() {
            editor.hideTransientPath();
            editor.draw();
        });
        addListener("drag", function() {
            var index = m.index;
            var poly = [m.getPoint()];
            if (index > 0) {
                poly.unshift(editor.markers[index - 1].getPoint())
            }
            if (index < editor.markers.length - 1) {
                poly.push(editor.markers[index + 1].getPoint())
            }
            editor.setTransientPath(poly);
        });
        addListener("mouseover", function() {
            subeditor.muteMarkerFollowMouse();
            var del = $('deleteButton');
            if (del == null) {
                $("map").insert(DELETE_BUTTON);
                del = $('deleteButton');
                GEvent.addListener(editor.map, "move", hideDeleteMarkerButton);
                GEvent.addListener(editor.map, "zoomend", hideDeleteMarkerButton);
            }
            if (editor.deleteHandler != null)
                GEvent.removeListener(editor.deleteHandler);
            editor.deleteHandler = GEvent.addDomListener(del, 'click', function() {
                editor.deleteMarker(m);
                del.hide();
            });
            var pixelPoint = editor.map.fromLatLngToContainerPixel(m.getPoint());
            var icon = marker.getIcon();
            var xOffset = -icon.iconAnchor.x;
            var yOffset = -icon.iconAnchor.y - del.getDimensions().height;
            del.setStyle({left:pixelPoint.x + xOffset + 'px', top: pixelPoint.y + yOffset + 'px'});
            del.show();
            marker.setImage($('map_marker_active').src);
        });
        addListener("mouseout", function() {
            marker.setImage($('map_marker').src);
            subeditor.vocalMarkerFollowMouse();
        });
    }
}
UpdateEditor.prototype.muteMarkerFollowMouse = function() {
    log('mute')
    GEvent.removeListener(this.mouseMoveListener);
    this.mouseMoveListener = null;
}
UpdateEditor.prototype.vocalMarkerFollowMouse = function() {
    log('vocal')
    var subEditor = this;
    if (this.mouseMoveListener == null)
        this.mouseMoveListener = GEvent.addListener(map, 'mousemove', function(latLng) {
            function square(x) {
                return Math.pow(x, 2);
            }
            function squareDistance(p1, p2) {
                return square(p2.lng() - p1.lng()) + square(p2.lat() - p1.lat());
            }
            if (this.transientMarkers != null) {
                this.transientMarkers.each(function(m) {
                    map.removeOverlay(m.marker.marker);
                });
            }
            var distances = [];
            editor.markers.each(function(m) {
                distances.push({distance: squareDistance(latLng, m.getPoint()), marker: m});
            });
            distances.sort(function(d1, d2) {
                return d1.distance - d2.distance;
            });
            this.transientMarkers = distances.slice(0, 10);
            this.transientMarkers.each(function(m) {
                subEditor.showMarker(m.marker);
            });
        });
}