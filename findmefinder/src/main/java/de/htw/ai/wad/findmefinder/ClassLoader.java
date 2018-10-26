package de.htw.ai.wad.findmefinder;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassLoader {
    public static Class loadClass(String classFilePath, String className) throws ClassNotFoundException, MalformedURLException, FileNotFoundException {
        File file = new File(classFilePath);
        if (!file.isFile()) {
            throw(new FileNotFoundException("Classfile " + classFilePath + " not found!"));
        }
        else{
            URL[] classUrls = { file.toURI().toURL() };
            URLClassLoader classLoader = new URLClassLoader(classUrls);
            return classLoader.loadClass(className);
        }
    }
}
