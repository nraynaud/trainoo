_mPreferMetric = true;
function loaded(evt) {
    updateHeight();
    startMap();
}
document.observe('dom:loaded', loaded);
function updateHeight() {
    $('content').style.height = $('center').clientHeight - $('content').offsetTop - 10 + "px";
}
var map;
var icon;
var markers = [];
var poly = [];
var line;
var distance = 0;
var encodedTrack = "";
function draw() {
    if (line) {
        map.removeOverlay(line)
    }
    line = new GPolyline(poly, '#FF0000', 3, 1);
    map.addOverlay(line);
    $('distance').update((distance / 1000).toFixed(2) + "km");
    $('lengthVar').setValue(distance / 1000);
    $('pointsCount').update(markers.length);
}
function loadTrack(track) {
    clearMap()
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
    var center = bounds.getCenter();
    map.setCenter(center, map.getBoundsZoomLevel(bounds));
}
function startMap() {
    window.addEventListener("resize", updateHeight, false);
    map = new GMap2($("map"));
    var start = new GLatLng(46.980252, 2.548828);
    map.setCenter(start, 5);
    map.setMapType(G_HYBRID_MAP);
    map.enableScrollWheelZoom();
    map.addControl(new GMapTypeControl(1));
    map.addControl(new GLargeMapControl());
    map.enableContinuousZoom();
    map.addControl(new GScaleControl(250));
    // red marker icon
    icon = new GIcon();
    icon.image = "http://labs.google.com/ridefinder/images/mm_20_red.png";
    icon.shadow = "http://labs.google.com/ridefinder/images/mm_20_shadow.png";
    icon.iconSize = new GSize(12, 20);
    icon.shadowSize = new GSize(22, 20);
    icon.iconAnchor = new GPoint(6, 20);
    draw();
    GEvent.addListener(map, 'click', function(overlay, latLng) {
        if (latLng) {
            console.log("click:" + latLng + map.getCurrentMapType().getProjection().fromLatLngToPixel(latLng, map.getZoom()));
            $('position').update(latLng.toString());
            addMarker(latLng);
        }
    });
    GEvent.addListener(map, 'mousemove', function(latLng) {
        if (latLng) {
            $('position').update(latLng.toString());
        }
    });
    new GDraggableObject($('controlPanel'));
    var type = createGeoMapType();
    map.addMapType(type);
    map.setMapType(type);
}
function addMarker(pnt) {
    map.panTo(pnt);
    var marker = new GMarker(pnt, {icon:icon, draggable: true});
    markers.push(marker);
    if (poly.length > 0)
        distance += poly[poly.length - 1].distanceFrom(pnt);
    poly.push(pnt);
    encodedTrack += '[' + pnt.lat() + ',' + pnt.lng() + '],';
    $('trackVar').setValue(encodedTrack);
    map.addOverlay(marker);
    marker.enableDragging();
    GEvent.addListener(marker, "drag", function() {
        draw();
    });
    draw();
}
function clearMap() {
    while (markers.length > 0) {
        map.removeOverlay(markers.pop());
    }
    markers = [];
    poly = [];
    distance = 0;
    encodedTrack = "";
    draw()
}
function newTrack() {
    clearMap();
}