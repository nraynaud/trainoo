//ratio / GLOBAL_PRECISION * EARTH_RADIUS_METERS
var ratios = [undefined, 127346247.5181519, 63673123.75907608, 31836561.87953804, 12734625.312202323,
    6367312.6561011495, 2547194.608622496, 1273597.304311248, 636815.5075480487, 509449.68290750677,
    254724.8414537537, 127362.42072687685, 63675.98956981053, 31840.523093769087, 15920.271123812016,
    7960.135561906038, 3980.067780953013, 1990.0338904765065, 995.0169452382532, 497.5084726191266, 248.7542363095633,
    124.37711815478133, 124.37635926878457, 124.37570726813895, 124.37506595602825];
var IGN_PHOTO_KEY = "UxG";
var IGN_MAP_KEY = "8u6";
function log(txt) {
    if ("console" in window && "firebug" in console)
        console.log(txt);
}
function degreeToRad(angle) {
    return Math.PI * angle / 180;
}
function radianToDegree(angle) {
    return angle * 180 / Math.PI;
}
function northPoleTile(geoZoom) {
    return Math.ceil(ratios[geoZoom] / 256);
}
function googleToGeoTileY(googleY, geoZoom) {
    return Math.floor(northPoleTile(geoZoom) - googleY);
}
// corrects  the north pole tile shift
//this is an involution function, as long as you stay in the north hemisphere. DON'T TRY THIS UNDER THE EQUATOR !
function convertYPixel(y, zoom) {
    return (northPoleTile(zoom) + 1) * 256 - y;
}
function GeoProjection() {
    this.lngProjectionFactor = (Math.cos(degreeToRad(46.5)));
}
GeoProjection.prototype = new GProjection();
GeoProjection.prototype.fromLatLngToPixel = function(latlong, _zoom) {
    var geoZoom = googleToGeoZoom(_zoom);
    var pixX = degreeToRad(latlong.lng()) * this.lngProjectionFactor * ratios[geoZoom] ;
    var pixY = degreeToRad(latlong.lat()) * ratios[geoZoom] ;
    return new GPoint(pixX, convertYPixel(pixY, geoZoom));
};
GeoProjection.prototype.fromPixelToLatLng = function(pix, _zoom, bounded) {
    var geoZoom = googleToGeoZoom(_zoom);
    var lng = radianToDegree((pix.x / ratios[geoZoom] ) / this.lngProjectionFactor);
    var lat = radianToDegree(convertYPixel(pix.y, geoZoom) / ratios[geoZoom]);
    return new GLatLng(lat, lng, bounded);
};
/**
 * avoids using Infinity because it creates a bug in overlays !
 */
GeoProjection.prototype.getWrapWidth = function() {
    return 99999999999999;
}
function createMapType(key, textColor, maxZoom) {
    var copyCollection = new GCopyrightCollection('© ');
    var earth = new GLatLngBounds(new GLatLng(-90, -180), new GLatLng(90, 180));
    var copyIndex = 1;
    function addCopyright(text) {
        var copyright = new GCopyright(copyIndex++, earth, 0, text);
        copyCollection.addCopyright(copyright);
    }
    addCopyright("<a class='copyright' href='http://www.ign.fr'>IGN</a>");
    addCopyright("<a class='copyright' href='http://www.spotimage.fr'>SpotImage</a>");
    addCopyright("<a class='copyright' href='http://www.cnes.fr'>CNES</a>");
    addCopyright("<a class='copyright' href='http://www.planetobserver.com'>PlanetObserver</a>");
    function createLayer(key) {
        var layer = new GTileLayer(copyCollection, 5, maxZoom);
        layer.getTileUrl = function (pt, zoom) {
            var geoZoom = googleToGeoZoom(zoom);
            return "/track/getTile?p=" + key + "&z=" + geoZoom + "&x=" + pt.x + "&y=" + googleToGeoTileY(pt.y, geoZoom)
                    + "&s=.jpg";
        }
        return layer;
    }
    return new GMapType([createLayer(key)], new GeoProjection(), "IGN", {
        errorMessage:"Aucune donnée disponible", textColor:textColor});
}
function googleToGeoZoom(googleZoom) {
    var z = 22 - googleZoom;
    return z > 0 ? z < ratios.length ? z : ratios.length - 1 : 1;
}
function createGeoMapType(prefix, textColor, maxZoom) {
    testProjection();
    return new createMapType(prefix, textColor, maxZoom);
}
var lastTimeCookieChecked;
/******some tests ******/
function testProjection() {
    var projection = new GeoProjection();
    var input = new GLatLng(46.980252, 2.548828);
    log("test result:" + projection.fromPixelToLatLng(projection.fromLatLngToPixel(input, 11), 11) + " expected:" + input);
}