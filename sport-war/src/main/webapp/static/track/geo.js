var map_scale_enc_table = [undefined, "8u6G", "8u63", "8u6r", "8u68", "8u6k", "8u6T", "8u6Q", "8u6m", "8u6Y", "8u6I", "8u6c", "8u6j", "8u6K", "8u6M", "8u67", "8u6W", "8u6b", "8u62", "8u6q", "8u6t", "8u6L"];
var photo_scale_enc_table = [undefined, "UxGG", "UxG3", "UxGr", "UxG8", "UxGk", "UxGT", "UxGQ", "UxGm", "UxGY", "UxGI", "UxGc", "UxGj", "UxGK", "UxGM", "UxG7", "UxGW", "UxGb", "UxG2", "UxGq", "UxGt", "UxGL"];
var len_enc_table = ["x", "G", "3", "r", "8", "k", "T", "Q", "m", "Y", "I", "c", "j", "K", "M", "7", "W", "b", "2", "q", "t", "L", "H", "9", "f"];
var enc_table = ["X", "u", "P", "4", "N", "G", "Z", "8", "n", "g", "I", "c", "j", "K", "M", "7", "W", "Q", "T", "b", "2", "q", "map", "1", "e", "h", "O", "o", "t", "L", "H", "9", "z", "s", "m", "a", "w", "J", "S", "Y", "l", "A", "i", "f", "U", "v", "y", "r", "k", "E", "D", "x", "3", "6", "5", "F", "p", "0", "V", "R", "d", "B"];
var sign_enc_table = ["2", "A", "9", "r"];
var ratios = [undefined,
    [0.199660570975744,-0.199660570975744],
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
    [7.8002161543273e-007,-7.8002161543273e-007],
    [3.90010807716365e-007,-3.90010807716365e-007],
    [1.95005403858182e-007,-1.95005403858182e-007],
    [1.95004214034262e-007,-1.95004214034262e-007],
    [1.95003191791175e-007,-1.95003191791175e-007],
    [1.95002186306171e-007,-1.95002186306171e-007]];
var IGN_PHOTO_PREFIX = "/FXX-GFD-PHOTO__PHOTO_jpg/256_256_";
var IGN_MAP_PREFIX = "/FXX-GFD-CARTE__CARTE_jpg/256_256_";
var EARTH_RADIUS_METERS = 6378137;
var GLOBAL_PRECISION = 0.01;
if (! ("console" in window) || !("firebug" in console)) {
    var names = ["log", "debug", "info", "warn", "error", "assert", "dir", "dirxml", "group"
        , "groupEnd", "time", "timeEnd", "count", "trace", "profile", "profileEnd"];
    window.console = {};
    for (var i = 0; i < names.length; ++i) window.console[names[i]] = function() {
    };
}
function log(txt) {
    //console.log(txt);
}
var geoEncode = function(nombre) {
    var base = 62;
    if (!isNaN(nombre)) {
        var r = nombre % base;
        var encoded;
        if (nombre - r == 0) {
            encoded = enc_table[r];
        } else {
            encoded = geoEncode(Math.floor((nombre - r) / base), base) + enc_table[r];
        }
    } else
        return 0;
    return encoded;
};
var encodeTile = function(prefix, scale, tileX, tileY) {
    var encoded_string;
    var table;
    table = prefix == IGN_PHOTO_PREFIX ? photo_scale_enc_table : map_scale_enc_table;
    encoded_string = table[scale];
    var signIndex;
    signIndex = tileX * tileY >= 0 ? tileX < 0 ? 3 : 0 : tileX < 0 ? 2 : 1;
    encoded_string += sign_enc_table[signIndex];
    tileX = Number(tileX);
    tileY = Number(tileY);
    var encodedTileX = geoEncode(Math.abs(tileX));
    var encodedTileY = geoEncode(Math.abs(tileY));
    encoded_string += len_enc_table[encodedTileX.length];
    encoded_string += encodedTileX + encodedTileY;
    log("X:" + tileX + " Y:" + tileY + " scale:" + scale + " encoded:" + encoded_string + " Xlen:" + len_enc_table[encodedTileX.length]
            + " eX:" + encodedTileX + " eY:" + encodedTileY);
    return encoded_string;
};
function degreeToRad(angle) {
    return Math.PI * angle / 180;
}
function radianToDegree(angle) {
    return angle * 180 / Math.PI;
}
/*
this is an involution function, if you stay in north hemisphere. DON'T TRY THIS UNDER THE EQUATOR 8
 */
