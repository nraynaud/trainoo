_mPreferMetric = true;
function updateHeight() {
    $('content').style.height = $('center').clientHeight - $('content').offsetTop - 10 + "px";
}
function loaded() {
    updateHeight();
    $('heading').remove();
    startMap();
}
var map;
var icon;
var markers = [];
var poly = [];
var line;
function draw() {
    poly.length = 0;
    var distance = 0;
    var encodedTrack = "";
    for (var i = 0; i < markers.length; i++) {
        var marker = markers[i];
        var pt = marker.getPoint();
        poly.push(pt);
        if (i > 0)
            distance += markers[i - 1].getPoint().distanceFrom(pt);
        encodedTrack += '[' + pt.lat() + ',' + pt.lng() + '],'
    }
    $('trackVar').setValue(encodedTrack);
    if (line) {
        map.removeOverlay(line)
    }
    ;
    line = new GPolyline(poly, '#FF0000', 3, 1, {geodesic:true});
    map.addOverlay(line);
    $('distance').update((distance / 1000).toFixed(2) + "km");
    $('lengthVar').setValue(distance / 1000);
}
function addMarker(pnt) {
    map.panTo(pnt);
    var marker = new GMarker(pnt, {icon:icon, draggable: true});
    markers.push(marker);
    map.addOverlay(marker);
    marker.enableDragging();
    GEvent.addListener(marker, "drag", function() {
        draw();
    });
    draw();
}
function loadTrack(track) {
    while (markers.length > 0) {
        map.removeOverlay(markers.pop());
    }
    markers = [];
    var positions = eval('[' + track + ']');
    for (var i1 = 0; i1 < positions.length; i1++) {
        addMarker(new GLatLng(positions[i1][0], positions[i1][1]));
    }
    draw();
    fit();
}
function fit() {
    var bounds = new GLatLngBounds();
    for (var i = 0; i < markers.length; i++) {
        bounds.extend(markers[i].getPoint())
    }
    ;
    var center = bounds.getCenter();
    map.setCenter(center, map.getBoundsZoomLevel(bounds));
}
function startMap() {
    window.addEventListener("resize", updateHeight, false);
    map = new GMap2(document.getElementById("map"));
    var start = new GLatLng(46.980252, 2.548828);
    map.setCenter(start, 5);
    map.setMapType(G_HYBRID_MAP);
    map.enableScrollWheelZoom();
    map.addControl(new GMapTypeControl(1));
    map.addControl(new GLargeMapControl());
    map.enableContinuousZoom();
    map.enableDoubleClickZoom();
    map.addControl(new GScaleControl(250));
    // red marker icon
    icon = new GIcon();
    icon.image = "http://labs.google.com/ridefinder/images/mm_20_red.png";
    icon.shadow = "http://labs.google.com/ridefinder/images/mm_20_shadow.png";
    icon.iconSize = new GSize(12, 20);
    icon.shadowSize = new GSize(22, 20);
    icon.iconAnchor = new GPoint(6, 20);
    draw();
    GEvent.addListener(map, 'click', function(overlay, pnt) {
        if (pnt) {
            addMarker(pnt);
        }
    });
    new GDraggableObject($('controlPanel'));
}
