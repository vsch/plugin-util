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

fun PsiElement?.isTypeOf(tokenSet: TokenSet) = PsiUtils.isIn(this, tokenSet)
fun PsiElement?.isNullOrTypeOf(tokenSet: TokenSet) = PsiUtils.isInOrNull(this, tokenSet)
fun ASTNode?.isTypeIn(tokenSet: TokenSet) = PsiUtils.isIn(this, tokenSet)
fun ASTNode?.isNullOrTypeIn(tokenSet: TokenSet) = PsiUtils.isInOrNull(this, tokenSet)
fun IElementType?.isIn(tokenSet: TokenSet) = PsiUtils.isIn(this, tokenSet)
fun IElementType?.isInOrNull(tokenSet: TokenSet) = PsiUtils.isInOrNull(this, tokenSet)
