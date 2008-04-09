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
        map.removeOverlay(line);
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
    line = new GPolyline(poly, 'blue', 3, 1);
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
function createHandleIcon() {
    var icon = new GIcon();
    icon.image = $('map_handle').src;
    icon.iconSize = new GSize(11, 11);
    icon.iconAnchor = new GPoint(5, 5);
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
        var myMarker = new GMarker(new GLatLng(47.081850, 2.3995035), {
            icon: createHandleIcon(), title:"insérer un point", draggable: true});
        map.addOverlay(myMarker);
        myMarker.hide();
        var markerIndex = null;
        var transientPath = null;
        GEvent.addListener(myMarker, 'mouseover', function() {
            myMarker.setImage($('map_handle_active').src);
        });
        GEvent.addListener(myMarker, 'mouseout', function() {
            myMarker.setImage($('map_handle').src);
        });
        GEvent.addListener(myMarker, 'dragstart', function() {
            myMarker.setImage($('map_handle').src);
        });
        GEvent.addListener(myMarker, 'dragend', function() {
            if (transientPath != null) {
                map.removeOverlay(transientPath);
                transientPath = null;
            }
            addMarker(myMarker.getPoint(), markerIndex + 1);
            draw();
        });
        GEvent.addListener(myMarker, 'drag', function() {
            if (transientPath != null) {
                map.removeOverlay(transientPath)
            }
            var poly = [markers[markerIndex].getPoint(), myMarker.getPoint(), markers[markerIndex + 1].getPoint()];
            transientPath = new GPolyline(poly, 'red', 6, 1);
            map.addOverlay(transientPath);
        });
        GEvent.addListener(map, 'mousemove', function(latLng) {
            if (markers.length > 1) {
                function square(x) {
                    return Math.pow(x, 2);
                }
                function computePoint(p, p1, p2) {
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
                for (var i = 0; i < markers.length - 1; i++) {
                    var point = computePoint(latLng, markers[i].getPoint(), markers[i + 1].getPoint());
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
                        myMarker.setLatLng(pointMin);
                        myMarker.show();
                        return;
                    }
                }
                myMarker.hide();
            }
        });
        new GDraggableObject($('controlPanel'));
        map.setMapType(IGN_PHOTO_TYPE);
    }
}
function addMarker(point, index) {
    map.panTo(point);
    var marker = new GMarker(point, {draggable: true});
    if (index == null)
        markers.push(marker);
    else
        markers.splice(index, 0, marker)
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