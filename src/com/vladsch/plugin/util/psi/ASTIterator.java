package com.vladsch.plugin.util.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.vladsch.tree.iteration.IterationConditions;
import com.vladsch.tree.iteration.TreeIterator;
import com.vladsch.tree.iteration.MappedIterator;
import com.vladsch.tree.iteration.ValueIterationAdapter;
import com.vladsch.tree.iteration.ValueIterationAdapterImpl;
import com.vladsch.tree.iteration.ValueIterationFilter;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Predicate;

public class ASTIterator<T extends ASTNode> extends MappedIterator<ASTNode, T> {
    public ASTIterator(@NotNull final ASTNode element, @NotNull ValueIterationAdapter<? super ASTNode, T> adapter, @NotNull TreeIterator<ASTNode> treeIterator) {
        super(element, adapter, treeIterator);
    }

    // *******************************************************
    //
    // Need Subclass Constructors
    //
    // *******************************************************

    @NotNull
    public ASTIterator<T> getModifiedCopy(final ASTNode element, final ValueIterationAdapter<? super ASTNode, T> adapter, final TreeIterator<ASTNode> treeIterator) {
        return new ASTIterator<>(element, adapter, treeIterator);
    }

    @NotNull
    public <F extends ASTNode> ASTIterator<F> getModifiedCopyF(final ASTNode element, final ValueIterationAdapter<? super ASTNode, F> adapter, final TreeIterator<ASTNode> treeIterator) {
        return new ASTIterator<>(element, adapter, treeIterator);
    }

    // *******************************************************
    //
    // Need Overrides with cast to sub-class
    //
    // *******************************************************

    @NotNull
    @Override
    public ASTIterator<T> reversed() {
        return (ASTIterator<T>) super.reversed();
    }

    @NotNull
    @Override
    public ASTIterator<T> recursive() {
        return (ASTIterator<T>) super.recursive();
    }

    @NotNull
    @Override
    public ASTIterator<T> nonRecursive() {
        return (ASTIterator<T>) super.nonRecursive();
    }

    @NotNull
    @Override
    public ASTIterator<T> recursive(final boolean recursive) {
        return (ASTIterator<T>) super.recursive(recursive);
    }

    @NotNull
    @Override
    public ASTIterator<T> nonRecursive(final boolean nonRecursive) {
        return (ASTIterator<T>) super.nonRecursive(nonRecursive);
    }

    @NotNull
    @Override
    public ASTIterator<T> recurse(@NotNull final Predicate<? super ASTNode> predicate) {
        return (ASTIterator<T>) super.recurse(predicate);
    }

    @NotNull
    @Override
    public ASTIterator<T> recurse(@NotNull final Class clazz) {
        return (ASTIterator<T>) super.recurse(clazz);
    }

    @NotNull
    @Override
    public <F extends ASTNode> ASTIterator<T> recurse(@NotNull final Class<F> clazz, @NotNull final Predicate<? super F> predicate) {
        return (ASTIterator<T>) super.recurse(clazz, predicate);
    }

    @NotNull
    @Override
    public ASTIterator<T> noRecurse(@NotNull final Predicate<? super ASTNode> predicate) {
        return (ASTIterator<T>) super.noRecurse(predicate);
    }

    @NotNull
    @Override
    public ASTIterator<T> noRecurse(@NotNull final Class clazz) {
        return (ASTIterator<T>) super.noRecurse(clazz);
    }

    @NotNull
    @Override
    public <F extends ASTNode> ASTIterator<T> noRecurse(@NotNull final Class<F> clazz, @NotNull final Predicate<? super F> predicate) {
        return (ASTIterator<T>) super.noRecurse(clazz, predicate);
    }

    @NotNull
    @Override
    public ASTIterator<T> filterFalse() {
        return (ASTIterator<T>) super.filterFalse();
    }

    @NotNull
    @Override
    public ASTIterator<T> aborted() {
        return (ASTIterator<T>) super.aborted();
    }

    @NotNull
    @Override
    public ASTIterator<T> filterOut(@NotNull final Predicate<? super ASTNode> predicate) {
        return (ASTIterator<T>) super.filterOut(predicate);
    }

