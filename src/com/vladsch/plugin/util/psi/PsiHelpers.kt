package com.vladsch.plugin.util.psi

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.TreeUtil
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet

fun PsiElement?.isTypeOf(tokenSet: TokenSet) = PsiUtils.isTypeOf(this, tokenSet)
fun PsiElement?.isNullOrTypeOf(tokenSet: TokenSet) = PsiUtils.isNullOrTypeOf(this, tokenSet)
fun ASTNode?.isTypeIn(tokenSet: TokenSet) = PsiUtils.isTypeOf(this, tokenSet)
fun ASTNode?.isNullOrTypeIn(tokenSet: TokenSet) = PsiUtils.isNullOrTypeOf(this, tokenSet)
fun IElementType?.isIn(tokenSet: TokenSet) = PsiUtils.isTypeOf(this, tokenSet)
fun IElementType?.isNullOrIn(tokenSet: TokenSet) = PsiUtils.isNullOrTypeOf(this, tokenSet)

fun PsiElement?.isIn(elementType: IElementType) = PsiUtils.isTypeOf(this, elementType)
fun PsiElement?.isNullOrIn(elementType: IElementType) = PsiUtils.isNullOrTypeOf(this, elementType)
fun ASTNode?.isTypeIn(elementType: IElementType) = PsiUtils.isTypeOf(this, elementType)
fun ASTNode?.isNullOrTypeIn(elementType: IElementType) = PsiUtils.isNullOrTypeOf(this, elementType)

fun ASTNode?.isParentOfTypeIn(tokenSet: TokenSet) = this != null && TreeUtil.findParent(this, tokenSet) != null
