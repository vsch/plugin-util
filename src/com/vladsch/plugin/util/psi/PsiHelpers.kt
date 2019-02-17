/*
 * Copyright (c) 2015-2019 Vladimir Schneider <vladimir.schneider@gmail.com>, all rights reserved.
 *
 * This code is private property of the copyright holder and cannot be used without
 * having obtained a license or prior written permission of the of the copyright holder.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package com.vladsch.plugin.util.psi

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
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
