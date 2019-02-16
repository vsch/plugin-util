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
import java.util.function.Function;
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

    default <R> ValueLoop<N, R> iterate(@NotNull N element, @NotNull R defaultValue, @NotNull ValueLoopConsumer<N, R> consumer) {
        final LoopInstance<N, R> instance = new LoopInstance<>(getConstraints(), getFilter(), getRecursion(), element, defaultValue);
        instance.iterate(consumer);
        return instance;
    }

    default <F, R> ValueLoop<N, R> iterate(@NotNull N element, @NotNull R defaultValue, @NotNull Function<N, F> adapter, @NotNull ValueLoopConsumer<F, R> consumer) {
        final MappedLoopInstance<N, F, R> instance = new MappedLoopInstance<>(adapter, new LoopInstance<>(getConstraints(), getFilter().and(it -> adapter.apply(it) != null), getRecursion(), element, defaultValue));
        if (adapter instanceof VoidLoopAdapter) {
            final VoidLoopConsumer<F> preFilter = ((VoidLoopAdapter<F>) adapter).getInstance();
            instance.iterate((it, result) -> {
                F applied = adapter.apply(it);
                preFilter.accept(applied, instance);
                if (result.isActive()) {
                    consumer.accept(applied, instance);
                }
            });
        } else {
            instance.iterate((it, result) -> consumer.accept(adapter.apply(it), instance));
        }
        return instance.getInstance();
    }

    default <R> VoidLoop<N> iterate(@NotNull final N element, @NotNull final VoidLoopConsumer<N> consumer) {
        final LoopInstance<N, R> instance = new LoopInstance<>(getConstraints(), getFilter(), getRecursion(), element);
        instance.iterate(consumer::accept);
        return instance;
    }

    default <F> VoidLoop<N> iterate(@NotNull final N element, @NotNull Function<N, F> adapter, @NotNull final VoidLoopConsumer<F> consumer) {
        final MappedLoopInstance<N, F, Object> instance = new MappedLoopInstance<>(adapter, new LoopInstance<>(getConstraints(), getFilter().and(it -> adapter.apply(it) != null), getRecursion(), element));
        if (adapter instanceof VoidLoopAdapter) {
            final VoidLoopConsumer<F> preFilter = ((VoidLoopAdapter<F>) adapter).getInstance();
            instance.iterate((it, result) -> {
                F applied = adapter.apply(it);
                preFilter.accept(applied, instance);
                if (result.isActive()) {
                    consumer.accept(applied, instance);
                }
            });
        } else {
            instance.iterate((it, result) -> consumer.accept(adapter.apply(it), instance));
        }
        return instance.getInstance();
    }

    @NotNull
    default <R> R doLoop(@NotNull N element, @NotNull R defaultValue, @NotNull ValueLoopConsumer<N, R> consumer) {
        return iterate(element, defaultValue, consumer).getValue();
    }

    default void doLoop(@NotNull N element, @NotNull VoidLoopConsumer<N> consumer) {
        iterate(element, consumer);
    }

    @NotNull
    default <F, R> R doLoop(@NotNull N element, @NotNull R defaultValue, @NotNull Function<N, F> adapter, @NotNull ValueLoopConsumer<F, R> consumer) {
        return iterate(element, defaultValue, adapter, consumer).getValue();
    }

    default <F> void doLoop(@NotNull N element, @NotNull Function<N, F> adapter, @NotNull VoidLoopConsumer<F> consumer) {
        iterate(element, adapter, consumer);
    }
}
