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
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.TokenSet;
import com.vladsch.plugin.util.looping.LoopConstraints;
import com.vladsch.plugin.util.looping.Looping;
import com.vladsch.plugin.util.looping.MappedLooping;
import com.vladsch.plugin.util.looping.ValueLoopAdapter;
import com.vladsch.plugin.util.looping.ValueLoopAdapterImpl;
import com.vladsch.plugin.util.looping.ValueLoopFilter;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Predicate;

public class PsiLooping<T extends PsiElement> extends MappedLooping<PsiElement, T> {
    public PsiLooping(@NotNull final PsiElement element, @NotNull ValueLoopAdapter<? super PsiElement, T> adapter, @NotNull Looping<PsiElement> looping) {
        super(element, adapter, looping);
    }

    // *******************************************************
    //
    // Need Subclass Constructors 
    //
    // *******************************************************

    @NotNull
    public PsiLooping<T> getModifiedCopy(final PsiElement element, final ValueLoopAdapter<? super PsiElement, T> adapter, final Looping<PsiElement> looping) {
        return new PsiLooping<>(element, adapter, looping);
    }

    @NotNull
    public <F extends PsiElement> PsiLooping<F> getModifiedCopyF(final PsiElement element, final ValueLoopAdapter<? super PsiElement, F> adapter, final Looping<PsiElement> looping) {
        return new PsiLooping<>(element, adapter, looping);
    }

    // *******************************************************
    //
    // Need Overrides with cast to sub-class
    //
    // *******************************************************

    @NotNull
    @Override
    public PsiLooping<T> reversed() {
        return (PsiLooping<T>) super.reversed();
    }

    @NotNull
    @Override
    public PsiLooping<T> recursive() {
        return (PsiLooping<T>) super.recursive();
    }

    @NotNull
    @Override
    public PsiLooping<T> nonRecursive() {
        return (PsiLooping<T>) super.nonRecursive();
    }

    @NotNull
    @Override
    public PsiLooping<T> recursive(final boolean recursive) {
        return (PsiLooping<T>) super.recursive(recursive);
    }

    @NotNull
    @Override
    public PsiLooping<T> nonRecursive(final boolean nonRecursive) {
        return (PsiLooping<T>) super.nonRecursive(nonRecursive);
    }

    @NotNull
    @Override
    public PsiLooping<T> recurse(@NotNull final Predicate<? super PsiElement> predicate) {
        return (PsiLooping<T>) super.recurse(predicate);
    }

    @NotNull
    @Override
    public PsiLooping<T> recurse(@NotNull final Class clazz) {
        return (PsiLooping<T>) super.recurse(clazz);
    }

    @NotNull
    @Override
    public <F extends PsiElement> PsiLooping<T> recurse(@NotNull final Class<F> clazz, @NotNull final Predicate<? super F> predicate) {
        return (PsiLooping<T>) super.recurse(clazz, predicate);
    }

    @NotNull
    @Override
    public PsiLooping<T> noRecurse(@NotNull final Predicate<? super PsiElement> predicate) {
        return (PsiLooping<T>) super.noRecurse(predicate);
    }

    @NotNull
    @Override
    public PsiLooping<T> noRecurse(@NotNull final Class clazz) {
        return (PsiLooping<T>) super.noRecurse(clazz);
    }

    @NotNull
    @Override
    public <F extends PsiElement> PsiLooping<T> noRecurse(@NotNull final Class<F> clazz, @NotNull final Predicate<? super F> predicate) {
        return (PsiLooping<T>) super.noRecurse(clazz, predicate);
    }

    @NotNull
    @Override
    public PsiLooping<T> filterFalse() {
        return (PsiLooping<T>) super.filterFalse();
    }

    @NotNull
    @Override
    public PsiLooping<T> aborted() {
        return (PsiLooping<T>) super.aborted();
    }

    @NotNull
    @Override
    public PsiLooping<T> filterOut(@NotNull final Predicate<? super PsiElement> predicate) {
        return (PsiLooping<T>) super.filterOut(predicate);
    }

    @NotNull
    @Override
    public PsiLooping<T> filterOut(@NotNull final Class clazz) {
        return (PsiLooping<T>) super.filterOut(clazz);
    }

