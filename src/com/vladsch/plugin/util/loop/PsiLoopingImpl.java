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

public class PsiLoopingImpl<T extends PsiElement> implements PsiLooping<T> {
    private final @NotNull PsiElement myElement;
    private final @NotNull ValueLoopAdapter<PsiElement, T> myAdapter;
    private final @NotNull LoopingImpl<PsiElement> myLooping;

    public PsiLoopingImpl(@NotNull final PsiElement element, @NotNull ValueLoopAdapter<PsiElement, T> adapter, @NotNull LoopingImpl<PsiElement> looping) {
        myElement = element;
        myAdapter = adapter;
        myLooping = looping;
    }

    @Override
    @NotNull
    public LoopingImpl<PsiElement> getLooping() {
        return myLooping;
    }

    @NotNull
    @Override
    public PsiLooping<T> reversed() {
        return new PsiLoopingImpl<>(myElement, myAdapter, myLooping.reversed());
    }

    @NotNull
    @Override
    public PsiLooping<T> recursive() {
        return new PsiLoopingImpl<>(myElement, myAdapter, myLooping.recursive());
    }

    @NotNull
    @Override
    public PsiLooping<T> nonRecursive() {
        return new PsiLoopingImpl<>(myElement, myAdapter, myLooping.nonRecursive());
    }

    @NotNull
    @Override
    public PsiLooping<T> recurse(@NotNull final Predicate<PsiElement> predicate) {
        return new PsiLoopingImpl<>(myElement, myAdapter, myLooping.recurse(predicate));
    }

    @NotNull
    @Override
    public PsiLooping<T> recurse(@NotNull final Class clazz) {
        return new PsiLoopingImpl<>(myElement, myAdapter, myLooping.recurse(clazz));
    }

    @NotNull
    @Override
    public <F> PsiLooping<T> recurse(@NotNull final Class<F> clazz, @NotNull final Predicate<F> predicate) {
        return new PsiLoopingImpl<>(myElement, myAdapter, myLooping.recurse(clazz, predicate));
    }

    @NotNull
    @Override
    public PsiLooping<T> filterFalse() {
        return aborted();
    }

    @NotNull
    @Override
    public PsiLooping<T> aborted() {
        return new PsiLoopingImpl<>(myElement, myAdapter, myLooping.aborted());
    }

    @NotNull
    @Override
    public PsiLooping<T> filterOut(@NotNull final Predicate<PsiElement> predicate) {
        return new PsiLoopingImpl<>(myElement, myAdapter, myLooping.filterOut(predicate));
    }

    @NotNull
    @Override
    public PsiLooping<T> filterOut(@NotNull Class clazz) {
        return new PsiLoopingImpl<>(myElement, myAdapter, myLooping.filterOut(clazz));
    }

    @NotNull
    @Override
    public <F> PsiLooping<T> filterOut(@NotNull Class<F> clazz, @NotNull Predicate<F> predicate) {
        return new PsiLoopingImpl<>(myElement, myAdapter, myLooping.filterOut(clazz, predicate));
    }

    @NotNull
    @Override
    public PsiLooping<T> filter(@NotNull final Predicate<PsiElement> predicate) {
        return new PsiLoopingImpl<>(myElement, myAdapter, myLooping.filter(predicate));
    }

    @NotNull
    @Override
    public <F extends PsiElement> PsiLooping<F> filter(@NotNull Class<F> clazz) {
        return new PsiLoopingImpl<>(myElement, myAdapter.andThen(ValueLoopAdapterImpl.of(clazz)), myLooping);
    }

    @NotNull
    @Override
    public <F extends PsiElement> PsiLooping<F> filter(@NotNull Class<F> clazz, @NotNull Predicate<F> predicate) {
        return new PsiLoopingImpl<>(myElement, myAdapter.andThen(ValueLoopAdapterImpl.of(clazz, predicate)), myLooping);
    }

    @NotNull
    @Override
    public <F extends PsiElement> PsiLooping<F> filter(@NotNull Function<T, F> adapter) {
        return new PsiLoopingImpl<>(myElement, myAdapter.andThen(ValueLoopAdapterImpl.of(adapter)), myLooping);
    }

    @NotNull
    @Override
    public <F extends PsiElement> PsiLooping<F> filter(@NotNull ValueLoopAdapter<T, F> adapter) {
        return new PsiLoopingImpl<>(myElement, myAdapter.andThen(adapter), myLooping);
    }

    @NotNull
    @Override
    public PsiLooping<T> filter(@NotNull ValueLoopFilter<T> filter) {
        return new PsiLoopingImpl<>(myElement, myAdapter.andThen(ValueLoopAdapterImpl.of(filter)), myLooping);
    }

    /*
     * Psi Only
     */

    @NotNull
    public PsiLooping<T> recurse(@NotNull TokenSet tokenSet) {
        return new PsiLoopingImpl<>(myElement, myAdapter, myLooping.recurse(it -> it.getNode() != null && tokenSet.contains(it.getNode().getElementType())));
    }

    @NotNull
    public PsiLooping<T> filterOut(@NotNull TokenSet tokenSet) {
        return new PsiLoopingImpl<>(myElement, myAdapter, myLooping.filterOut(it -> it.getNode() != null && tokenSet.contains(it.getNode().getElementType())));
    }

    @NotNull
    public PsiLooping<T> filter(@NotNull TokenSet tokenSet) {
        return new PsiLoopingImpl<>(myElement, myAdapter, myLooping.filter(it -> it.getNode() != null && tokenSet.contains(it.getNode().getElementType())));
    }

    @NotNull
    @Override
    public PsiLooping<T> filterOutLeafPsi() {
        return new PsiLoopingImpl<>(myElement, myAdapter, myLooping.filterOut(LeafPsiElement.class));
    }

    /*
     * Looping
     */

    @Override
    @NotNull
    public <R> R doLoop(@NotNull final R defaultValue, @NotNull final ValueLoopConsumer<T, R> consumer) {
        return myLooping.doLoop(myElement, defaultValue, myAdapter, consumer);
    }

    @Override
    public void doLoop(@NotNull final VoidLoopConsumer<T> consumer) {
        myLooping.doLoop(myElement, myAdapter, consumer);
    }

    /*
     * Static Factories
     */

    public static <R> PsiLooping<PsiElement> create(final PsiElement element, @NotNull LoopingImpl<PsiElement> looping) {
        return new PsiLoopingImpl<>(element, ValueLoopAdapterImpl.of(), looping);
    }
}
