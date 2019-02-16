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
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Predicate;

public interface Loop<N> {
    LoopConstrainsBuilder<PsiElement> PSI = LoopConstrainsBuilder.PSI_LOOPS;
    LoopConstrainsBuilder<ASTNode> AST = LoopConstrainsBuilder.AST_LOOPS;
    Predicate NOT_LEAF_PSI = n -> n instanceof LeafPsiElement;
    Predicate LEAF_PSI = n -> !(n instanceof LeafPsiElement);
    Predicate TRUE = o -> true;
    Predicate FALSE = o -> false;
    Predicate NOT_NULL = Objects::nonNull;

    @NotNull
    LoopConstraints<N> getConstraints();

    Predicate<N> getRecursion();

    Predicate<N> getFilter();

    default <R> ValueLoop<R> iterate(@NotNull N element, @NotNull R defaultValue, @NotNull ValueLoopConsumer<N, R> consumer) {
        final LoopInstance<N, R> instance = new LoopInstance<>(getConstraints(), getFilter(), getRecursion(), element, defaultValue);
        instance.iterate(consumer);
        return instance;
    }

    default <T, R> ValueLoop<R> iterate(@NotNull N element, @NotNull R defaultValue, @NotNull ValueLoopAdapter<N, T> adapter, @NotNull ValueLoopConsumer<T, R> consumer) {
        final LoopInstance<N, R> instance = new LoopInstance<>(getConstraints(), getFilter(), getRecursion(), element, defaultValue);
        instance.iterate(adapter.getConsumerAdapter().getConsumer(consumer));
        return instance;
    }

    default <R> VoidLoop iterate(@NotNull final N element, @NotNull final VoidLoopConsumer<N> consumer) {
        final LoopInstance<N, R> instance = new LoopInstance<>(getConstraints(), getFilter(), getRecursion(), element);
        instance.iterate(consumer);
        return instance;
    }

    default <T, R> VoidLoop iterate(@NotNull final N element, @NotNull ValueLoopAdapter<N, T> adapter, @NotNull final VoidLoopConsumer<T> consumer) {
        final LoopInstance<N, R> instance = new LoopInstance<>(getConstraints(), getFilter(), getRecursion(), element);
        instance.iterate(adapter.getConsumerAdapter().getConsumer(consumer));
        return instance;
    }

    @NotNull
    default <R> R doLoop(@NotNull N element, @NotNull R defaultValue, @NotNull ValueLoopConsumer<N, R> consumer) {
        return iterate(element, defaultValue, consumer).getResult();
    }

    default void doLoop(@NotNull N element, @NotNull VoidLoopConsumer<N> consumer) {
        iterate(element, consumer);
    }

    @NotNull
    default <T, R> R doLoop(@NotNull N element, @NotNull R defaultValue, @NotNull ValueLoopAdapter<N, T> adapter, @NotNull ValueLoopConsumer<T, R> consumer) {
        return iterate(element, defaultValue, adapter, consumer).getResult();
    }

    default <T, R> void doLoop(@NotNull N element, @NotNull ValueLoopAdapter<N, T> adapter, @NotNull VoidLoopConsumer<T> consumer) {
        iterate(element, adapter, consumer);
    }
}
