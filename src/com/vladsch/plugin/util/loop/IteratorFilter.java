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

package com.vladsch.plugin.util.loop;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import org.jetbrains.annotations.Nullable;

public interface IteratorFilter<T> extends MorphingFilter<T, T> {

    IteratorFilter<PsiElement> NullPsiFilter = new IteratorFilter<PsiElement>() {
        @Nullable
        @Override
        public PsiElement match(final PsiElement element) {
            return element;
        }
    };

    IteratorFilter<ASTNode> NullAstFilter = new IteratorFilter<ASTNode>() {
        @Nullable
        @Override
        public ASTNode match(final ASTNode element) {
            return element instanceof LeafPsiElement ? element : null;
        }
    };

    IteratorFilter<PsiElement> FilterOutLeafPsi = new IteratorFilter<PsiElement>() {
        @Nullable
        @Override
        public PsiElement match(final PsiElement element) {
            return element instanceof LeafPsiElement ? null : element;
        }
    };

    IteratorFilter<PsiElement> FilterOutNonLeafPsi = new IteratorFilter<PsiElement>() {
        @Nullable
        @Override
        public PsiElement match(final PsiElement element) {
            return element instanceof LeafPsiElement ? element : null;
        }
    };
}
