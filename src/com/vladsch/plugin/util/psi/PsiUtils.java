package com.vladsch.plugin.util.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiUtils {
    public static boolean isTypeOf(@Nullable IElementType elementType, @NotNull TokenSet tokenSet) {
        return tokenSet.contains(elementType);
    }

    public static boolean isNullOrTypeOf(@Nullable IElementType elementType, @NotNull TokenSet tokenSet) {
        return elementType == null || tokenSet.contains(elementType);
    }

    public static boolean isTypeOf(@Nullable ASTNode node, @NotNull TokenSet tokenSet) {
        return node != null && isTypeOf(node.getElementType(), tokenSet);
    }

    public static boolean isNullOrTypeOf(@Nullable ASTNode node, @NotNull TokenSet tokenSet) {
        return node == null || isNullOrTypeOf(node.getElementType(), tokenSet);
    }

    public static boolean isTypeOf(@Nullable PsiElement element, @NotNull TokenSet tokenSet) {
        return element != null && isTypeOf(element.getNode(), tokenSet);
    }

    public static boolean isNullOrTypeOf(@Nullable PsiElement element, @NotNull TokenSet tokenSet) {
        return element == null || isNullOrTypeOf(element.getNode(), tokenSet);
    }

    public static boolean isTypeOf(@Nullable ASTNode node, @NotNull IElementType elementType) {
        return node != null && elementType.equals(node.getElementType());
    }

    public static boolean isNullOrTypeOf(@Nullable ASTNode node, @NotNull IElementType tokenSet) {
        IElementType elementType = node == null ? null : node.getElementType();
        return node == null || tokenSet.equals(elementType);
    }

    public static boolean isTypeOf(@Nullable PsiElement element, @NotNull IElementType tokenSet) {
        return element != null && isTypeOf(element.getNode(), tokenSet);
    }

    public static boolean isNullOrTypeOf(@Nullable PsiElement element, @NotNull IElementType tokenSet) {
        return element == null || isNullOrTypeOf(element.getNode(), tokenSet);
    }
}
