_mPreferMetric = true;
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
var icon;
var markers = [];
var line;
function draw() {
    if (line) {
        map.removeOverlay(line)
    }
    var poly = [];
    var encodedTrack = "";
    var distance = 0
    for (var i = 0; i < markers.length; ++i) {
        var pnt = markers[i].getPoint();
        if (poly.length > 0)
            distance += poly[poly.length - 1].distanceFrom(pnt);
        poly.push(pnt);
        encodedTrack += '[' + pnt.lat() + ',' + pnt.lng() + '],';
    }
    line = new GPolyline(poly, '#FF0000', 3, 1);
    map.addOverlay(line);
    $('trackVar').setValue(encodedTrack);
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
function createIcon() {
    var icon = new GIcon();
    icon.image = "http://labs.google.com/ridefinder/images/mm_20_red.png";
    icon.shadow = "http://labs.google.com/ridefinder/images/mm_20_shadow.png";
    icon.iconSize = new GSize(12, 20);
    icon.shadowSize = new GSize(22, 20);
    icon.iconAnchor = new GPoint(6, 20);
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
        draw();
        GEvent.addListener(map, 'click', function(overlay, latLng) {
            addMarker(latLng);
            draw();
        });
        new GDraggableObject($('controlPanel'));
        map.setMapType(IGN_PHOTO_TYPE);
    }
}
function addMarker(pnt) {
    map.panTo(pnt);
    var marker = new GMarker(pnt, {icon:icon, draggable: true});
    markers.push(marker);
    map.addOverlay(marker);
    marker.enableDragging();
    GEvent.addListener(marker, "drag", draw);
}
function clearMap() {
    while (markers.length > 0) {
        map.removeOverlay(markers.pop());
    }
    markers = [];
    draw()
}
function newTrack() {
    clearMap();
}