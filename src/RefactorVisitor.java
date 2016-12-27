/**
 * Created by Justice on 12/25/2016.
 */
/*
 * Copyright (C) 2007-2010 Júlio Vilmar Gesser.
 * Copyright (C) 2011, 2013-2016 The JavaParser Team.
 *
 * This file is part of JavaParser.
 *
 * JavaParser can be used either under the terms of
 * a) the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * b) the terms of the Apache License
 *
 * You should have received a copy of both licenses in LICENCE.LGPL and
 * LICENCE.APACHE. Please refer to those files for details.
 *
 * JavaParser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 */


import com.github.javaparser.ast.TypeParameter;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.Iterator;
import java.util.List;

import static com.github.javaparser.ast.internal.Utils.isNullOrEmpty;

/**
 * Dumps the AST to formatted Java source code.
 *
 * @author Julio Vilmar Gesser
 */
public class RefactorVisitor extends VoidVisitorAdapter<Object> {

    private boolean printComments;

    public RefactorVisitor() {
        this(true);
    }

    public RefactorVisitor(boolean printComments) {
        this.printComments = printComments;
    }

    public static class SourcePrinter {

        private final String indentation;

        public SourcePrinter(final String indentation) {
            this.indentation = indentation;
        }

        private int level = 0;

        private boolean indented = false;

        private final StringBuilder buf = new StringBuilder();

        public void indent() {
            level++;
        }

        public void unindent() {
            level--;
        }

        private void makeIndent() {
            for (int i = 0; i < level; i++) {
                buf.append(indentation);
            }
        }

        public void print(final String arg) {
            if (!indented) {
                makeIndent();
                indented = true;
            }
            buf.append(arg);
        }

        public void printLn(final String arg) {
            print(arg);
            printLn();
        }

        public void printLn() {
            buf.append(System.getProperty("line.separator"));
            indented = false;
        }

        public String getSource() {
            return buf.toString();
        }

        @Override public String toString() {
            return getSource();
        }
    }

    private final com.github.javaparser.ast.visitor.DumpVisitor.SourcePrinter printer = createSourcePrinter();

    protected com.github.javaparser.ast.visitor.DumpVisitor.SourcePrinter createSourcePrinter() {
        return new com.github.javaparser.ast.visitor.DumpVisitor.SourcePrinter("    ");
    }

    public String getSource() {
        return printer.getSource();
    }

    private void printModifiersToAnnotations(int modifiers, Object arg) {

        // don't forget to remove any modifier printed here from printModifiers()
        if (ModifierSet.isSynchronized(modifiers)) {
            printer.printLn("[MethodImpl(MethodImplOptions.Synchronized)]");
        }
        if (ModifierSet.isTransient(modifiers)) {
            printer.printLn("[ScriptIgnore]");
        }
    }

    private void printMembers(final List<BodyDeclaration> members, final Object arg) {
        for (final BodyDeclaration member : members) {
            printer.printLn();
            member.accept(this, arg);
            printer.printLn();
        }
    }

    private void printMemberAnnotations(final List<AnnotationExpr> annotations, final Object arg) {
        if (!isNullOrEmpty(annotations)) {
            for (final AnnotationExpr a : annotations) {
                // any annotation removed should be added elsewhere or commented out unless unnecessary
                if(a.getName().toString().equals("Override")) {
                    continue;
                }
                if(a.getName().toString().equals("Deprecated")) {
                    printer.printLn("[Obsolete] ");
                    continue;
                }
                a.accept(this, arg);
                printer.printLn();
            }
        }
    }

    private void printAnnotationsToModifiers(List<AnnotationExpr> annotations, Object arg) {
        for (final AnnotationExpr a : annotations) {
            // any annotation converted should be removed from printMemberAnnotations()
            if(a.getName().toString().equals("Override")) {
                printer.print("override ");
            }
        }
    }

    private void printAnnotations(final List<AnnotationExpr> annotations, final Object arg) {
        if (!isNullOrEmpty(annotations)) {
            for (final AnnotationExpr a : annotations) {
                a.accept(this, arg);
                printer.print(" ");
            }
        }
    }

