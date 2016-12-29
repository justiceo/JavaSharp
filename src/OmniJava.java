import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.FileInputStream;

/**
 * Created by Justice on 12/20/2016.
 */
public class OmniJava {

    public static void main(String[] args) throws Exception {
        // creates an input stream for the file to be parsed
        FileInputStream in = new FileInputStream("D:\\Code\\IdeaProjects\\Omni-java\\commons-lang\\ArrayUtils.java");

        // parse the file
        CompilationUnit cu = JavaParser.parse(in);

        RefactorVisitor.SetBasePackage("org.apache.commons.lang3", "CommonsLang");
        RefactorVisitor rv = new RefactorVisitor();
        cu.accept(rv, null);

        //printTaggedCu(cu);
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
        CSharpPrintVisitor.UseJavaDoc = true;
        CSharpPrintVisitor dv = new CSharpPrintVisitor();
        cu.accept(dv, null);
        return dv.getSource();
    }

    private static void printTaggedCu(CompilationUnit cu) {
        TagVisitor dv = new TagVisitor();
        CompilationUnit clone = (CompilationUnit) cu.clone();
        clone.accept(dv, null);
        System.out.println(clone.toString());
    }
}
