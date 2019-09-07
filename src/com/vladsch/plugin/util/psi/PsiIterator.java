package com.vladsch.plugin.util.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.TokenSet;
import com.vladsch.tree.iteration.IterationConditions;
import com.vladsch.tree.iteration.MappedIterator;
import com.vladsch.tree.iteration.TreeIterator;
import com.vladsch.tree.iteration.ValueIterationAdapter;
import com.vladsch.tree.iteration.ValueIterationAdapterImpl;
import com.vladsch.tree.iteration.ValueIterationFilter;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Predicate;

public class PsiIterator<T extends PsiElement> extends MappedIterator<PsiElement, T> {
    public PsiIterator(@NotNull final PsiElement element, @NotNull ValueIterationAdapter<? super PsiElement, T> adapter, @NotNull TreeIterator<PsiElement> treeIterator) {
        super(element, adapter, treeIterator);
    }

    // *******************************************************
    //
    // Need Subclass Constructors
    //
    // *******************************************************

    @NotNull
    public PsiIterator<T> getModifiedCopy(final PsiElement element, final ValueIterationAdapter<? super PsiElement, T> adapter, final TreeIterator<PsiElement> treeIterator) {
        return new PsiIterator<>(element, adapter, treeIterator);
    }

    @NotNull
    public <F extends PsiElement> PsiIterator<F> getModifiedCopyF(final PsiElement element, final ValueIterationAdapter<? super PsiElement, F> adapter, final TreeIterator<PsiElement> treeIterator) {
        return new PsiIterator<>(element, adapter, treeIterator);
    }

    // *******************************************************
    //
    // Need Overrides with cast to sub-class
    //
    // *******************************************************

    @NotNull
    @Override
    public PsiIterator<T> reversed() {
        return (PsiIterator<T>) super.reversed();
    }

    @NotNull
    @Override
    public PsiIterator<T> recursive() {
        return (PsiIterator<T>) super.recursive();
    }

    @NotNull
    @Override
    public PsiIterator<T> nonRecursive() {
        return (PsiIterator<T>) super.nonRecursive();
    }

    @NotNull
    @Override
    public PsiIterator<T> recursive(final boolean recursive) {
        return (PsiIterator<T>) super.recursive(recursive);
    }

    @NotNull
    @Override
    public PsiIterator<T> nonRecursive(final boolean nonRecursive) {
        return (PsiIterator<T>) super.nonRecursive(nonRecursive);
    }

    @NotNull
    @Override
    public PsiIterator<T> recurse(@NotNull final Predicate<? super PsiElement> predicate) {
        return (PsiIterator<T>) super.recurse(predicate);
    }

    @NotNull
    @Override
    public PsiIterator<T> recurse(@NotNull final Class clazz) {
        return (PsiIterator<T>) super.recurse(clazz);
    }

    @NotNull
    @Override
    public <F extends PsiElement> PsiIterator<T> recurse(@NotNull final Class<F> clazz, @NotNull final Predicate<? super F> predicate) {
        return (PsiIterator<T>) super.recurse(clazz, predicate);
    }

    @NotNull
    @Override
    public PsiIterator<T> noRecurse(@NotNull final Predicate<? super PsiElement> predicate) {
        return (PsiIterator<T>) super.noRecurse(predicate);
    }

    @NotNull
    @Override
    public PsiIterator<T> noRecurse(@NotNull final Class clazz) {
        return (PsiIterator<T>) super.noRecurse(clazz);
    }

    @NotNull
    @Override
    public <F extends PsiElement> PsiIterator<T> noRecurse(@NotNull final Class<F> clazz, @NotNull final Predicate<? super F> predicate) {
        return (PsiIterator<T>) super.noRecurse(clazz, predicate);
    }

    @NotNull
    @Override
    public PsiIterator<T> filterFalse() {
        return (PsiIterator<T>) super.filterFalse();
    }

    @NotNull
    @Override
    public PsiIterator<T> aborted() {
        return (PsiIterator<T>) super.aborted();
    }

    @NotNull
    @Override
    public PsiIterator<T> filterOut(@NotNull final Predicate<? super PsiElement> predicate) {
        return (PsiIterator<T>) super.filterOut(predicate);
    }