function googleToGeoTileY(googleY, zoom) {
    var northPoleTile = -EARTH_RADIUS_METERS * ratios[googleToGeoZoom(zoom)][1] / 256 / GLOBAL_PRECISION;
    return Math.floor(Math.ceil(northPoleTile) - googleY);
}
function EquirectangularProjection() {
    this.phi0 = degreeToRad(46.5);
    this.lngProjectionFactor = (EARTH_RADIUS_METERS * Math.cos(this.phi0));
}
EquirectangularProjection.prototype = new GProjection();
function convertYPixel(y, zoom) {
    var northPolePixel = -EARTH_RADIUS_METERS * ratios[zoom][1] / GLOBAL_PRECISION;
    var northPoleRoundedTile = (Math.ceil(northPolePixel / 256) + 1) * 256;
    return northPoleRoundedTile - y;
}
EquirectangularProjection.prototype.fromLatLngToPixel = function(latlong, _zoom) {
    var zoom = googleToGeoZoom(_zoom);
    var pixX = degreeToRad(latlong.lng()) * this.lngProjectionFactor * ratios[zoom][0] / GLOBAL_PRECISION;
    var pixY = -EARTH_RADIUS_METERS * degreeToRad(latlong.lat()) * ratios[zoom][1] / GLOBAL_PRECISION;
    var result = new GPoint(pixX, convertYPixel(pixY, zoom));
    log("fromLatLngToPixel:" + latlong + " -> " + result + " tile:(" + pixX / 256 + ", " + pixY / 256 + ")");
    return result;
};
EquirectangularProjection.prototype.fromPixelToLatLng = function(pix, _zoom, bounded) {
    var zoom = googleToGeoZoom(_zoom);
    var lng = radianToDegree((pix.x / ratios[zoom][0] * GLOBAL_PRECISION) / this.lngProjectionFactor);
    var lat = -radianToDegree(convertYPixel(pix.y, zoom) / ratios[zoom][1] * GLOBAL_PRECISION / EARTH_RADIUS_METERS);
    var result = new GLatLng(lat, lng, bounded);
    log("fromPixelToLatLng: " + pix + " zoom:" + zoom + " -> " + result);
    return result;
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
            ensureCookies();
            return "http://visu-2d.geoportail.fr/geoweb/maps"
                    + encodeTile(prefix, googleToGeoZoom(zoom), pt.x, googleToGeoTileY(pt.y, zoom)) + ".jpg";
        }
        return layer;
    }
    var projection = new EquirectangularProjection();
    return new GMapType([createLayer(prefix)], projection, "IGN", {errorMessage:"Aucune donnée disponible", textColor:textColor});
}
function googleToGeoZoom(googleZoom) {
    var z = 22 - googleZoom;
    return z > 0 ? z < ratios.length ? z : ratios.length - 1 : 1;
}
function createGeoMapType(prefix, textColor, maxZoom) {
    ensureCookies();
    testProjection();
    testEncodeTile();
    return new createMapType(prefix, textColor, maxZoom);
}
var lastTimeCookieChecked;
/**
 * the portal seems to mandate to have a cookie to get the tiles. the cookie is set after a strange redirection when querying http://www.geoportail.fr/imgs/visu/empty.gif.
 * it expires quick, so we re-query the url every minute or so.
 */
function ensureCookies() {
    function fetchFakeImage() {
        var img = $('lolToken');
        img.style.visibility = 'hidden';
        return img;
    }
    var now = new Date().getTime() / 1000;
    if (lastTimeCookieChecked == null || now - lastTimeCookieChecked > 60) {
        fetchFakeImage().src = "http://www.geoportail.fr/imgs/visu/empty.gif?random=" + Math.random();
        lastTimeCookieChecked = now;
    }
}
/******some tests ******/
function testEncodeTile() {
    var tests = [
        ["8u6M9GPp", -2, 56, 14],
        ["8u6I2GgKl", 9, 846, 10]
    ];
    for (var i = 0; i < tests.length; i++) {
        var testSegment = tests[i]
        log("expected:" + testSegment[0] + " actual:" + encodeTile("/FXX-GFD-CARTE__CARTE_jpg/256_256_",
                testSegment[3], testSegment[1], testSegment[2]));
    }
}
function testProjection() {
    var projection = new EquirectangularProjection();
    var input = new GLatLng(46.980252, 2.548828);
    var pix = projection.fromLatLngToPixel(input, 11);
    log("test result:" + projection.fromPixelToLatLng(pix, 11) + " expected:" + input);
}