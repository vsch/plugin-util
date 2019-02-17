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

package com.vladsch.plugin.util.looping;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.function.Function;

public class LoopConstrainsBuilder<N> {
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

    final IterationFunctions[] ourIterators;

    private LoopConstrainsBuilder(final Function<? super N, N> NEXT_SIBLING, final Function<? super N, N> PREV_SIBLING, final Function<? super N, N> FIRST_CHILD, final Function<? super N, N> LAST_CHILD, final Function<? super N, N> PARENT) {
        this.NEXT_SIBLING = NEXT_SIBLING;
        this.PREV_SIBLING = PREV_SIBLING;
        this.FIRST_CHILD = FIRST_CHILD;
        this.LAST_CHILD = LAST_CHILD;
        this.PARENT = PARENT;

        ourIterators = new IterationFunctions[MAX_CONSTRAINTS];
        ourIterators[ITERATE_CHILDREN] = new IterationFunctions<N>(ITERATE_CHILDREN, FIRST_CHILD, NEXT_SIBLING, LAST_CHILD, PREV_SIBLING);
        ourIterators[ITERATE_CHILDREN_REV] = new IterationFunctions<N>(ITERATE_CHILDREN_REV, LAST_CHILD, PREV_SIBLING, FIRST_CHILD, NEXT_SIBLING);
        ourIterators[ITERATE_SIBLINGS] = new IterationFunctions<N>(ITERATE_SIBLINGS, NEXT_SIBLING, NEXT_SIBLING, PREV_SIBLING, PREV_SIBLING);
        ourIterators[ITERATE_SIBLINGS_REV] = new IterationFunctions<N>(ITERATE_SIBLINGS_REV, PREV_SIBLING, PREV_SIBLING, NEXT_SIBLING, NEXT_SIBLING);
    }

    final LoopConstraints[] myConstraints = new LoopConstraints[MAX_CONSTRAINTS];

    LoopConstraints<N> getOrComputeConstraint(int index) {
        LoopConstraints constraints = myConstraints[index];
        if (constraints == null) {
            //noinspection unchecked
            IterationFunctions<N> iterator = ourIterators[index];

            constraints = new FixedLoopConstraints<>(iterator.initializer, iterator.iterator, iterator.reverseInitializer, iterator.reverseIterator);
            myConstraints[index] = constraints;
        }

        //noinspection unchecked
        return constraints;
    }

    public LoopConstraints<N> getIterateSiblings() {
        return getOrComputeConstraint(ITERATE_SIBLINGS);
    }

    public LoopConstraints<N> getIterateSiblingsRev() {
        return getOrComputeConstraint(ITERATE_SIBLINGS_REV);
    }

    public LoopConstraints<N> getIterateChildren() {
        return getOrComputeConstraint(ITERATE_CHILDREN);
    }

    public LoopConstraints<N> getIterateChildrenRev() {
        return getOrComputeConstraint(ITERATE_CHILDREN_REV);
    }

    public static <T> Function<T, T> NULL() {
        return n -> null;
    }

    final private static HashMap<Class, LoopConstrainsBuilder> ourCachedBuilders = new HashMap<>();
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

    final public static LoopConstrainsBuilder<PsiElement> PSI_LOOPS = new LoopConstrainsBuilder<>(PSI_NEXT_SIBLING, PSI_PREV_SIBLING, PSI_FIRST_CHILD, PSI_LAST_CHILD, PSI_PARENT);
    public static final LoopConstrainsBuilder<ASTNode> AST_LOOPS = new LoopConstrainsBuilder<>(AST_NEXT_SIBLING, AST_PREV_SIBLING, AST_FIRST_CHILD, AST_LAST_CHILD, AST_PARENT);
    static {
        ourCachedBuilders.put(PsiElement.class, PSI_LOOPS);
        ourCachedBuilders.put(ASTNode.class, AST_LOOPS);
    }
    @Nullable
    public static <N> LoopConstrainsBuilder<N> getFor(Class<N> clazz) {
        //noinspection unchecked
        return ourCachedBuilders.get(clazz);
    }

    @NotNull
    public static <N> LoopConstrainsBuilder<N> createFor(Class<N> clazz, final Function<? super N, N> NEXT_SIBLING, final Function<? super N, N> PREV_SIBLING, final Function<? super N, N> FIRST_CHILD, final Function<? super N, N> LAST_CHILD, final Function<? super N, N> PARENT) {
        // TODO: decide if need to access builders by sub-class then on first subclass add the subclass entry for the superclass builder
        //LoopConstrainsBuilder builder = ourCachedBuilders.get(clazz);
        //if (builder == null) {
        //    // see if there are super classes defined or interfaces 
        //    clazz.getSuperclass();
        //    clazz.getInterfaces();
        //}

        //noinspection unchecked
        return ourCachedBuilders.computeIfAbsent(clazz, (c) -> new LoopConstrainsBuilder<N>(NEXT_SIBLING, PREV_SIBLING, FIRST_CHILD, LAST_CHILD, PARENT));
    }
}
