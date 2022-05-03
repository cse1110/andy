package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.result.ResultBuilder;
import nl.tudelft.cse1110.andy.utils.FilesUtils;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementScanner9;
import javax.tools.*;
import java.io.File;
import java.util.*;

import static nl.tudelft.cse1110.andy.utils.ClassUtils.asClassPath;

/**
 * This step compiles the student code and the library code.
 * It makes use of the Java Compiler API.
 */
public class CompilationStep implements ExecutionStep {


    @Override
    public void execute(Context ctx, ResultBuilder result) {
        DirectoryConfiguration dirCfg = ctx.getDirectoryConfiguration();

        /*
         * creates the java compiler and diagnostic collector object
         * using just the standard configuration. Nothing to optimize here, I believe.
         */
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager manager = compiler.getStandardFileManager(
                diagnostics, null, null );

        /*
         * Create a compilation task with the list of files to compile.
         * The compiler uses the classpath of the current JVM.
         * So, all the libraries available (JUnit, etc) will be reused here
         */
        Collection<File> listOfFiles = FilesUtils.getAllJavaFiles(dirCfg.getWorkingDir());

        Iterable<? extends JavaFileObject > sources =
                manager.getJavaFileObjectsFromFiles(listOfFiles);

        ClassNameScanner scanner = new ClassNameScanner();
        ClassNameProcessor processor = new ClassNameProcessor(scanner);

        List<String> options = ctx.hasLibrariesToBeIncluded() ? Arrays.asList(new String[] { "-cp", asClassPath(ctx.getLibrariesToBeIncluded()) }) : null;
        JavaCompiler.CompilationTask task = compiler.getTask(null, manager, diagnostics, options, null, sources);
        task.setProcessors(Arrays.asList(processor));

        /*
         * Compiles. The .class files will be saved in the same directory
         * as the java files.
         */
        try {
            boolean compilationResult = task.call();

            if(compilationResult) {
                ctx.setNewClassNames(scanner.getFullClassNames());
                result.compilationSuccess();
            }
            else {
                result.compilationFail(diagnostics.getDiagnostics());
            }

            manager.close();
        } catch(Exception e) {
            result.genericFailure(this, e);
        }
    }

    @SupportedSourceVersion(SourceVersion.RELEASE_11)
    @SupportedAnnotationTypes("*")
    public class ClassNameProcessor extends AbstractProcessor {
        private final ClassNameScanner scanner;

        public ClassNameProcessor(ClassNameScanner scanner) {
            this.scanner = scanner;
        }

        public boolean process(final Set< ? extends TypeElement> types,
                               final RoundEnvironment environment) {

            if(!environment.processingOver()) {
                for(final Element element: environment.getRootElements()) {
                    scanner.scan(element);
                }
            }

            return true;
        }
    }

    public class ClassNameScanner extends ElementScanner9< Void, Void > {
        private List<String> fullClassNames = new ArrayList<>();

        public Void visitType(final TypeElement type, final Void p) {
            fullClassNames.add(type.getQualifiedName().toString());
            return super.visitType(type, p);
        }

        public List<String> getFullClassNames() {
            return Collections.unmodifiableList(fullClassNames);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof CompilationStep;
    }

}