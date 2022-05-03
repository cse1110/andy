package nl.tudelft.cse1110.andy.utils;

import java.util.HashMap;
import java.util.Map;

public class FromBytesClassLoader extends ClassLoader {
    private final Map<String, byte[]> definitions = new HashMap<>();

    public FromBytesClassLoader(ClassLoader cleanClassloader) {
        super(cleanClassloader);
    }

    public void addDefinition(final String name, final byte[] bytes) {
        definitions.put(name, bytes);
    }

    /**Method that is called by Java when it looks for a class. If the class is one that we added in the code
     * it will load it from the bytes we supplied. Otherwise it will search in Java itself for the class.
     *
     * @param name - the name of the class to find
     * @param resolve - some boolean
     * @return - the loaded class
     * @throws ClassNotFoundException - if the class was not found anywhere
     */
    @Override
    protected Class<?> loadClass(final String name, final boolean resolve)
            throws ClassNotFoundException {
        final byte[] bytes = definitions.get(name);
        if (bytes != null) {
            return defineClass(name, bytes, 0, bytes.length);
        }
        return super.loadClass(name, resolve);
    }
}
