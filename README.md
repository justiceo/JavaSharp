JavaSharp
=========

This tool would transpile a Java source code to valid C#.
Uses JavaParser(include link) to get the AST and then re-writes the syntax via a visitor.

Features
--------
* Basic syntax rewrite changes `boolean` to `bool`, `a(String... strArray)` to `a(params String[] strArray)`, `package` to `namespace` etc
* Structural rewrite extracts the `@Override` annotation to an inline `override` modifier, ensures that `namespace` scopes around all classes in the file etc
* Reserved words refactoring changes variables and methods with names like `ref`, `typeof`, `yield` to `ref_`, `typeof_`, `yield` respectively
* Namespace resolution trims excessive suffices like `org.java.package.actualCode` to `actualCode`, and with C# convention enabled would be `ActualCode`
* Type simplification take `String popsicle = "sweet"` to `var popsicle = "sweet"`
* [Deprecated] Regex transforms can be used to convert `a.toString()` to `a.ToString()`
* [Upcoming] Type mappings would transform `a.toString()` to `a.ToString()` 
