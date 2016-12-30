import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.TypeParameter;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;

/**
 * Created by Justice on 12/27/2016.
 */
public class RefactorVisitor extends VoidVisitorAdapter<Object> {

    private final HashSet<String> reserved;
    private static String oldSuffix = "";
    private static String newSuffix = "";

    public RefactorVisitor() throws IOException, URISyntaxException {
        URI uri = getClass().getResource("/reserved_keywords.txt").toURI();
        reserved = new HashSet<>(Files.readAllLines(Paths.get(uri)));
    }

    @Override public void visit(final PackageDeclaration n, final Object arg) {
        String name = n.getName().toString().replace(this.oldSuffix, this.newSuffix);
        //name = toPackageTitleCase(name);
        n.setName(new NameExpr(n.getRange(), name));
        visitComment(n.getComment(), arg);
        if (n.getAnnotations() != null) {
            for (final AnnotationExpr a : n.getAnnotations()) {
                a.accept(this, arg);
            }
        }
    }

    @Override public void visit(final ImportDeclaration n, final Object arg) {
        String name = n.getName().toString().replace(this.oldSuffix, this.newSuffix);
        //name = toPackageTitleCase(name);
        n.setName(new NameExpr(n.getRange(), name));
        visitComment(n.getComment(), arg);
    }

    @Override public void visit(final NameExpr n, final Object arg) {
        if(reserved.contains(n.getName())) n.setName(hash(n.getName()));
        visitComment(n.getComment(), arg);
    }

    @Override public void visit(final QualifiedNameExpr n, final Object arg) {
        if(reserved.contains(n.getName())) n.setName(hash(n.getName()));
        visitComment(n.getComment(), arg);
        n.getQualifier().accept(this, arg);
    }

    @Override public void visit(final ClassOrInterfaceDeclaration n, final Object arg) {
        if(reserved.contains(n.getName())) n.setName(hash(n.getName()));
        visitComment(n.getComment(), arg);
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        for (final AnnotationExpr a : n.getAnnotations()) {
            a.accept(this, arg);
        }
        //n.getNameExpr().accept(this, arg);
        for (final TypeParameter t : n.getTypeParameters()) {
            t.accept(this, arg);
        }
        for (final ClassOrInterfaceType c : n.getExtends()) {
            c.accept(this, arg);
        }
        for (final ClassOrInterfaceType c : n.getImplements()) {
            c.accept(this, arg);
        }
        for (final BodyDeclaration member : n.getMembers()) {
            member.accept(this, arg);
        }
    }

    @Override public void visit(final ClassOrInterfaceType n, final Object arg) {
        if(reserved.contains(n.getName())) n.setName(hash(n.getName()));
        visitComment(n.getComment(), arg);
        if (n.getScope() != null) {
            n.getScope().accept(this, arg);
        }
        if (n.getTypeArgs() != null) {
            for (final Type t : n.getTypeArgs()) {
                t.accept(this, arg);
            }
        }
    }

    @Override public void visit(final TypeParameter n, final Object arg) {
        if(reserved.contains(n.getName())) n.setName(hash(n.getName()));
        visitComment(n.getComment(), arg);
        if (n.getTypeBound() != null) {
            for (final ClassOrInterfaceType c : n.getTypeBound()) {
                c.accept(this, arg);
            }
        }
    }

    @Override public void visit(final VariableDeclaratorId n, final Object arg) {
        if(reserved.contains(n.getName())) n.setName(hash(n.getName()));
        visitComment(n.getComment(), arg);
    }

    @Override public void visit(final MethodCallExpr n, final Object arg) {
        if(reserved.contains(n.getName())) n.setName(hash(n.getName()));
        visitComment(n.getComment(), arg);
        if (n.getScope() != null) {
            n.getScope().accept(this, arg);
        }
        if (n.getTypeArgs() != null) {
            for (final Type t : n.getTypeArgs()) {
                t.accept(this, arg);
            }
        }
        //n.getNameExpr().accept(this, arg);
        if (n.getArgs() != null) {
            for (final Expression e : n.getArgs()) {
                e.accept(this, arg);
            }
        }
    }

    @Override public void visit(final ConstructorDeclaration n, final Object arg) {
        if(reserved.contains(n.getName())) n.setName(hash(n.getName()));
        visitComment(n.getComment(), arg);
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        if (n.getAnnotations() != null) {
            for (final AnnotationExpr a : n.getAnnotations()) {
                a.accept(this, arg);
            }
        }
        if (n.getTypeParameters() != null) {
            for (final TypeParameter t : n.getTypeParameters()) {
                t.accept(this, arg);
            }
        }
        //n.getNameExpr().accept(this, arg);
        if (n.getParameters() != null) {
            for (final Parameter p : n.getParameters()) {
                p.accept(this, arg);
            }
        }
        if (n.getThrows() != null) {
            for (final ReferenceType name : n.getThrows()) {
                name.accept(this, arg);
            }
        }
        n.getBlock().accept(this, arg);
    }

