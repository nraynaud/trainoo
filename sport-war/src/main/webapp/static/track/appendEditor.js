function AppendEditor(editor) {
    this.editor = editor;
    function nextPreviewPoint(latLng) {
        editor.setTransientPath([editor.markers[editor.markers.length - 1].getPoint(), latLng]);
    }
    function isByBorder(latLng) {
        var mapDiv = $('map');
        var point = editor.map.fromLatLngToContainerPixel(latLng);
        function isOutOfRange(value, range, margin) {
            return value < range * margin || value > range * (1 - margin);
        }
        var borderWidth = 0.15;
        return isOutOfRange(point.x, mapDiv.getWidth(), borderWidth) || isOutOfRange(point.y, mapDiv.getHeight(), borderWidth);
    }
    this.addPointCallback = function(overlay, latLng) {
        editor.hideTransientPath();
        editor.addMarker(latLng);
        if (isByBorder(latLng))
            map.panTo(latLng);
        editor.draw();
        nextPreviewPoint(this.pos);
    };
    this.linePreviewCallback = function(latLng) {
        this.pos = latLng;
        nextPreviewPoint(latLng);
    }
}
AppendEditor.prototype.install = function() {
    this.canAppendPointHandler = GEvent.addListener(this.editor.map, 'click', this.addPointCallback)
    this.linePreviewHandler = GEvent.addListener(this.editor.map, 'mousemove', this.linePreviewCallback);
    this.editor.map.getDragObject().setDraggableCursor('crosshair');
}
AppendEditor.prototype.unInstall = function() {
    this.editor.map.getDragObject().setDraggableCursor('default');
    GEvent.removeListener(this.canAppendPointHandler);
    GEvent.removeListener(this.linePreviewHandler);
    this.editor.hideTransientPath();
}