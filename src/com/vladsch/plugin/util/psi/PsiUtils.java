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

package com.vladsch.plugin.util.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiUtils {
    public static boolean isIn(@Nullable IElementType elementType, @NotNull TokenSet tokenSet) {
        return tokenSet.contains(elementType);
    }

    public static boolean isIn(@Nullable ASTNode node, @NotNull TokenSet tokenSet) {
        return node != null && isIn(node.getElementType(), tokenSet);
    }

    public static boolean isIn(@Nullable PsiElement element, @NotNull TokenSet tokenSet) {
        return element != null && isIn(element.getNode(), tokenSet);
    }

    public static boolean isInOrNull(@Nullable IElementType elementType, @NotNull TokenSet tokenSet) {
        return elementType == null || tokenSet.contains(elementType);
    }

    public static boolean isInOrNull(@Nullable ASTNode node, @NotNull TokenSet tokenSet) {
        return node == null || isIn(node.getElementType(), tokenSet);
    }

    public static boolean isInOrNull(@Nullable PsiElement element, @NotNull TokenSet tokenSet) {
        return element == null || isIn(element.getNode(), tokenSet);
    }
}