    @NotNull
    @Override
    public <F extends PsiElement> PsiLooping<T> filterOut(@NotNull final Class<F> clazz, @NotNull final Predicate<? super F> predicate) {
        return (PsiLooping<T>) super.filterOut(clazz, predicate);
    }

    @NotNull
    @Override
    public PsiLooping<T> filter(@NotNull final Predicate<? super PsiElement> predicate) {
        return (PsiLooping<T>) super.filter(predicate);
    }

    @NotNull
    @Override
    public PsiLooping<T> acceptFilter(@NotNull final ValueLoopFilter<? super T> filter) {
        return (PsiLooping<T>) super.acceptFilter(filter);
    }

    // *******************************************************
    //
    // Mapping Functions
    //
    // *******************************************************

    @NotNull
    @Override
    public <F extends PsiElement> PsiLooping<F> filter(@NotNull final Class<F> clazz) {
        return (PsiLooping<F>) super.filter(clazz);
    }

    @NotNull
    @Override
    public <F extends PsiElement> PsiLooping<F> filter(@NotNull final Class<F> clazz, @NotNull final Predicate<? super F> predicate) {
        return (PsiLooping<F>) super.filter(clazz, predicate);
    }

    @NotNull
    @Override
    public <F extends PsiElement> PsiLooping<F> adapt(@NotNull final Function<? super T, F> adapter) {
        return (PsiLooping<F>) super.adapt(adapter);
    }

    @NotNull
    @Override
    public <F extends PsiElement> PsiLooping<F> adapt(@NotNull final ValueLoopAdapter<? super T, F> adapter) {
        return (PsiLooping<F>) super.adapt(adapter);
    }

    // *******************************************************
    //
    // PsiLooping specific
    //
    // *******************************************************

    @NotNull
    public PsiLooping<T> recurse(@NotNull TokenSet tokenSet) {
        return recurse(it -> PsiUtils.isTypeOf(it, tokenSet));
    }

    @NotNull
    public PsiLooping<T> filterOut(@NotNull TokenSet tokenSet) {
        return filterOut(it -> PsiUtils.isNullOrTypeOf(it, tokenSet));
    }

    @NotNull
    public PsiLooping<T> filter(@NotNull TokenSet tokenSet) {
        return filter(it -> PsiUtils.isTypeOf(it, tokenSet));
    }

    @NotNull
    public PsiLooping<T> filterOutLeafPsi() {
        return filterOut(LeafPsiElement.class);
    }

    @NotNull
    public MappedLooping<Object, PsiElement> toPsiObjectMapped() {
        return toObjectMapped(PsiElement.class);
    }

    @NotNull
    public MappedLooping<Object, ASTNode> toAstObjectMapped() {
        return toPsiObjectMapped().adapt(PsiElement::getNode);
    }

    @NotNull
    public <A extends ASTNode> MappedLooping<Object, A> toAstObjectMapped(Class<A> clazz) {
        return toAstObjectMapped().filter(clazz);
    }

    // *******************************************************
    //
    // Static Factories
    //
    // *******************************************************

    public static PsiLooping<PsiElement> of(final @NotNull PsiElement element, final @NotNull Looping<PsiElement> looping) {
        return new PsiLooping<>(element, ValueLoopAdapterImpl.of(), looping);
    }

    public static PsiLooping<PsiElement> of(final @NotNull PsiElement element, final @NotNull LoopConstraints<PsiElement> constraints) {
        return of(element, new Looping<>(constraints));
    }

    public static PsiLooping<PsiElement> of(final @NotNull PsiElement element, final @NotNull LoopConstraints<PsiElement> constraints, final @NotNull Predicate<? super PsiElement> filter) {
        return of(element, new Looping<>(constraints, filter));
    }

    public static PsiLooping<PsiElement> of(final @NotNull PsiElement element, final @NotNull LoopConstraints<PsiElement> constraints, final @NotNull Predicate<? super PsiElement> filter, final @NotNull Predicate<? super PsiElement> recursion) {
        return of(element, new Looping<>(constraints, filter, recursion));
    }
}
