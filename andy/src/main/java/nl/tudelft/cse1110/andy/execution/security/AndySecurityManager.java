package nl.tudelft.cse1110.andy.execution.security;

import java.io.FilePermission;
import java.lang.reflect.ReflectPermission;
import java.net.NetPermission;
import java.net.SocketPermission;
import java.net.URLPermission;
import java.security.Permission;
import java.security.SecurityPermission;
import java.util.PropertyPermission;
import java.util.logging.LoggingPermission;

public class AndySecurityManager extends SecurityManager {
    public AndySecurityManager() {
        super();
    }

    @Override
    public void checkPermission(Permission perm) {
        /*
         * Determine whether the current code should be trusted based on the package name.
         * If the code is trusted, all permissions are automatically granted.
         * As the solution file appears in the stacktrace of certain internal JDK methods as well as Mockito,
         * check if this is the case and grant some more dangerous permissions.
         */
        boolean untrusted = false;
        boolean mockitoInternal = false;
        boolean seleniumInternal = false;
        boolean databaseConnection = false;
        boolean jdkInternalLoader = false;
        for (StackTraceElement elem : Thread.currentThread().getStackTrace()) {
            if (elem.getClassName().startsWith("org.mockito.internal")) mockitoInternal = true;
            if (elem.getClassName().startsWith("org.openqa.selenium")) seleniumInternal = true;
            if (elem.getClassName().startsWith("java.sql.DriverManager")) databaseConnection = true;
            if (elem.getClassName().startsWith("jdk.internal.") && !untrusted) jdkInternalLoader = true;
            if (elem.getClassName().startsWith("delft.")) untrusted = true;
        }

        /*
         * If this method returns without throwing an exception, the requested permission will be granted.
         * If a SecurityException is thrown, it will be denied.
         */

        // If the code is trusted, grant all permissions
        if (!untrusted) {
            return;
        }

        // Determine if the currently requested permission should be granted
        if (checkPermissionsUntrusted(perm, mockitoInternal, seleniumInternal, jdkInternalLoader, databaseConnection)) return;

        // If the currently requested permission should not be allowed,
        // throw an exception to block the execution
        throw new SecurityException(
                String.format("Operation not permitted: %s name=%s actions=%s mockito=%b jdk=%b",
                        perm.getClass().getName(),
                        perm.getName(),
                        perm.getActions(),
                        mockitoInternal,
                        jdkInternalLoader
                ));
    }

    private boolean checkPermissionsUntrusted(Permission perm, boolean mockitoInternal, boolean seleniumInternal,
                                              boolean jdkInternalLoader, boolean databaseConnection) {
        if (checkFilePermission(perm, mockitoInternal, jdkInternalLoader)) return true;

        if (checkPropertyPermission(perm)) return true;

        if (checkRuntimePermission(perm, mockitoInternal, seleniumInternal, databaseConnection)) return true;

        if (checkLoggingPermission(perm)) return true;

        if (checkReflectPermission(perm)) return true;

        if (checkSecurityPermission(perm)) return true;

        if (checkNetworkPermissions(perm)) return true;

        return false;
    }

    private boolean checkNetworkPermissions(Permission perm) {
        // Allow network access as it is needed for Selenium tests
        // External connections are not blocked here as they are prevented via other means
        if (perm instanceof NetPermission || perm instanceof URLPermission || perm instanceof SocketPermission) {
            return true;
        }
        return false;
    }

    private boolean checkSecurityPermission(Permission perm) {
        // Allow property access as it is needed in order to execute the tests
        if (perm instanceof SecurityPermission) {
            if (perm.getName().startsWith("putProviderProperty.") ||
                perm.getName().startsWith("getProperty.")) {
                return true;
            }
        }
        return false;
    }

    private boolean checkReflectPermission(Permission perm) {
        // Allow overriding access checks via reflection as it is needed in order to execute the tests
        // Using reflection in solution code is blocked via other means
        if (perm instanceof ReflectPermission) {
            if (perm.getName().equals("suppressAccessChecks")) {
                return true;
            }
        }
        return false;
    }

    private boolean checkLoggingPermission(Permission perm) {
        // Allow log control permissions as they are needed in order to execute the tests
        if (perm instanceof LoggingPermission) {
            if (perm.getName().equals("control")) {
                return true;
            }
        }
        return false;
    }