    @NotNull
    @Override
    public ASTIterator<T> filterOut(@NotNull final Class clazz) {
        return (ASTIterator<T>) super.filterOut(clazz);
    }

    @NotNull
    @Override
    public <F extends ASTNode> ASTIterator<T> filterOut(@NotNull final Class<F> clazz, @NotNull final Predicate<? super F> predicate) {
        return (ASTIterator<T>) super.filterOut(clazz, predicate);
    }

    @NotNull
    @Override
    public ASTIterator<T> filter(@NotNull final Predicate<? super ASTNode> predicate) {
        return (ASTIterator<T>) super.filter(predicate);
    }

    @NotNull
    @Override
    public ASTIterator<T> acceptFilter(@NotNull final ValueIterationFilter<? super T> filter) {
        return (ASTIterator<T>) super.acceptFilter(filter);
    }

    // *******************************************************
    //
    // Mapping Functions
    //
    // *******************************************************

    @NotNull
    @Override
    public <F extends ASTNode> ASTIterator<F> filter(@NotNull final Class<F> clazz) {
        return (ASTIterator<F>) super.filter(clazz);
    }

    @NotNull
    @Override
    public <F extends ASTNode> ASTIterator<F> filter(@NotNull final Class<F> clazz, @NotNull final Predicate<? super F> predicate) {
        return (ASTIterator<F>) super.filter(clazz, predicate);
    }

    @NotNull
    @Override
    public <F extends ASTNode> ASTIterator<F> adapt(@NotNull final Function<? super T, F> adapter) {
        return (ASTIterator<F>) super.adapt(adapter);
    }

    @NotNull
    @Override
    public <F extends ASTNode> ASTIterator<F> adapt(@NotNull final ValueIterationAdapter<? super T, F> adapter) {
        return (ASTIterator<F>) super.adapt(adapter);
    }

    // *******************************************************
    //
    // Psi/AST Looping specific
    //
    // *******************************************************

    @NotNull
    public ASTIterator<T> recurse(@NotNull TokenSet tokenSet) {
        return getModifiedCopyF(myElement, myAdapter, myTreeIterator.recurse(it -> PsiUtils.isTypeOf(it, tokenSet)));
    }

    @NotNull
    public ASTIterator<T> filterOut(@NotNull TokenSet tokenSet) {
        return getModifiedCopyF(myElement, myAdapter, myTreeIterator.filterOut(it -> PsiUtils.isNullOrTypeOf(it, tokenSet)));
    }

    @NotNull
    public ASTIterator<T> filter(@NotNull TokenSet tokenSet) {
        return getModifiedCopyF(myElement, myAdapter, myTreeIterator.filter(it -> PsiUtils.isTypeOf(it, tokenSet)));
    }

    @NotNull
    public MappedIterator<Object, ASTNode> toAstObjectMapped() {
        return toObjectMapped(ASTNode.class);
    }

    // *******************************************************
    //
    // Static Factories
    //
    // *******************************************************

    public static ASTIterator<ASTNode> of(final @NotNull ASTNode element, final @NotNull TreeIterator<ASTNode> treeIterator) {
        return new ASTIterator<>(element, ValueIterationAdapterImpl.of(), treeIterator);
    }

    public static ASTIterator<ASTNode> of(final @NotNull ASTNode element, final @NotNull IterationConditions<ASTNode> constraints) {
        return of(element, new TreeIterator<>(constraints));
    }

    public static ASTIterator<ASTNode> of(final @NotNull ASTNode element, final @NotNull IterationConditions<ASTNode> constraints, final @NotNull Predicate<? super ASTNode> filter) {
        return of(element, new TreeIterator<>(constraints, filter));
    }

    public static ASTIterator<ASTNode> of(final @NotNull ASTNode element, final @NotNull IterationConditions<ASTNode> constraints, final @NotNull Predicate<? super ASTNode> filter, final @NotNull Predicate<? super ASTNode> recursion) {
        return of(element, new TreeIterator<>(constraints, filter, recursion));
    }
}

