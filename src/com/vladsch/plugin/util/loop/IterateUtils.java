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
import org.jetbrains.annotations.NotNull;

public class IterateUtils {

    public static <N, F extends N> void iterate(@NotNull LoopIterator<N> iterator, @NotNull MorphingFilter<N, F> filter, @NotNull N element, @NotNull ReturnResult<Object> result, @NotNull BreakingConsumer<F> consumer) {
        iterate(iterator, filter, element, result, (ReturningConsumer<F, Object>) consumer::accept);
    }

    public static <N, F extends N, R> void iterate(@NotNull LoopIterator<N> iterator, @NotNull MorphingFilter<N, F> filter, @NotNull N element, @NotNull ReturnResult<R> result, @NotNull ReturningConsumer<F, R> consumer) {
        N next = iterator.getFirst(element);
        while (next != null) {
            N child = next;
            next = iterator.getNext(next);

            F match = filter.match(element);
            if (match == null) continue; 
            
            consumer.accept(match, result);

            if (result.isBreak()) break;
            if (result.isContinue()) continue;

            iterate(iterator, filter, child, result, consumer);
            if (result.isBreak()) break;
        }
    }

    /* ******************** Recursive Versions **************************** */
    
    public static <N, F extends N> void forEachChild(boolean recurse, @NotNull LoopIterator<N> iterator, @NotNull MorphingFilter<N, F> filter, @NotNull N element, @NotNull BreakingConsumer<F> consumer) {
        ReturnResult<Object> result = new ReturnResult<>(BreakResult.NULL);
        if (!recurse) result.Continue();
        iterate(iterator, filter, element, result, consumer);
    }

    @NotNull
    public static <N, F extends N, R> R forEachChild(boolean recurse, @NotNull LoopIterator<N> iterator, @NotNull MorphingFilter<N, F> filter, @NotNull N element, @NotNull R defaultValue, @NotNull ReturningConsumer<F, R> consumer) {
        ReturnResult<R> result = new ReturnResult<>(defaultValue);
        if (!recurse) result.Continue();
        iterate(iterator, filter, element, result, consumer);
        return result.getValue();
    }

    /* ******************** PsiElement Versions, Filtering leaf elements **************************** */
    
    public static void forEachChild(boolean recurse, @NotNull LoopIterator<PsiElement> iterator,  @NotNull PsiElement element, boolean wantLeafElements, @NotNull BreakingConsumer<PsiElement> consumer) {
        forEachChild(recurse, iterator, wantLeafElements ? IteratorFilter.NullPsiFilter :  IteratorFilter.FilterOutLeafPsi, element, consumer);
    }

    @NotNull
    public static <R> R forEachChild(boolean recurse, @NotNull LoopIterator<PsiElement> iterator, @NotNull PsiElement element, @NotNull R defaultValue, boolean wantLeafElements, @NotNull ReturningConsumer<PsiElement, R> consumer) {
        return forEachChild(recurse, iterator, wantLeafElements ? IteratorFilter.NullPsiFilter :  IteratorFilter.FilterOutLeafPsi, element, defaultValue, consumer);
    }

    public static void forEachChild(boolean recurse, @NotNull PsiElement element, boolean wantLeafElements, @NotNull BreakingConsumer<PsiElement> consumer) {
        forEachChild(recurse, LoopIterator.PsiIterator, element, wantLeafElements, consumer);
    }

    public static void forEachChildRev(boolean recurse, @NotNull PsiElement element, boolean wantLeafElements, @NotNull BreakingConsumer<PsiElement> consumer) {
        forEachChild(recurse, LoopIterator.RevPsiIterator, element, wantLeafElements, consumer);
    }

    @NotNull
    public static <R> R forEachChild(boolean recurse, @NotNull PsiElement element, @NotNull R defaultValue, boolean wantLeafElements, @NotNull ReturningConsumer<PsiElement, R> consumer) {
        return forEachChild(recurse, LoopIterator.PsiIterator, element, defaultValue, wantLeafElements, consumer);
    }

    @NotNull
    public static <R> R forEachChildRev(boolean recurse, @NotNull PsiElement element, @NotNull R defaultValue, boolean wantLeafElements, @NotNull ReturningConsumer<PsiElement, R> consumer) {
        return forEachChild(recurse, LoopIterator.RevPsiIterator, element, defaultValue, wantLeafElements, consumer);
    }

    /* ******************** ASTNode Versions **************************** */

    public static void forEachChild(boolean recurse, @NotNull MorphingFilter<ASTNode, ASTNode> filter, @NotNull ASTNode element, @NotNull BreakingConsumer<ASTNode> consumer) {
        forEachChild(recurse, LoopIterator.AstIterator, filter, element, consumer);
    }

    public static void forEachChildRev(boolean recurse, @NotNull ASTNode element, @NotNull BreakingConsumer<ASTNode> consumer) {
        forEachChild(recurse, LoopIterator.RevAstIterator,  IteratorFilter.NullAstFilter, element, consumer);
    }

    @NotNull
    public static <R> R forEachChild(boolean recurse, @NotNull ASTNode element, @NotNull R defaultValue, @NotNull ReturningConsumer<ASTNode, R> consumer) {
        return forEachChild(recurse, LoopIterator.AstIterator,  IteratorFilter.NullAstFilter, element, defaultValue, consumer);
    }

    @NotNull
    public static <R> R forEachChildRev(boolean recurse, @NotNull ASTNode element, @NotNull R defaultValue, @NotNull ReturningConsumer<ASTNode, R> consumer) {
        return forEachChild(recurse, LoopIterator.RevAstIterator,  IteratorFilter.NullAstFilter, element, defaultValue, consumer);
    }
}
