package nl.tudelft.cse1110.andy.utils;

import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class ClassUtils {

    /**
     * Extracts the class name out of the full class name
     * e.g., a.b.c.D return D
     * @param newClassName the full qualified class name
     * @return the class name
     */
    public static String extractClassName(String newClassName) {
        String[] packageAndClassName = newClassName.split("\\.");
        return packageAndClassName[packageAndClassName.length-1];
    }

    /**
     * Gets the name of the directory where a class is supposed to be
     * based on its package name
     * @param newClassName full qualified class name
     * @return directory
     */
    public static String extractDirectoryName(String newClassName) {
        List<String> packagesAndClass = new ArrayList<>(Arrays.asList(newClassName.split("\\.")));
        packagesAndClass.remove(packagesAndClass.size() - 1);

        // TODO: '/' is OS dependant
        return packagesAndClass.stream().collect(Collectors.joining("/"));
    }

    public static String packageToDirectory(String packageName) {
        // TODO: '/' is OS dependant
        return packageName.replace(".", "/");
    }

    /**
     * Receives the list of jar files and puts them in a classpath format
     *
     * @param libraries the list of libraries
     * @return string in the format of "/dir/dir/lib1.jar:/dir/dir/lib2.jar..."
     */
    public static String asClassPath(List<String> libraries) {
        return libraries.stream().collect(Collectors.joining(classSeparator()));
    }

    /**
     * Finds the test class using simple convention.
     * Lots to improve here. In particular, what happens if there are two classes
     * that match?
     *
     * @param listOfClasses the list of classes to find the test class
     * @return the name of the test class
     * @throws NoSuchElementException if there are no test classes
     */
    public static String getTestClass(List<String> listOfClasses) {
        String className = listOfClasses.stream().filter(c -> c.contains("Test"))
                    .findFirst()
                    .get();

        return className;
    }

    /**Finds the configuration class.
     *
     * @param listOfClasses - the list of classes to search in
     * @return - the name of the class
     */
    public static String getConfigurationClass(List<String> listOfClasses) {
        String className = listOfClasses.stream().filter(c -> c.contains("Config"))
                .findFirst()
                .get();

        return className;
    }

    /**
     * Finds all the classes, but the testing class one.
     * @param listOfClasses list of classes
     * @return list of classes without the testing class
     */
    public static List<String> allClassesButTestingAndConfigOnes(List<String> listOfClasses) {
        return listOfClasses.stream().filter(c -> !c.contains("Test") && !c.contains("Config"))
                .collect(Collectors.toList());
    }


    /**
     * Quite naive way to do it. But it's pretty fast.
     *
     * @param content java source code
     * @return package name
     */
    public static String extractPackageName(String content) {
        Pattern p = Pattern.compile("(package (.*);)");
        Matcher m = p.matcher(content);

        if(m.find()) {
            return m.group(2);
        }

        throw new RuntimeException("Package name not found!");
    }

    /**
     * The method determines what class separator should be used depending on the OS.
     * ";" for Windows or ":" for Mac/Linux.
     *
     * @return string for class separator
     */
    public static String classSeparator() {
        if (SystemUtils.IS_OS_WINDOWS) {
            return ";";
        }
        return ":";
    }

    /**
     * Get the name of the .class file.
     * If the full name contains a subclass, we should replace
     * the dot (.) by a ($), so that we find the file.
     *
     * @param name the full name
     * @return the full name with subclasses using $
     */
    public static String clazzNameAsPath(String name) {
        String[] parts = name.split("\\.");

        StringBuilder finalName = new StringBuilder();
        for(int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if(i>0) {
                String previousPart = parts[i-1];
                if(Character.isUpperCase(previousPart.charAt(0)))
                    finalName.append("$");
                else
                    finalName.append("/");
            }

            finalName.append(part);
        }

        finalName.append(".class");
        return finalName.toString();
    }

}
