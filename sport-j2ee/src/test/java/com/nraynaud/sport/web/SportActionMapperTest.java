package com.nraynaud.sport.web;

import com.opensymphony.xwork2.config.entities.PackageConfig;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.HashMap;

public class SportActionMapperTest {

    @Test
    public void testNaspaceExtraction() {
        final HashMap<String, PackageConfig> map = new HashMap<String, PackageConfig>();
        config(map, "lol", "");
        config(map, "lol2", "/");
        config(map, "lol3", "/");
        config(map, "lol4", "/lol");
        config(map, "lol5", "/lol/pop");
        checkExtract(map, "/", "pouet", "index", "/pouet");
        checkExtract(map, "/lol", "pouet", "index", "/lol/pouet");
        checkExtract(map, "/lol/pop", "pouet", "index", "/lol/pop/pouet");
        checkExtract(map, "/lol", "poppouet", "index", "/lol/poppouet");
        checkExtract(map, "/lol", "poppouet", "new", "/lol/poppouet/new");
    }

    private static void checkExtract(final HashMap<String, PackageConfig> map,
                                     final String expectedNS,
                                     final String expectedName,
                                     final String method,
                                     final String testString) {
        final ActionMapping mapping = new ActionMapping();
        SportActionMapper.extractNamespaceAndName(testString, map, mapping, "GET");
        assertEquals(expectedNS, mapping.getNamespace());
        assertEquals(expectedName, mapping.getName());
        assertEquals(method, mapping.getMethod());
    }

    private static void config(final HashMap<String, PackageConfig> map, final String name, final String namespace) {
        map.put(name, new PackageConfig(name, namespace, false));
    }
}
