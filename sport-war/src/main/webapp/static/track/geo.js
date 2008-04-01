var map_scale_enc_table = [undefined, "8u6G", "8u63", "8u6r", "8u68", "8u6k", "8u6T", "8u6Q", "8u6m", "8u6Y", "8u6I", "8u6c", "8u6j", "8u6K", "8u6M", "8u67", "8u6W", "8u6b", "8u62", "8u6q", "8u6t", "8u6L"];
var photo_scale_enc_table = [undefined, "UxGG", "UxG3", "UxGr", "UxG8", "UxGk", "UxGT", "UxGQ", "UxGm", "UxGY", "UxGI", "UxGc", "UxGj", "UxGK", "UxGM", "UxG7", "UxGW", "UxGb", "UxG2", "UxGq", "UxGt", "UxGL"];
var len_enc_table = ["x", "G", "3", "r", "8", "k", "T", "Q", "m", "Y", "I", "c", "j", "K", "M", "7", "W", "b", "2", "q", "t", "L", "H", "9", "f"];
var enc_table = ["X", "u", "P", "4", "N", "G", "Z", "8", "n", "g", "I", "c", "j", "K", "M", "7", "W", "Q", "T", "b", "2", "q", "C", "1", "e", "h", "O", "o", "t", "L", "H", "9", "z", "s", "m", "a", "w", "J", "S", "Y", "l", "A", "i", "f", "U", "v", "y", "r", "k", "E", "D", "x", "3", "6", "5", "F", "p", "0", "V", "R", "d", "B"];
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
function log(txt) {
    if ((typeof(console) != "undefined") && console.log) {
        console.log(txt);
    }
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
/**
 *
 * scale 6(max de détails)->16(on voit rien)
 * google 11 ~ geoportail 16
 */
var encodeTile = function(prefix, scale, tileX, tileY) {
    var encoded_string;
    var table;
    table = prefix == "/FXX-GFD-PHOTO__PHOTO_jpg/256_256_" ? photo_scale_enc_table : map_scale_enc_table;
    encoded_string = table[scale];
    if (tileX * tileY >= 0) {
        if (tileX < 0) {
            encoded_string += sign_enc_table[3];
        } else {
            encoded_string += sign_enc_table[0];
        }
    } else {
        if (tileX < 0) {
            encoded_string += sign_enc_table[2];
        } else {
            encoded_string += sign_enc_table[1];
        }
    }
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
function testEncodeTile() {
    var tests = [
        ["8u6M9GPp", -2, 56, 14],
        ["8u6I2GgKl", 9, 846, 10]
    ];
    for (var i = 0; i < tests.length; i++) {
        var testSegment = tests[i]
        log("expected:" + testSegment[0] +
            " actual:" + encodeTile("/FXX-GFD-CARTE__CARTE_jpg/256_256_",
                testSegment[3], testSegment[1], testSegment[2]));
    }
}
function decimalDegreeToRad(angle) {
    return Math.PI * angle / 180;
}
function radianToDecimalDegree(angle) {
    return angle * 180 / Math.PI;
}
var earth_radius_milimeters = 6378137;
function googleToGeoTileY(googleY, _zoom) {
    var zoom = googleToGeoZoom(_zoom);
    var geoYTile = -earth_radius_milimeters * ratios[zoom][1] / 256 / global_precision - googleY;
    return Math.floor(geoYTile);
}
function EquirectangularProjection() {
    this.R = earth_radius_milimeters;
    this.phi0 = decimalDegreeToRad(46.5);
    this.lam0 = decimalDegreeToRad(3);
    this.projectionFactor = (this.R * Math.cos(this.phi0));
}
var global_precision = 0.01;
EquirectangularProjection.prototype = new GProjection();
EquirectangularProjection.prototype.fromLatLngToPixel = function(latlong, _zoom) {
    log("{fromLatLngToPixel " + latlong + ' zoom:' + zoom);
    var zoom = googleToGeoZoom(_zoom);
    var x = this.R * decimalDegreeToRad(latlong.lng()) * Math.cos(this.phi0);
    var y = this.R * decimalDegreeToRad(latlong.lat());
    log("fromLatLngToPixel realX:" + x + " realY:" + y);
    var pixX = x * ratios[zoom][0] / global_precision;
    var pixY = -(this.R - y) * ratios[zoom][1] / global_precision;
    var result = new GPoint(Math.round(pixX), Math.round(pixY));
    log("fromLatLngToPixel scalex:" + ratios[zoom][0] + ' scaley:' + ratios[zoom][1] + " -> " + result);
    log("}fromLatLngToPixel -> " + result);
    return result;
};
EquirectangularProjection.prototype.fromPixelToLatLng = function(pix, _zoom, bounded) {
    var zoom = googleToGeoZoom(_zoom);
    var lng = radianToDecimalDegree((pix.x / ratios[zoom][0] * global_precision) / this.projectionFactor);
    var correctedY = this.R + (pix.y / ratios[zoom][1] * global_precision);
    var lat = radianToDecimalDegree(correctedY / this.R);
    var result = new GLatLng(lat, lng, bounded);
    log("fromPixelToLatLng: " + pix + " zoom:" + _zoom + " -> " + result);
    return result;
};
EquirectangularProjection.prototype.tileCheckRange = function(tile, zoom, tileSize) {
    log("tileCheckRange " + tile + ' zoom:' + zoom + ' tileSize:' + tileSize);
    if (!isNaN(tile.x) && !isNaN(tile.y))
        return true;
    else
        log("nan :fou:");
}
function testProjection() {
    var projection = new EquirectangularProjection();
    var input = new GLatLng(46.980252, 2.548828);
    var pix = projection.fromLatLngToPixel(input, 11);
    log("test result:" + projection.fromPixelToLatLng(pix, 11) + " expected:" + input);
}
function googleToGeoZoom(googleZoom) {
    var z = 17 - googleZoom;
    return z > 0 ? z < ratios.length ? z : ratios.length - 1 : 1;
}
function createGeoMapType() {
    ensureCookies();
    testProjection();
    var copyCollection = new GCopyrightCollection('Geo');
    var copyright = new GCopyright(1, new GLatLngBounds(new GLatLng(-90, -180), new GLatLng(90, 180)), 0, "© IGN");
    copyCollection.addCopyright(copyright);
    var tilelayers = [new GTileLayer(copyCollection, 0, 16)];
    tilelayers[0].getTileUrl = CustomGetTileUrl;
    function CustomGetTileUrl(pt, zoom) {
        ensureCookies();
        return "http://visu-2d.geoportail.fr/geoweb/maps"
                + encodeTile("/FXX-GFD-CARTE__CARTE_jpg/256_256_", googleToGeoZoom(zoom), pt.x, googleToGeoTileY(pt.y, zoom))
                + ".jpg";
    }
    var projection = new EquirectangularProjection();
    return new GMapType(tilelayers, projection, "Geo", {errorMessage:"No geo data available", textColor:"black"});
}
var lastTimeCookieChecked;
/**
 * the portal seems to mandate to have a cookie to get the tiles. the cookie is set after a strange redirection when querying http://www.geoportail.fr/imgs/visu/empty.gif.
 * it expires quick, so we re-query the url every minute or so.
 */
function ensureCookies() {
    function fetchFakeImage() {
        var img = $('lolToken');
        if (img == null) {
            //style='visibility:hidden'
            $('body').insert("<img id='lolToken' src='' alt=''>");
            return $('lolToken');
        } else
            return img;
    }
    var now = new Date().getTime() / 1000;
    if (lastTimeCookieChecked == null || now - lastTimeCookieChecked > 60) {
        fetchFakeImage().src = "http://www.geoportail.fr/imgs/visu/empty.gif?random=" + Math.random();
        lastTimeCookieChecked = now;
    }
}