    private void printTypeArgs(final List<Type> args, final Object arg) {
        if (!isNullOrEmpty(args)) {
            printer.print("<");
            for (final Iterator<Type> i = args.iterator(); i.hasNext();) {
                final Type t = i.next();
                t.accept(this, arg);
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
            printer.print(">");
        }
    }

    private void printTypeParameters(final List<TypeParameter> args, final Object arg) {
        if (!isNullOrEmpty(args)) {
            printer.print("<");
            for (final Iterator<TypeParameter> i = args.iterator(); i.hasNext();) {
                final TypeParameter t = i.next();
                t.accept(this, arg);
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
            printer.print(">");
        }
    }

    private void printArguments(final List<Expression> args, final Object arg) {
        printer.print("(");
        if (!isNullOrEmpty(args)) {
            for (final Iterator<Expression> i = args.iterator(); i.hasNext();) {
                final Expression e = i.next();
                e.accept(this, arg);
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
        }
        printer.print(")");
    }

    private void printJavadoc(final JavadocComment javadoc, final Object arg) {
        if (javadoc != null) {
            javadoc.accept(this, arg);
        }
    }

    private void printJavaComment(final Comment javacomment, final Object arg) {
        /*if (javacomment != null) {
            javacomment.accept(this, arg);
        }*/
    }

    @Override public void visit(final NameExpr n, final Object arg) {
        n.setName(n.getName()+ "_NameExpr");
    }

    @Override public void visit(final QualifiedNameExpr n, final Object arg) {
        n.getQualifier().accept(this, arg);
        n.setName(n.getName()+ "_QNameExpr");
    }

    @Override public void visit(final ClassOrInterfaceDeclaration n, final Object arg) {

        n.setName(n.getName().toUpperCase());

        if (!isNullOrEmpty(n.getExtends())) {
            for (final Iterator<ClassOrInterfaceType> i = n.getExtends().iterator(); i.hasNext();) {
                final ClassOrInterfaceType c = i.next();
                c.accept(this, arg);
            }
        }

        if (!isNullOrEmpty(n.getImplements())) {
            for (final Iterator<ClassOrInterfaceType> i = n.getImplements().iterator(); i.hasNext();) {
                final ClassOrInterfaceType c = i.next();
                c.accept(this, arg);
            }
        }

        if (!isNullOrEmpty(n.getMembers())) {
            printMembers(n.getMembers(), arg);
        }
    }

    @Override public void visit(final ClassOrInterfaceType n, final Object arg) {
        n.setName(n.getName() + "_CorIType");
        if (n.getScope() != null) {
            n.getScope().accept(this, arg);
        }
    }

    @Override public void visit(final TypeParameter n, final Object arg) {
        n.setName(n.getName() + "_TypeParameter");
        if (!isNullOrEmpty(n.getTypeBound())) {
            for (final Iterator<ClassOrInterfaceType> i = n.getTypeBound().iterator(); i.hasNext();) {
                final ClassOrInterfaceType c = i.next();
                c.accept(this, arg);
            }
        }
    }

    @Override public void visit(final VariableDeclaratorId n, final Object arg) {
        n.setName(n.getName() + "_VarDecatorId");
    }

    @Override public void visit(final MethodCallExpr n, final Object arg) {
        n.setName(n.getName() + "_MethodCallExpr");
        if (n.getScope() != null) {
            n.getScope().accept(this, arg);
        }
        printTypeArgs(n.getTypeArgs(), arg);
        printer.print(n.getName());
        printArguments(n.getArgs(), arg);
    }

    @Override public void visit(final ConstructorDeclaration n, final Object arg) { // stays
        printJavaComment(n.getComment(), arg);
        printJavadoc(n.getJavaDoc(), arg);
        printMemberAnnotations(n.getAnnotations(), arg);

        printTypeParameters(n.getTypeParameters(), arg);
        if (!n.getTypeParameters().isEmpty()) {
            printer.print(" ");
        }
        printer.print(n.getName());

        printer.print("(");
        if (!n.getParameters().isEmpty()) {
            for (final Iterator<Parameter> i = n.getParameters().iterator(); i.hasNext();) {
                final Parameter p = i.next();
                p.accept(this, arg);
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
        }
        n.getBlock().accept(this, arg);
    }

    @Override public void visit(final MethodDeclaration n, final Object arg) {
        printJavaComment(n.getComment(), arg);
        printJavadoc(n.getJavaDoc(), arg);
        printMemberAnnotations(n.getAnnotations(), arg);
        printModifiersToAnnotations(n.getModifiers(), arg);
        if (n.isDefault()) {
            printer.print("default ");
        }
        printAnnotationsToModifiers(n.getAnnotations(), arg);
        printTypeParameters(n.getTypeParameters(), arg);
        if (!isNullOrEmpty(n.getTypeParameters())) {
            printer.print(" ");
        }

        n.getType().accept(this, arg);
        printer.print(" ");
        printer.print(n.getName());

        printer.print("(");
        if (!isNullOrEmpty(n.getParameters())) {
            for (final Iterator<Parameter> i = n.getParameters().iterator(); i.hasNext();) {
                final Parameter p = i.next();
                p.accept(this, arg);
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
        }
        printer.print(")");

        for (int i = 0; i < n.getArrayCount(); i++) {
            printer.print("[]");
        }
        /*
        if (!isNullOrEmpty(n.getThrows())) {
            printer.print(" throws ");
            for (final Iterator<ReferenceType> i = n.getThrows().iterator(); i.hasNext();) {
                final ReferenceType name = i.next();
                name.accept(this, arg);
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
        }*/
        if (n.getBody() == null) {
            printer.print(";");
        } else {
            printer.print(" ");
            n.getBody().accept(this, arg);
        }
    }

    @Override public void visit(final EnumDeclaration n, final Object arg) { // stays
        printJavaComment(n.getComment(), arg);
        printJavadoc(n.getJavaDoc(), arg);
        printMemberAnnotations(n.getAnnotations(), arg);

        printer.print("enum ");
        printer.print(n.getName());

        if (!n.getImplements().isEmpty()) {
            printer.print(" implements ");
            for (final Iterator<ClassOrInterfaceType> i = n.getImplements().iterator(); i.hasNext();) {
                final ClassOrInterfaceType c = i.next();
                c.accept(this, arg);
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
        }

        printer.printLn(" {");
        printer.indent();
        if (n.getEntries() != null) {
            printer.printLn();
            for (final Iterator<EnumConstantDeclaration> i = n.getEntries().iterator(); i.hasNext();) {
                final EnumConstantDeclaration e = i.next();
                e.accept(this, arg);
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
        }
        if (!n.getMembers().isEmpty()) {
            printer.printLn(";");
            printMembers(n.getMembers(), arg);
        } else {
            if (!n.getEntries().isEmpty()) {
                printer.printLn();
            }
        }
        printer.unindent();
        printer.print("}");
    }

    @Override public void visit(final EnumConstantDeclaration n, final Object arg) { // stays
        printJavaComment(n.getComment(), arg);
        printJavadoc(n.getJavaDoc(), arg);
        printMemberAnnotations(n.getAnnotations(), arg);
        printer.print(n.getName());

        if (!n.getArgs().isEmpty()) {
            printArguments(n.getArgs(), arg);
        }

        if (!n.getClassBody().isEmpty()) {
            printer.printLn(" {");
            printer.indent();
            printMembers(n.getClassBody(), arg);
            printer.unindent();
            printer.printLn("}");
        }
    }

    @Override public void visit(final AnnotationDeclaration n, final Object arg) { // stays
        printJavaComment(n.getComment(), arg);
        printJavadoc(n.getJavaDoc(), arg);
        printMemberAnnotations(n.getAnnotations(), arg);

        printer.print("@interface ");
        printer.print(n.getName());
        printer.printLn(" {");
        printer.indent();
        if (n.getMembers() != null) {
            printMembers(n.getMembers(), arg);
        }
        printer.unindent();
        printer.print("}");
    }

    @Override public void visit(final AnnotationMemberDeclaration n, final Object arg) { // stays
        printJavaComment(n.getComment(), arg);
        printJavadoc(n.getJavaDoc(), arg);
        printMemberAnnotations(n.getAnnotations(), arg);

        n.getType().accept(this, arg);
        printer.print(" ");
        printer.print(n.getName());
        printer.print("()");
        if (n.getDefaultValue() != null) {
            printer.print(" default ");
            n.getDefaultValue().accept(this, arg);
        }
        printer.print(";");
    }

    @Override public void visit(final MemberValuePair n, final Object arg) { // stays
        printJavaComment(n.getComment(), arg);
        printer.print(n.getName());
        printer.print(" = ");
        n.getValue().accept(this, arg);
    }
}

