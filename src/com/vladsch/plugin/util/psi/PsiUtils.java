package com.vladsch.plugin.util.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;

public class PsiUtils {
    static class Holder {
        final static HashSet<String> TYPE_PREFIXES = new HashSet<>(Arrays.asList(
                "TABLE_CELL_",
                "TABLE_HDR_"
        ));
    }

    public static boolean isTypeOfSuffix(@Nullable IElementType elementType, @NotNull TokenSet tokenSet) {
        if (elementType != null) {
            String typeName = elementType.toString();

            if (typeName.startsWith("TABLE_CELL_") || typeName.startsWith("TABLE_HDR_")) {
                for (IElementType types : tokenSet.getTypes()) {
                    if (typeName.endsWith("_" + types.toString())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isTypeOf(@Nullable IElementType elementType, @NotNull TokenSet tokenSet) {
        return tokenSet.contains(elementType) || isTypeOfSuffix(elementType, tokenSet);
    }

    public static boolean isNullOrTypeOf(@Nullable IElementType elementType, @NotNull TokenSet tokenSet) {
        return elementType == null || isTypeOf(elementType, tokenSet);
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
