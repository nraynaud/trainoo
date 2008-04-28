var DELETE_BUTTON = "<div id='deleteButton' title='effacer le point'>X</div>"
function UpdateEditor(editor) {
    this.editor = editor;
}
function hideDeleteMarkerButton() {
    var del = $('deleteButton');
    del.hide();
}
UpdateEditor.prototype.install = function() {
    var editor = this.editor;
    var map = editor.map;
    editor.markers.each(function(m) {
        var marker = m.marker;
        map.addOverlay(marker);
        m.dragStartHandler = GEvent.addListener(marker, "dragstart", function() {
            hideDeleteMarkerButton();
        });
        m.dragEndHandler = GEvent.addListener(marker, "dragend", function() {
            editor.hideTransientPath();
            editor.draw();
        });
        m.dragHandler = GEvent.addListener(marker, "drag", function() {
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
        m.mouseOverHandler = GEvent.addListener(marker, "mouseover", function() {
            var del = $('deleteButton');
            if (del == null) {
                $("map").insert(DELETE_BUTTON);
                del = $('deleteButton');
                GEvent.addListener(editor.map, "move", hideDeleteMarkerButton);
                GEvent.addListener(editor.map, "zoomend", hideDeleteMarkerButton);
                GEvent.addDomListener(del, 'mouseover', function() {
                    editor.insertionEditor.canInsertPoint(false);
                });
                GEvent.addDomListener(del, 'mouseout', function() {
                    editor.insertionEditor.canInsertPoint(true);
                });
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
        m.mouseOutHandler = GEvent.addListener(marker, "mouseout", function() {
            marker.setImage($('map_marker').src);
        });
    });
}
UpdateEditor.prototype.unInstall = function() {
    hideDeleteMarkerButton();
    var map = this.editor.map;
    this.editor.markers.each(function(m) {
        GEvent.removeListener(m.mouseOutHandler);
        GEvent.removeListener(m.mouseOverHandler);
        GEvent.removeListener(m.dragHandler);
        GEvent.removeListener(m.dragEndHandler);
        GEvent.removeListener(m.dragStartHandler);
        map.removeOverlay(m.marker);
    });
}