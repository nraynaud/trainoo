package com.nraynaud.sport.web;

import com.opensymphony.xwork2.config.entities.PackageConfig;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import static java.util.Collections.singletonMap;
import java.util.HashMap;
import java.util.Map;

public class SportActionMapperTest {

    @Test
    public void testNaspaceExtraction() {
        final HashMap<String, PackageConfig> map = new HashMap<String, PackageConfig>();
        config(map, "lol", "");
        config(map, "lol2", "/");
        config(map, "lol3", "/");
        config(map, "lol4", "/lol");
        config(map, "lol5", "/lol/pop");
        checkExtract(map, "/", "pouet", "index", null, "/pouet");
        checkExtract(map, "/lol", "pouet", "index", null, "/lol/pouet");
        checkExtract(map, "/lol/pop", "pouet", "index", null, "/lol/pop/pouet");
        checkExtract(map, "/lol", "poppouet", "index", null, "/lol/poppouet");
        checkExtract(map, "/lol", "poppouet", "new", null, "/lol/poppouet/new");
        checkExtract(map, "/lol", "poppouet", "edit", singletonMap("id", "1"), "/lol/poppouet/edit/1");
    }

    @Test
    public void testURICreation() {
    }

    private static void checkExtract(final HashMap<String, PackageConfig> map,
                                     final String expectedNS,
                                     final String expectedName,
                                     final String method,
                                     final Map<String, String> params,
                                     final String url) {
        final ActionMapping mapping = new ActionMapping();
        SportActionMapper.extractNamespaceAndName(url, map, mapping, "GET");
        assertEquals(expectedNS, mapping.getNamespace());
        assertEquals(expectedName, mapping.getName());
        assertEquals(method, mapping.getMethod());
        assertEquals(params, mapping.getParams());

        final String recreatedUri = new SportActionMapper().getUriFromActionMapping(mapping);
        assertEquals(url, recreatedUri);
    }

    private static void config(final HashMap<String, PackageConfig> map, final String name, final String namespace) {
        map.put(name, new PackageConfig(name, namespace, false));
    }
}