    @Override public void visit(final MethodDeclaration n, final Object arg) {
        if(reserved.contains(n.getName())) n.setName(hash(n.getName()));
        visitComment(n.getComment(), arg);
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        if (n.getAnnotations() != null) {
            for (final AnnotationExpr a : n.getAnnotations()) {
                a.accept(this, arg);
            }
        }
        if (n.getTypeParameters() != null) {
            for (final TypeParameter t : n.getTypeParameters()) {
                t.accept(this, arg);
            }
        }
        n.getType().accept(this, arg);
        //n.getNameExpr().accept(this, arg);
        if (n.getParameters() != null) {
            for (final Parameter p : n.getParameters()) {
                p.accept(this, arg);
            }
        }
        if (n.getThrows() != null) {
            for (final ReferenceType name : n.getThrows()) {
                name.accept(this, arg);
            }
        }
        if (n.getBody() != null) {
            n.getBody().accept(this, arg);
        }
    }

    @Override public void visit(final EnumDeclaration n, final Object arg) {
        if(reserved.contains(n.getName())) n.setName(hash(n.getName()));
        visitComment(n.getComment(), arg);
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        if (n.getAnnotations() != null) {
            for (final AnnotationExpr a : n.getAnnotations()) {
                a.accept(this, arg);
            }
        }
        //n.getNameExpr().accept(this, arg);
        if (n.getImplements() != null) {
            for (final ClassOrInterfaceType c : n.getImplements()) {
                c.accept(this, arg);
            }
        }
        if (n.getEntries() != null) {
            for (final EnumConstantDeclaration e : n.getEntries()) {
                e.accept(this, arg);
            }
        }
        if (n.getMembers() != null) {
            for (final BodyDeclaration member : n.getMembers()) {
                member.accept(this, arg);
            }
        }
    }

    @Override public void visit(final EnumConstantDeclaration n, final Object arg) {
        if(reserved.contains(n.getName())) n.setName(hash(n.getName()));
        visitComment(n.getComment(), arg);
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        if (n.getAnnotations() != null) {
            for (final AnnotationExpr a : n.getAnnotations()) {
                a.accept(this, arg);
            }
        }
        if (n.getArgs() != null) {
            for (final Expression e : n.getArgs()) {
                e.accept(this, arg);
            }
        }
        if (n.getClassBody() != null) {
            for (final BodyDeclaration member : n.getClassBody()) {
                member.accept(this, arg);
            }
        }
    }

    @Override public void visit(final AnnotationDeclaration n, final Object arg) {
        if(reserved.contains(n.getName())) n.setName(hash(n.getName()));
        visitComment(n.getComment(), arg);
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        if (n.getAnnotations() != null) {
            for (final AnnotationExpr a : n.getAnnotations()) {
                a.accept(this, arg);
            }
        }
        //n.getNameExpr().accept(this, arg);
        if (n.getMembers() != null) {
            for (final BodyDeclaration member : n.getMembers()) {
                member.accept(this, arg);
            }
        }
    }

    @Override public void visit(final AnnotationMemberDeclaration n, final Object arg) {
        if(reserved.contains(n.getName())) n.setName(hash(n.getName()));
        visitComment(n.getComment(), arg);
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        if (n.getAnnotations() != null) {
            for (final AnnotationExpr a : n.getAnnotations()) {
                a.accept(this, arg);
            }
        }
        n.getType().accept(this, arg);
        if (n.getDefaultValue() != null) {
            n.getDefaultValue().accept(this, arg);
        }
    }

    @Override public void visit(final MemberValuePair n, final Object arg) {
        if(reserved.contains(n.getName())) n.setName(hash(n.getName()));
        visitComment(n.getComment(), arg);
        n.getValue().accept(this, arg);
    }

    private void visitComment(final Comment n, final Object arg) {
        if (n != null) {
            n.accept(this, arg);
        }
    }

    private String hash(NameExpr nameExpr) {
        return hash(nameExpr.toString());
    }

    private String hash(String str) {
        return str + "_";
    }

    public static String toPackageTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (c == '.') {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }
        return titleCase.toString();
    }

    public static void SetBasePackage(String oldName, String newName) {
        oldSuffix = oldName;
        newSuffix = newName;
    }
}
