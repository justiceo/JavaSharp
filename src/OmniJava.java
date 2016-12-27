import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justice on 12/20/2016.
 */
public class OmniJava {

    public static void main(String[] args) throws Exception {
        // creates an input stream for the file to be parsed
        FileInputStream in = new FileInputStream("D:\\Code\\IdeaProjects\\Omni-java\\commons-lang\\StringUtils.java");

        // parse the file
        CompilationUnit cu = JavaParser.parse(in);


        refactorVisitor(cu);
        String cuStr = performRegexTransforms(printVisitor(cu));

        // prints the resulting compilation unit to default system output
        System.out.println(cuStr);

    }

    private static String performRegexTransforms(String cuStr) {
        cuStr = cuStr.replaceAll("\\bString\\b", "string"); // since String is a keyword, this is save
        cuStr = cuStr.replaceAll("\\btoString()\\b", "ToString");
        return cuStr;
    }

    private static String printVisitor(CompilationUnit cu) {
        SharpVisitor dv = new SharpVisitor();
        cu.accept(dv, null);
        return dv.getSource();
    }

    private static void refactorVisitor(CompilationUnit cu) {
        TagVisitor dv = new TagVisitor();
        cu.accept(dv, null);
    }

    /**
     * Simple visitor implementation for visiting MethodDeclaration nodes.
     */
    private static class MethodVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(MethodDeclaration n, Void arg) {
            /* here you can access the attributes of the method.
             this method will be called for all methods in this
             CompilationUnit, including inner class methods */
            System.out.println(n.getName());
            super.visit(n, arg);
        }
    }

    /**
     * Simple visitor implementation for visiting MethodDeclaration nodes.
     */
    private static class MethodChangerVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(MethodDeclaration n, Void arg) {
            // change the name of the method to upper case
            n.setName(n.getName().toUpperCase());

            // add a new parameter to the method
            n.setParameters(new ArrayList<Parameter>());

            inlineOverride(n);
        }

        private void inlineOverride(MethodDeclaration n) {
            List<AnnotationExpr> overrides = n.getAnnotations();
            for(AnnotationExpr a: overrides) {
                if(a.getName().toString().equals("Override")) {
                    int x = n.getModifiers();
                    System.out.println("found override: " + x);
                }
                else {
                    System.out.println("other an: " + a.getName());
                }
            }
        }
    }


}
