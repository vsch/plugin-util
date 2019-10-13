package com.vladsch.plugin.util.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.vladsch.flexmark.tree.iteration.FixedIterationConditions;
import com.vladsch.flexmark.tree.iteration.IterationConditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class TreeIteratorConstrains<N> {
    public static final Predicate<Object> NOT_LEAF_PSI = n -> n instanceof LeafPsiElement;
    public static final Predicate<Object> LEAF_PSI = n -> !(n instanceof LeafPsiElement);

    final Function<? super N, N> NEXT_SIBLING;
    final Function<? super N, N> PREV_SIBLING;
    final Function<? super N, N> FIRST_CHILD;
    final Function<? super N, N> LAST_CHILD;
    final Function<? super N, N> PARENT;

    final public static int ITERATE_CHILDREN = 0;
    final public static int ITERATE_CHILDREN_REV = 1;
    final public static int ITERATE_SIBLINGS = 2;
    final public static int ITERATE_SIBLINGS_REV = 3;
    final public static int MAX_CONSTRAINTS = 4;

    static class IterationFunctions<N> {
        final int builderIndex;
        @NotNull final Function<? super N, N> initializer;
        @NotNull final Function<? super N, N> iterator;
        @NotNull final Function<? super N, N> reverseInitializer;
        @NotNull final Function<? super N, N> reverseIterator;

        public IterationFunctions(final int builderIndex, @NotNull final Function<? super N, N> initializer, @NotNull final Function<? super N, N> iterator, @NotNull final Function<? super N, N> reverseInitializer, @NotNull final Function<? super N, N> reverseIterator) {
            this.builderIndex = builderIndex;
            this.initializer = initializer;
            this.iterator = iterator;
            this.reverseInitializer = reverseInitializer;
            this.reverseIterator = reverseIterator;
        }
    }

    @SuppressWarnings("rawtypes") final IterationFunctions[] ourIterators;

    private TreeIteratorConstrains(final Function<? super N, N> NEXT_SIBLING, final Function<? super N, N> PREV_SIBLING, final Function<? super N, N> FIRST_CHILD, final Function<? super N, N> LAST_CHILD, final Function<? super N, N> PARENT) {
        this.NEXT_SIBLING = NEXT_SIBLING;
        this.PREV_SIBLING = PREV_SIBLING;
        this.FIRST_CHILD = FIRST_CHILD;
        this.LAST_CHILD = LAST_CHILD;
        this.PARENT = PARENT;

        ourIterators = new IterationFunctions[MAX_CONSTRAINTS];
        ourIterators[ITERATE_CHILDREN] = new IterationFunctions<>(ITERATE_CHILDREN, FIRST_CHILD, NEXT_SIBLING, LAST_CHILD, PREV_SIBLING);
        ourIterators[ITERATE_CHILDREN_REV] = new IterationFunctions<>(ITERATE_CHILDREN_REV, LAST_CHILD, PREV_SIBLING, FIRST_CHILD, NEXT_SIBLING);
        ourIterators[ITERATE_SIBLINGS] = new IterationFunctions<>(ITERATE_SIBLINGS, NEXT_SIBLING, NEXT_SIBLING, PREV_SIBLING, PREV_SIBLING);
        ourIterators[ITERATE_SIBLINGS_REV] = new IterationFunctions<>(ITERATE_SIBLINGS_REV, PREV_SIBLING, PREV_SIBLING, NEXT_SIBLING, NEXT_SIBLING);
    }

    @SuppressWarnings("rawtypes") final IterationConditions[] myConstraints = new IterationConditions[MAX_CONSTRAINTS];

    IterationConditions<N> getOrComputeConstraint(int index) {
        //noinspection rawtypes
        IterationConditions constraints = myConstraints[index];
        if (constraints == null) {
            //noinspection unchecked
            IterationFunctions<N> iterator = ourIterators[index];

            constraints = new FixedIterationConditions<>(iterator.initializer, iterator.iterator, iterator.reverseInitializer, iterator.reverseIterator);
            myConstraints[index] = constraints;
        }

        //noinspection unchecked
        return constraints;
    }

    public IterationConditions<N> getIterateSiblings() {
        return getOrComputeConstraint(ITERATE_SIBLINGS);
    }

    public IterationConditions<N> getIterateSiblingsRev() {
        return getOrComputeConstraint(ITERATE_SIBLINGS_REV);
    }

    public IterationConditions<N> getIterateChildren() {
        return getOrComputeConstraint(ITERATE_CHILDREN);
    }

    public IterationConditions<N> getIterateChildrenRev() {
        return getOrComputeConstraint(ITERATE_CHILDREN_REV);
    }

    public static <T> Function<T, T> NULL() {
        return n -> null;
    }

    @SuppressWarnings("rawtypes") final private static HashMap<Class<?>, TreeIteratorConstrains> ourCachedBuilders = new HashMap<>();
    final public static Function<? super PsiElement, PsiElement> PSI_NEXT_SIBLING = element -> element.isValid() ? element.getNextSibling() : null;
    final public static Function<? super PsiElement, PsiElement> PSI_PREV_SIBLING = element -> element.isValid() ? element.getPrevSibling() : null;
    final public static Function<? super PsiElement, PsiElement> PSI_FIRST_CHILD = element -> element.isValid() ? element.getFirstChild() : null;
    final public static Function<? super PsiElement, PsiElement> PSI_LAST_CHILD = element -> element.isValid() ? element.getLastChild() : null;
    final public static Function<? super PsiElement, PsiElement> PSI_PARENT = element -> element.isValid() ? element.getParent() : null;

    final public static Function<? super ASTNode, ASTNode> AST_NEXT_SIBLING = ASTNode::getTreeNext;
    final public static Function<? super ASTNode, ASTNode> AST_PREV_SIBLING = ASTNode::getTreePrev;
    final public static Function<? super ASTNode, ASTNode> AST_FIRST_CHILD = ASTNode::getFirstChildNode;
    final public static Function<? super ASTNode, ASTNode> AST_LAST_CHILD = ASTNode::getLastChildNode;
    final public static Function<? super ASTNode, ASTNode> AST_PARENT = ASTNode::getTreeParent;

    final public static TreeIteratorConstrains<PsiElement> PSI_LOOPS = new TreeIteratorConstrains<>(PSI_NEXT_SIBLING, PSI_PREV_SIBLING, PSI_FIRST_CHILD, PSI_LAST_CHILD, PSI_PARENT);
    public static final TreeIteratorConstrains<PsiElement> PSI = PSI_LOOPS;
    public static final TreeIteratorConstrains<ASTNode> AST_LOOPS = new TreeIteratorConstrains<>(AST_NEXT_SIBLING, AST_PREV_SIBLING, AST_FIRST_CHILD, AST_LAST_CHILD, AST_PARENT);
    public static final TreeIteratorConstrains<ASTNode> AST = AST_LOOPS;
    static {
        ourCachedBuilders.put(PsiElement.class, PSI_LOOPS);
        ourCachedBuilders.put(ASTNode.class, AST_LOOPS);
    }
    @Nullable
    public static <N> TreeIteratorConstrains<N> getFor(Class<N> clazz) {
        //noinspection unchecked
        return ourCachedBuilders.get(clazz);
    }

    @NotNull
    public static <N> TreeIteratorConstrains<N> createFor(Class<N> clazz, final Function<? super N, N> NEXT_SIBLING, final Function<? super N, N> PREV_SIBLING, final Function<? super N, N> FIRST_CHILD, final Function<? super N, N> LAST_CHILD, final Function<? super N, N> PARENT) {
        // TODO: decide if need to access builders by sub-class then on first subclass add the subclass entry for the superclass builder
        //LoopConstrainsBuilder builder = ourCachedBuilders.get(clazz);
        //if (builder == null) {
        //    // see if there are super classes defined or interfaces
        //    clazz.getSuperclass();
        //    clazz.getInterfaces();
        //}

        //noinspection unchecked
        return ourCachedBuilders.computeIfAbsent(clazz, (c) -> new TreeIteratorConstrains<>(NEXT_SIBLING, PREV_SIBLING, FIRST_CHILD, LAST_CHILD, PARENT));
    }
}
