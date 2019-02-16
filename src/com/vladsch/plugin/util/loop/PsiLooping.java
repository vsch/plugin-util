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

import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Predicate;

public interface PsiLooping<T extends PsiElement> extends MorphedLooping<PsiElement, T> {
    static PsiLooping<PsiElement> of(final @NotNull PsiElement element, final @NotNull LoopingImpl<PsiElement> looping) {
        return new PsiLoopingImpl<>(element, ValueLoopAdapterImpl.of(), looping);
    }

    static PsiLooping<PsiElement> of(final @NotNull PsiElement element, final @NotNull LoopConstraints<PsiElement> constraints) {
        return of(element, new LoopingImpl<>(constraints));
    }

    static PsiLooping<PsiElement> of(final @NotNull PsiElement element, final @NotNull LoopConstraints<PsiElement> constraints, final @NotNull Predicate<PsiElement> filter) {
        return of(element, new LoopingImpl<>(constraints, filter));
    }

    static PsiLooping<PsiElement> of(final @NotNull PsiElement element, final @NotNull LoopConstraints<PsiElement> constraints, final @NotNull Predicate<PsiElement> filter, final @NotNull Predicate<PsiElement> recursion) {
        return of(element, new LoopingImpl<>(constraints, filter, recursion));
    }

    @NotNull
    @Override
    PsiLooping<T> reversed();

    @NotNull
    @Override
    PsiLooping<T> recursive();

    @NotNull
    @Override
    PsiLooping<T> nonRecursive();

    @NotNull
    default PsiLooping<T> recursive(boolean recursive) {
        return recursive ? recursive() : nonRecursive();
    }

    @NotNull
    default PsiLooping<T> nonRecursive(boolean nonRecursive) {
        return nonRecursive ? nonRecursive() : recursive();
    }

    @NotNull
    @Override
    PsiLooping<T> recurse(@NotNull Predicate<PsiElement> predicate);

    @NotNull
    PsiLooping<T> recurse(@NotNull Class clazz);

    @NotNull
    <F> PsiLooping<T> recurse(@NotNull Class<F> clazz, @NotNull Predicate<F> predicate);

    @NotNull
    @Override
    PsiLooping<T> filterFalse();

    @NotNull
    @Override
    PsiLooping<T> aborted();

    @NotNull
    @Override
    PsiLooping<T> filterOut(@NotNull Predicate<PsiElement> predicate);

    @NotNull
    @Override
    PsiLooping<T> filterOut(@NotNull Class clazz);

    @NotNull
    @Override
    <F> PsiLooping<T> filterOut(@NotNull Class<F> clazz, @NotNull Predicate<F> predicate);

    @NotNull
    @Override
    PsiLooping<T> filter(@NotNull Predicate<PsiElement> predicate);

    @NotNull
    <F extends PsiElement> PsiLooping<F> filter(@NotNull Class<F> clazz);

    @NotNull
    <F extends PsiElement> PsiLooping<F> filter(@NotNull Class<F> clazz, @NotNull Predicate<F> predicate);

    @NotNull
    <F extends PsiElement> PsiLooping<F> filter(@NotNull Function<T, F> adapter);

    @NotNull
    <F extends PsiElement> PsiLooping<F> filter(@NotNull ValueLoopAdapter<T, F> adapter);

    @NotNull
    PsiLooping<T> filter(@NotNull ValueLoopFilter<T> filter);


    /*
     * Psi Only
     */

    @NotNull
    PsiLooping<T> recurse(@NotNull TokenSet tokenSet);

    @NotNull
    PsiLooping<T> filterOut(@NotNull TokenSet tokenSet);

    @NotNull
    PsiLooping<T> filter(@NotNull TokenSet tokenSet);

    @NotNull
    PsiLooping<T> filterOutLeafPsi();
}
