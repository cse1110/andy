package unit.codechecker;

import nl.tudelft.cse1110.andy.codechecker.checks.Check;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;

import java.util.Map;

public class JDTParser {

    public void run(String file, Check check) {
        ASTParser parser = ASTParser.newParser(AST.JLS15);
        parser.setResolveBindings(true);
        parser.setBindingsRecovery(true);

        Map<String, String> options = JavaCore.getOptions();
        JavaCore.setComplianceOptions(JavaCore.VERSION_15, options);
        parser.setCompilerOptions(options);

        parser.setEnvironment(new String[0], new String[0], null, true);
        parser.createASTs(new String[] { file }, null, new String[0], new FileASTRequestor() {
            public void acceptAST(String sourceFilePath, CompilationUnit cu) {
                check.check(cu);
            }
        }, null);
    }

}