    private boolean checkRuntimePermission(Permission perm, boolean mockitoInternal, boolean seleniumInternal, boolean databaseConnection) {
        // Allow various runtime permissions depending on the context as they are needed in order to execute the tests
        if (perm instanceof RuntimePermission) {
            if (mockitoInternal && checkMockitoInternalRuntimePermission(perm)
                || seleniumInternal && checkSeleniumRuntimePermissions(perm)
                || databaseConnection && checkDatabaseConnectionRuntimePermissions(perm)
                || checkNormalRuntimePermissions(perm)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkSeleniumRuntimePermissions(Permission perm) {
        // Grant permissions necessary for Selenium to run correctly
        // The "modifyThread" RuntimePermission grants Selenium access to methods for controlling threads,
        // which are necessary for its operation.
        // The HOSTNAME and COMPUTERNAME environment variables are necessary for Selenium exceptions
        return perm.getName().equals("modifyThread")
               || perm.getName().equals("getenv.HOSTNAME")
               || perm.getName().equals("getenv.COMPUTERNAME");
    }

    private boolean checkDatabaseConnectionRuntimePermissions(Permission perm) {
        // Grant permissions necessary for Selenium to run correctly
        // These RuntimePermissions are necessary to load the driver correctly.
        return perm.getName().equals("accessClassInPackage.jdk.internal.reflect")
               || perm.getName().equals("shutdownHooks");
    }

    private boolean checkNormalRuntimePermissions(Permission perm) {
        return perm.getName().equals("loggerFinder")
               || perm.getName().equals("createClassLoader")
               || perm.getName().equals("createSecurityManager")
               || perm.getName().equals("accessSystemModules")
               || checkReflectionRuntimePermissions(perm)
               || checkNetworkRuntimePermissions(perm);
    }

    private boolean checkReflectionRuntimePermissions(Permission perm) {
        return perm.getName().equals("getProtectionDomain")
               || perm.getName().equals("accessDeclaredMembers")
               || perm.getName().equals("getClassLoader");
    }

    private boolean checkNetworkRuntimePermissions(Permission perm) {
        return perm.getName().equals("selectorProvider")
               || perm.getName().equals("accessClassInPackage.sun.net")
               || perm.getName().equals("loadLibrary.extnet")
               || perm.getName().startsWith("accessClassInPackage.sun.security.");
    }

    private boolean checkMockitoInternalRuntimePermission(Permission perm) {
        // Allow those permissions only if they are requested by internal Mockito classes
        return perm.getName().equals("accessSystemModules") ||
               perm.getName().equals("accessClassInPackage.sun.misc") ||
               perm.getName().startsWith("accessClassInPackage.") ||
               perm.getName().equals("reflectionFactoryAccess") ||
               perm.getName().equals("localeServiceProvider"); // Required when running via Docker
    }

    private boolean checkPropertyPermission(Permission perm) {
        if (perm instanceof PropertyPermission) {
            if (perm.getActions().equals("read") ||
                perm.getName().equals("*")) {
                return true;
            }
        }
        return false;
    }

    private boolean checkFilePermission(Permission perm, boolean mockitoInternal, boolean jdkInternalLoader) {
        // Allow reading certain files based on their path and extension.
        // This is needed in order to execute the tests.
        // Block writing or executing files.
        if (perm instanceof FilePermission) {
            boolean isAccessToMavenLibrary = perm.getName().contains("/repository/");
            boolean isAccessToFileInTargetFolder = perm.getName().contains("/target/");
            boolean mayBePathTraversalAttempt = perm.getName().contains("..");
            if (perm.getActions().equals("read") &&
                !perm.getName().endsWith(".java") &&
                (
                        checkPrivilegedFilePermission(perm, mockitoInternal, jdkInternalLoader) ||
                        (isAccessToMavenLibrary || isAccessToFileInTargetFolder)
                )
                && !mayBePathTraversalAttempt) {
                return true;
            }
        }
        return false;
    }

    private boolean checkPrivilegedFilePermission(Permission perm, boolean mockitoInternal, boolean jdkInternalLoader) {
        return (
                       checkFilePermissionAllowedExtensions(perm) ||
                       checkFilePermissionAllowedPaths(perm)
               )
               && (mockitoInternal || jdkInternalLoader);
    }

    private boolean checkFilePermissionAllowedExtensions(Permission perm) {
        return perm.getName().endsWith(".jar") ||
               perm.getName().endsWith(".class") ||
               perm.getName().endsWith("net.properties");
    }

    private boolean checkFilePermissionAllowedPaths(Permission perm) {
        return perm.getName().contains("META-INF") ||
               perm.getName().contains("jdk") ||
               perm.getName().contains("jre") ||
               perm.getName().contains("jvm") ||
               perm.getName().contains("mockito");
    }

    @Override
    public void checkPermission(Permission perm, Object context) {
        this.checkPermission(perm);
    }
}