    @NotNull
    @Override
    public PsiIterator<T> filterOut(@NotNull final Class clazz) {
        return (PsiIterator<T>) super.filterOut(clazz);
    }

    @NotNull
    @Override
    public <F extends PsiElement> PsiIterator<T> filterOut(@NotNull final Class<F> clazz, @NotNull final Predicate<? super F> predicate) {
        return (PsiIterator<T>) super.filterOut(clazz, predicate);
    }

    @NotNull
    @Override
    public PsiIterator<T> filter(@NotNull final Predicate<? super PsiElement> predicate) {
        return (PsiIterator<T>) super.filter(predicate);
    }

    @NotNull
    @Override
    public PsiIterator<T> acceptFilter(@NotNull final ValueIterationFilter<? super T> filter) {
        return (PsiIterator<T>) super.acceptFilter(filter);
    }

    // *******************************************************
    //
    // Mapping Functions
    //
    // *******************************************************

    @NotNull
    @Override
    public <F extends PsiElement> PsiIterator<F> filter(@NotNull final Class<F> clazz) {
        return (PsiIterator<F>) super.filter(clazz);
    }

    @NotNull
    @Override
    public <F extends PsiElement> PsiIterator<F> filter(@NotNull final Class<F> clazz, @NotNull final Predicate<? super F> predicate) {
        return (PsiIterator<F>) super.filter(clazz, predicate);
    }

    @NotNull
    @Override
    public <F extends PsiElement> PsiIterator<F> adapt(@NotNull final Function<? super T, F> adapter) {
        return (PsiIterator<F>) super.adapt(adapter);
    }

    @NotNull
    @Override
    public <F extends PsiElement> PsiIterator<F> adapt(@NotNull final ValueIterationAdapter<? super T, F> adapter) {
        return (PsiIterator<F>) super.adapt(adapter);
    }

    // *******************************************************
    //
    // PsiLooping specific
    //
    // *******************************************************

    @NotNull
    public PsiIterator<T> recurse(@NotNull TokenSet tokenSet) {
        return recurse(it -> PsiUtils.isTypeOf(it, tokenSet));
    }

    @NotNull
    public PsiIterator<T> filterOut(@NotNull TokenSet tokenSet) {
        return filterOut(it -> PsiUtils.isNullOrTypeOf(it, tokenSet));
    }

    @NotNull
    public PsiIterator<T> filter(@NotNull TokenSet tokenSet) {
        return filter(it -> PsiUtils.isTypeOf(it, tokenSet));
    }

    @NotNull
    public PsiIterator<T> filterOutLeafPsi() {
        return filterOut(LeafPsiElement.class);
    }

    @NotNull
    public MappedIterator<Object, PsiElement> toPsiObjectMapped() {
        return toObjectMapped(PsiElement.class);
    }

    @NotNull
    public MappedIterator<Object, ASTNode> toAstObjectMapped() {
        return toPsiObjectMapped().adapt(PsiElement::getNode);
    }

    @NotNull
    public <A extends ASTNode> MappedIterator<Object, A> toAstObjectMapped(Class<A> clazz) {
        return toAstObjectMapped().filter(clazz);
    }

    // *******************************************************
    //
    // Static Factories
    //
    // *******************************************************

    public static PsiIterator<PsiElement> of(final @NotNull PsiElement element, final @NotNull TreeIterator<PsiElement> treeIterator) {
        return new PsiIterator<>(element, ValueIterationAdapterImpl.of(), treeIterator);
    }

    public static PsiIterator<PsiElement> of(final @NotNull PsiElement element, final @NotNull IterationConditions<PsiElement> constraints) {
        return of(element, new TreeIterator<>(constraints));
    }

    public static PsiIterator<PsiElement> of(final @NotNull PsiElement element, final @NotNull IterationConditions<PsiElement> constraints, final @NotNull Predicate<? super PsiElement> filter) {
        return of(element, new TreeIterator<>(constraints, filter));
    }

    public static PsiIterator<PsiElement> of(final @NotNull PsiElement element, final @NotNull IterationConditions<PsiElement> constraints, final @NotNull Predicate<? super PsiElement> filter, final @NotNull Predicate<? super PsiElement> recursion) {
        return of(element, new TreeIterator<>(constraints, filter, recursion));
    }
}
