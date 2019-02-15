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

public interface LoopIterator<T> {
    T getFirst(T element);
    T getNext(T element);
    
    LoopIterator<PsiElement> PsiIterator = new LoopIterator<PsiElement>() {
        @Override
        public PsiElement getFirst(final PsiElement element) {
            return element.getFirstChild();
        }

        @Override
        public PsiElement getNext(final PsiElement element) {
            return element.getNextSibling();
        }
    };

    LoopIterator<PsiElement> RevPsiIterator = new LoopIterator<PsiElement>() {
        @Override
        public PsiElement getFirst(final PsiElement element) {
            return element.getLastChild();
        }

        @Override
        public PsiElement getNext(final PsiElement element) {
            return element.getPrevSibling();
        }
    };

    LoopIterator<ASTNode> AstIterator = new LoopIterator<ASTNode>() {
        @Override
        public ASTNode getFirst(final ASTNode element) {
            return element.getFirstChildNode();
        }

        @Override
        public ASTNode getNext(final ASTNode element) {
            return element.getTreeNext();
        }
    };

    LoopIterator<ASTNode> RevAstIterator = new LoopIterator<ASTNode>() {
        @Override
        public ASTNode getFirst(final ASTNode element) {
            return element.getLastChildNode();
        }

        @Override
        public ASTNode getNext(final ASTNode element) {
            return element.getTreePrev();
        }
    };
}
