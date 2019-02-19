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
import com.intellij.psi.tree.TokenSet;
import com.vladsch.plugin.util.tree.IterationConstraints;
import com.vladsch.plugin.util.tree.TreeIterator;
import com.vladsch.plugin.util.tree.MappedLooping;
import com.vladsch.plugin.util.tree.ValueLoopAdapter;
import com.vladsch.plugin.util.tree.ValueLoopAdapterImpl;
import com.vladsch.plugin.util.tree.ValueLoopFilter;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Predicate;

public class ASTLooping<T extends ASTNode> extends MappedLooping<ASTNode, T> {
    public ASTLooping(@NotNull final ASTNode element, @NotNull ValueLoopAdapter<? super ASTNode, T> adapter, @NotNull TreeIterator<ASTNode> treeIterator) {
        super(element, adapter, treeIterator);
    }

    // *******************************************************
    //
    // Need Subclass Constructors 
    //
    // *******************************************************

    @NotNull
    public ASTLooping<T> getModifiedCopy(final ASTNode element, final ValueLoopAdapter<? super ASTNode, T> adapter, final TreeIterator<ASTNode> treeIterator) {
        return new ASTLooping<>(element, adapter, treeIterator);
    }

    @NotNull
    public <F extends ASTNode> ASTLooping<F> getModifiedCopyF(final ASTNode element, final ValueLoopAdapter<? super ASTNode, F> adapter, final TreeIterator<ASTNode> treeIterator) {
        return new ASTLooping<>(element, adapter, treeIterator);
    }

    // *******************************************************
    //
    // Need Overrides with cast to sub-class
    //
    // *******************************************************

    @NotNull
    @Override
    public ASTLooping<T> reversed() {
        return (ASTLooping<T>) super.reversed();
    }

    @NotNull
    @Override
    public ASTLooping<T> recursive() {
        return (ASTLooping<T>) super.recursive();
    }

    @NotNull
    @Override
    public ASTLooping<T> nonRecursive() {
        return (ASTLooping<T>) super.nonRecursive();
    }

    @NotNull
    @Override
    public ASTLooping<T> recursive(final boolean recursive) {
        return (ASTLooping<T>) super.recursive(recursive);
    }

    @NotNull
    @Override
    public ASTLooping<T> nonRecursive(final boolean nonRecursive) {
        return (ASTLooping<T>) super.nonRecursive(nonRecursive);
    }

    @NotNull
    @Override
    public ASTLooping<T> recurse(@NotNull final Predicate<? super ASTNode> predicate) {
        return (ASTLooping<T>) super.recurse(predicate);
    }

    @NotNull
    @Override
    public ASTLooping<T> recurse(@NotNull final Class clazz) {
        return (ASTLooping<T>) super.recurse(clazz);
    }

    @NotNull
    @Override
    public <F extends ASTNode> ASTLooping<T> recurse(@NotNull final Class<F> clazz, @NotNull final Predicate<? super F> predicate) {
        return (ASTLooping<T>) super.recurse(clazz, predicate);
    }

    @NotNull
    @Override
    public ASTLooping<T> noRecurse(@NotNull final Predicate<? super ASTNode> predicate) {
        return (ASTLooping<T>) super.noRecurse(predicate);
    }

    @NotNull
    @Override
    public ASTLooping<T> noRecurse(@NotNull final Class clazz) {
        return (ASTLooping<T>) super.noRecurse(clazz);
    }

    @NotNull
    @Override
    public <F extends ASTNode> ASTLooping<T> noRecurse(@NotNull final Class<F> clazz, @NotNull final Predicate<? super F> predicate) {
        return (ASTLooping<T>) super.noRecurse(clazz, predicate);
    }

    @NotNull
    @Override
    public ASTLooping<T> filterFalse() {
        return (ASTLooping<T>) super.filterFalse();
    }

    @NotNull
    @Override
    public ASTLooping<T> aborted() {
        return (ASTLooping<T>) super.aborted();
    }

    @NotNull
    @Override
    public ASTLooping<T> filterOut(@NotNull final Predicate<? super ASTNode> predicate) {
        return (ASTLooping<T>) super.filterOut(predicate);
    }

