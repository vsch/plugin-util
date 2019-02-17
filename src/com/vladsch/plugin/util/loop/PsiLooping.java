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
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Predicate;

public class PsiLooping<T extends PsiElement> extends TypedLooping<PsiElement, T, PsiLooping<T>> {
    /*
     * Static Factories
     */

    public static PsiLooping<PsiElement> of(final @NotNull PsiElement element, final @NotNull Looping<PsiElement> looping) {
        return new PsiLooping<>(element, ValueLoopAdapterImpl.of(), looping);
    }

    public static PsiLooping<PsiElement> of(final @NotNull PsiElement element, final @NotNull LoopConstraints<PsiElement> constraints) {
        return of(element, new Looping<>(constraints));
    }

    public static PsiLooping<PsiElement> of(final @NotNull PsiElement element, final @NotNull LoopConstraints<PsiElement> constraints, final @NotNull Predicate<PsiElement> filter) {
        return of(element, new Looping<>(constraints, filter));
    }

    public static PsiLooping<PsiElement> of(final @NotNull PsiElement element, final @NotNull LoopConstraints<PsiElement> constraints, final @NotNull Predicate<PsiElement> filter, final @NotNull Predicate<PsiElement> recursion) {
        return of(element, new Looping<>(constraints, filter, recursion));
    }

    public PsiLooping(@NotNull final PsiElement element, @NotNull ValueLoopAdapter<PsiElement, T> adapter, @NotNull Looping<PsiElement> looping) {
        super(element, adapter, looping);
    }

    @NotNull
    final public MappedLooping<PsiElement, T> asMapped() {
        return new MappedLooping<>(myElement, myAdapter, myLooping);
    }

    @NotNull
    final public MorphedLooping<PsiElement, T> asMorphed() {
        return new MorphedLooping<>(myElement, myAdapter, myLooping);
    }

    @NotNull
    @Override
    final public PsiLooping<T> getModifiedCopy(final PsiElement element, final ValueLoopAdapter<PsiElement, T> adapter, final Looping<PsiElement> looping) {
        return getModifiedCopyF(element, adapter, looping);
    }

    @NotNull
    public <F extends PsiElement> PsiLooping<F> getModifiedCopyF(final PsiElement element, final ValueLoopAdapter<PsiElement, F> adapter, final Looping<PsiElement> looping) {
        return new PsiLooping<>(element, adapter, looping);
    }

    @NotNull
    public <F extends PsiElement> PsiLooping<F> filter(@NotNull Class<F> clazz) {
        return getModifiedCopyF(myElement, myAdapter.andThen(ValueLoopAdapterImpl.of(clazz)), myLooping);
    }

    @NotNull
    public <F extends PsiElement> PsiLooping<F> filter(@NotNull Class<F> clazz, @NotNull Predicate<F> predicate) {
        return getModifiedCopyF(myElement, myAdapter.andThen(ValueLoopAdapterImpl.of(clazz, predicate)), myLooping);
    }

    @NotNull
    public <F extends PsiElement> PsiLooping<F> filter(@NotNull Function<T, F> adapter) {
        return getModifiedCopyF(myElement, myAdapter.andThen(ValueLoopAdapterImpl.of(adapter)), myLooping);
    }

    @NotNull
    public <F extends PsiElement> PsiLooping<F> filter(@NotNull ValueLoopAdapter<T, F> adapter) {
        return getModifiedCopyF(myElement, myAdapter.andThen(adapter), myLooping);
    }

    /*
     * Psi Only
     */

    @NotNull
    public PsiLooping<T> recurse(@NotNull TokenSet tokenSet) {
        return getModifiedCopyF(myElement, myAdapter, myLooping.recurse(it -> it.getNode() != null && tokenSet.contains(it.getNode().getElementType())));
    }

    @NotNull
    public PsiLooping<T> filterOut(@NotNull TokenSet tokenSet) {
        return getModifiedCopyF(myElement, myAdapter, myLooping.filterOut(it -> it.getNode() != null && tokenSet.contains(it.getNode().getElementType())));
    }

    @NotNull
    public PsiLooping<T> filter(@NotNull TokenSet tokenSet) {
        return getModifiedCopyF(myElement, myAdapter, myLooping.filter(it -> it.getNode() != null && tokenSet.contains(it.getNode().getElementType())));
    }

    @NotNull
    public PsiLooping<T> filterOutLeafPsi() {
        return getModifiedCopyF(myElement, myAdapter, myLooping.filterOut(LeafPsiElement.class));
    }
}
