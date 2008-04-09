var len_enc_table = ["x", "G", "3", "r", "8", "k", "T", "Q", "m", "Y", "I", "c", "j", "K", "M", "7", "W", "b", "2", "q", "t", "L", "H", "9", "f"];
var enc_table = ["X", "u", "P", "4", "N", "G", "Z", "8", "n", "g", "I", "c", "j", "K", "M", "7", "W", "Q", "T", "b", "2", "q", "C", "1", "e", "h", "O", "o", "t", "L", "H", "9", "z", "s", "m", "a", "w", "J", "S", "Y", "l", "A", "i", "f", "U", "v", "y", "r", "k", "E", "D", "x", "3", "6", "5", "F", "p", "0", "V", "R", "d", "B"];
var sign_enc_table = ["2", "A", "9", "r"];
var ratios = [undefined,
    [0.1996605709757440000,-0.199660570975744],
    [9.98302854878722e-002,-9.98302854878722e-002],
    [4.99151427439361e-002,-4.99151427439361e-002],
    [1.99660579761807e-002,-1.99660579761807e-002],
    [9.98302898809033e-003,-9.98302898809033e-003],
    [3.99363420481952e-003,-3.99363420481952e-003],
    [1.99681710240976e-003,-1.99681710240976e-003],
    [9.98434978032063e-004,-9.98434978032063e-004],
    [7.98743712948635e-004,-7.98743712948635e-004],
    [3.99371856474318e-004,-3.99371856474318e-004],
    [1.99685928237159e-004,-1.99685928237159e-004],
    [9.98347786662634e-005,-9.98347786662634e-005],
    [4.99213533572093e-005,-4.99213533572093e-005],
    [2.49606916938473e-005,-2.49606916938473e-005],
    [1.24803458469237e-005,-1.24803458469237e-005],
    [6.24017292346184e-006,-6.24017292346184e-006],
    [3.12008646173092e-006,-3.12008646173092e-006],
    [1.56004323086546e-006,-1.56004323086546e-006],
    [7.80021615432730e-007,-7.80021615432730e-007],
    [3.90010807716365e-007,-3.90010807716365e-007],
    [1.95005403858182e-007,-1.95005403858182e-007],
    [1.95004214034262e-007,-1.95004214034262e-007],
    [1.95003191791175e-007,-1.95003191791175e-007],
    [1.95002186306171e-007,-1.95002186306171e-007]];