    @NotNull
    @Override
    public ASTLooping<T> filterOut(@NotNull final Class clazz) {
        return (ASTLooping<T>) super.filterOut(clazz);
    }

    @NotNull
    @Override
    public <F extends ASTNode> ASTLooping<T> filterOut(@NotNull final Class<F> clazz, @NotNull final Predicate<? super F> predicate) {
        return (ASTLooping<T>) super.filterOut(clazz, predicate);
    }

    @NotNull
    @Override
    public ASTLooping<T> filter(@NotNull final Predicate<? super ASTNode> predicate) {
        return (ASTLooping<T>) super.filter(predicate);
    }

    @NotNull
    @Override
    public ASTLooping<T> acceptFilter(@NotNull final ValueLoopFilter<? super T> filter) {
        return (ASTLooping<T>) super.acceptFilter(filter);
    }

    // *******************************************************
    //
    // Mapping Functions
    //
    // *******************************************************

    @NotNull
    @Override
    public <F extends ASTNode> ASTLooping<F> filter(@NotNull final Class<F> clazz) {
        return (ASTLooping<F>) super.filter(clazz);
    }

    @NotNull
    @Override
    public <F extends ASTNode> ASTLooping<F> filter(@NotNull final Class<F> clazz, @NotNull final Predicate<? super F> predicate) {
        return (ASTLooping<F>) super.filter(clazz, predicate);
    }

    @NotNull
    @Override
    public <F extends ASTNode> ASTLooping<F> adapt(@NotNull final Function<? super T, F> adapter) {
        return (ASTLooping<F>) super.adapt(adapter);
    }

    @NotNull
    @Override
    public <F extends ASTNode> ASTLooping<F> adapt(@NotNull final ValueLoopAdapter<? super T, F> adapter) {
        return (ASTLooping<F>) super.adapt(adapter);
    }

    // *******************************************************
    //
    // Psi/AST Looping specific
    //
    // *******************************************************

    @NotNull
    public ASTLooping<T> recurse(@NotNull TokenSet tokenSet) {
        return getModifiedCopyF(myElement, myAdapter, myTreeIterator.recurse(it -> PsiUtils.isTypeOf(it, tokenSet)));
    }

    @NotNull
    public ASTLooping<T> filterOut(@NotNull TokenSet tokenSet) {
        return getModifiedCopyF(myElement, myAdapter, myTreeIterator.filterOut(it -> PsiUtils.isNullOrTypeOf(it, tokenSet)));
    }

    @NotNull
    public ASTLooping<T> filter(@NotNull TokenSet tokenSet) {
        return getModifiedCopyF(myElement, myAdapter, myTreeIterator.filter(it -> PsiUtils.isTypeOf(it, tokenSet)));
    }

    @NotNull
    public MappedLooping<Object, ASTNode> toAstObjectMapped() {
        return toObjectMapped(ASTNode.class);
    }

    // *******************************************************
    //
    // Static Factories
    //
    // *******************************************************

    public static ASTLooping<ASTNode> of(final @NotNull ASTNode element, final @NotNull TreeIterator<ASTNode> treeIterator) {
        return new ASTLooping<>(element, ValueLoopAdapterImpl.of(), treeIterator);
    }

    public static ASTLooping<ASTNode> of(final @NotNull ASTNode element, final @NotNull IterationConstraints<ASTNode> constraints) {
        return of(element, new TreeIterator<>(constraints));
    }

    public static ASTLooping<ASTNode> of(final @NotNull ASTNode element, final @NotNull IterationConstraints<ASTNode> constraints, final @NotNull Predicate<? super ASTNode> filter) {
        return of(element, new TreeIterator<>(constraints, filter));
    }

    public static ASTLooping<ASTNode> of(final @NotNull ASTNode element, final @NotNull IterationConstraints<ASTNode> constraints, final @NotNull Predicate<? super ASTNode> filter, final @NotNull Predicate<? super ASTNode> recursion) {
        return of(element, new TreeIterator<>(constraints, filter, recursion));
    }
}
                                                                        
