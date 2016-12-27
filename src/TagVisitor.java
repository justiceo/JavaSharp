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
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.QualifiedNameExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.Iterator;

import static com.github.javaparser.ast.internal.Utils.isNullOrEmpty;

/**
 * Dumps the AST to formatted Java source code.
 *
 * @author Julio Vilmar Gesser
 */
public class TagVisitor extends VoidVisitorAdapter<Object> {

    @Override public void visit(final NameExpr n, final Object arg) {
        n.setName(n.getName()+ "_NameExpr");
    }

    @Override public void visit(final QualifiedNameExpr n, final Object arg) {
        n.setName(n.getName()+ "_QNameExpr");
        n.getQualifier().accept(this, arg);
    }

    @Override public void visit(final ClassOrInterfaceDeclaration n, final Object arg) {
        n.setName(n.getName().toUpperCase());
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
    }

    @Override public void visit(final ConstructorDeclaration n, final Object arg) {
        n.setName(n.getName() + "_ConstructorDeca");
        if (!n.getParameters().isEmpty()) {
            for (final Iterator<Parameter> i = n.getParameters().iterator(); i.hasNext();) {
                final Parameter p = i.next();
                p.accept(this, arg);
            }
        }
        n.getBlock().accept(this, arg);
    }

    @Override public void visit(final MethodDeclaration n, final Object arg) {
        n.setName(n.getName() + "_MethodDeclaration");
        n.getType().accept(this, arg);

        if (!isNullOrEmpty(n.getParameters())) {
            for (final Iterator<Parameter> i = n.getParameters().iterator(); i.hasNext();) {
                final Parameter p = i.next();
                p.accept(this, arg);
            }
        }
        if (n.getBody() != null) {
            n.getBody().accept(this, arg);
        }
    }

    @Override public void visit(final EnumDeclaration n, final Object arg) {
        n.setName(n.getName() + "_EnumDeclaration");
    }

    @Override public void visit(final EnumConstantDeclaration n, final Object arg) {
        n.setName(n.getName() + "_EnumConstantDeclaration");
    }

    @Override public void visit(final AnnotationDeclaration n, final Object arg) {
        n.setName(n.getName() + "_AnnotationDeclaration");
    }

    @Override public void visit(final AnnotationMemberDeclaration n, final Object arg) {
        n.setName(n.getName() + "_AnnotMemberDeclaration");
    }

    @Override public void visit(final MemberValuePair n, final Object arg) {
        n.setName(n.getName() + "_MemberValuePair");
    }
}

