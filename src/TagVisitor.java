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

/**
 * Dumps the AST to formatted Java source code.
 *
 * @author Julio Vilmar Gesser
 */
public class TagVisitor extends VoidVisitorAdapter<Object> {

    @Override public void visit(final PackageDeclaration n, final Object arg) {
        n.getName().setName(n.getName()+ "_PackageDeclaration");
        visitComment(n.getComment(), arg);
        if (n.getAnnotations() != null) {
            for (final AnnotationExpr a : n.getAnnotations()) {
                a.accept(this, arg);
            }
        }
    }

    @Override public void visit(final ImportDeclaration n, final Object arg) {
        n.getName().setName(n.getName()+ "_ImportDeclaration");
        visitComment(n.getComment(), arg);
        n.getName().accept(this, arg);
    }

    @Override public void visit(final NameExpr n, final Object arg) {
        n.setName(n.getName()+ "_NameExpr");
        visitComment(n.getComment(), arg);
    }

    @Override public void visit(final QualifiedNameExpr n, final Object arg) {
        n.setName(n.getName()+ "_QNameExpr");
        visitComment(n.getComment(), arg);
        n.getQualifier().accept(this, arg);
    }

    @Override public void visit(final ClassOrInterfaceDeclaration n, final Object arg) {
        n.setName(n.getName() + "_CorIDeclaration");
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
        n.setName(n.getName() + "_CorIType");
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
        n.setName(n.getName() + "_TypeParameter");
        visitComment(n.getComment(), arg);
        if (n.getTypeBound() != null) {
            for (final ClassOrInterfaceType c : n.getTypeBound()) {
                c.accept(this, arg);
            }
        }
    }

    @Override public void visit(final VariableDeclaratorId n, final Object arg) {
        n.setName(n.getName() + "_VarDecatorId");
        visitComment(n.getComment(), arg);
    }

    @Override public void visit(final MethodCallExpr n, final Object arg) {
        n.setName(n.getName() + "_MethodCallExpr");
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
        n.setName(n.getName() + "_ConstructorDeca");
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
        n.setName(n.getName() + "_MethodDeclaration");
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
        n.setName(n.getName() + "_EnumDeclaration");
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
        n.setName(n.getName() + "_EnumConstantDeclaration");
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
        n.setName(n.getName() + "_AnnotationDeclaration");
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
        n.setName(n.getName() + "_AnnotMemberDeclaration");
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
        n.setName(n.getName() + "_MemberValuePair");
        visitComment(n.getComment(), arg);
        n.getValue().accept(this, arg);
    }

    private void visitComment(final Comment n, final Object arg) {
        if (n != null) {
            n.accept(this, arg);
        }
    }
}