var IGN_PHOTO_PREFIX = "/FXX-GFD-PHOTO__PHOTO_jpg/256_256_";
var IGN_PHOTO_KEY = "UxG";
var IGN_MAP_PREFIX = "/FXX-GFD-CARTE__CARTE_jpg/256_256_";
var IGN_MAP_KEY = "8u6";
var EARTH_RADIUS_METERS = 6378137;
var GLOBAL_PRECISION = 0.01;
function log(txt) {
    if ("console" in window && "firebug" in console)
        console.log(txt);
}
function geoEncode(_number) {
    var number = Math.abs(_number)
    var base = 62;
    var r = number % base;
    return (number - r == 0 ? "" : geoEncode(Math.floor((number - r) / base))) + enc_table[r];
}
function encodeTile(prefix, scale, tileX, tileY) {
    var encoded_string = prefix == IGN_PHOTO_PREFIX ? IGN_PHOTO_KEY : IGN_MAP_KEY;
    encoded_string += len_enc_table[scale];
    var signIndex = tileX * tileY >= 0 ? tileX < 0 ? 3 : 0 : tileX < 0 ? 2 : 1;
    encoded_string += sign_enc_table[signIndex];
    var encodedTileX = geoEncode(tileX);
    encoded_string += len_enc_table[encodedTileX.length];
    encoded_string += encodedTileX + geoEncode(tileY);
    return encoded_string;
}
function degreeToRad(angle) {
    return Math.PI * angle / 180;
}
function radianToDegree(angle) {
    return angle * 180 / Math.PI;
}
function northPoleTile(geoZoom) {
    return Math.ceil(-EARTH_RADIUS_METERS * ratios[geoZoom][1] / GLOBAL_PRECISION / 256);
}
function googleToGeoTileY(googleY, zoom) {
    return Math.floor(northPoleTile(googleToGeoZoom(zoom)) - googleY);
}
// corrects  the north pole tile shift
//this is an involution function, as long as you stay in the north hemisphere. DON'T TRY THIS UNDER THE EQUATOR !
function convertYPixel(y, zoom) {
    return (northPoleTile(zoom) + 1) * 256 - y;
}
function EquirectangularProjection() {
    this.phi0 = degreeToRad(46.5);
    this.lngProjectionFactor = (EARTH_RADIUS_METERS * Math.cos(this.phi0));
}
EquirectangularProjection.prototype = new GProjection();
EquirectangularProjection.prototype.fromLatLngToPixel = function(latlong, _zoom) {
    var zoom = googleToGeoZoom(_zoom);
    var pixX = degreeToRad(latlong.lng()) * this.lngProjectionFactor * ratios[zoom][0] / GLOBAL_PRECISION;
    var pixY = -EARTH_RADIUS_METERS * degreeToRad(latlong.lat()) * ratios[zoom][1] / GLOBAL_PRECISION;
    return new GPoint(pixX, convertYPixel(pixY, zoom));
};
EquirectangularProjection.prototype.fromPixelToLatLng = function(pix, _zoom, bounded) {
    var zoom = googleToGeoZoom(_zoom);
    var lng = radianToDegree((pix.x / ratios[zoom][0] * GLOBAL_PRECISION) / this.lngProjectionFactor);
    var lat = -radianToDegree(convertYPixel(pix.y, zoom) / ratios[zoom][1] * GLOBAL_PRECISION / EARTH_RADIUS_METERS);
    return new GLatLng(lat, lng, bounded);
};
/**
 * avoids using Infinity because it creates a bug in overlays !
 */
EquirectangularProjection.prototype.getWrapWidth = function() {
    return 99999999999999;
}
function createMapType(prefix, textColor, maxZoom) {
    var copyCollection = new GCopyrightCollection('Geo');
    var copyright = new GCopyright(1, new GLatLngBounds(new GLatLng(-90, -180), new GLatLng(90, 180)), 0, "© IGN");
    copyCollection.addCopyright(copyright);
    function createLayer(prefix) {
        var layer = new GTileLayer(copyCollection, 5, maxZoom);
        layer.getTileUrl = function (pt, zoom) {
            return "/track/getTile?tileName=maps"
                    + encodeTile(prefix, googleToGeoZoom(zoom), pt.x, googleToGeoTileY(pt.y, zoom)) + ".jpg";
        }
        return layer;
    }
    return new GMapType([createLayer(prefix)], new EquirectangularProjection(), "IGN", {
        errorMessage:"Aucune donnée disponible", textColor:textColor});
}
function googleToGeoZoom(googleZoom) {
    var z = 22 - googleZoom;
    return z > 0 ? z < ratios.length ? z : ratios.length - 1 : 1;
}
function createGeoMapType(prefix, textColor, maxZoom) {
    testProjection();
    testEncodeTile();
    return new createMapType(prefix, textColor, maxZoom);
}
var lastTimeCookieChecked;
/******some tests ******/
function testEncodeTile() {
    var tests = [ ["8u6M9GPp", -2, 56, 14],  ["8u6I2GgKl", 9, 846, 10] ];
    for (var i = 0; i < tests.length; i++) {
        var testSegment = tests[i]
        log("expected:" + testSegment[0] + " actual:" + encodeTile("/FXX-GFD-CARTE__CARTE_jpg/256_256_",
                testSegment[3], testSegment[1], testSegment[2]));
    }
}
function testProjection() {
    var projection = new EquirectangularProjection();
    var input = new GLatLng(46.980252, 2.548828);
    log("test result:" + projection.fromPixelToLatLng(projection.fromLatLngToPixel(input, 11), 11) + " expected:" + input);
}