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

package com.vladsch.plugin.util.tree;

import org.jetbrains.annotations.NotNull;

public class VoidToValueIConsumerAdapter<N, R> implements ValueIterationConsumer<N, R> {
    final private @NotNull VoidIterationConsumer<N> myConsumer;

    public VoidToValueIConsumerAdapter(@NotNull final VoidIterationConsumer<N> consumer) {
        myConsumer = consumer;
    }

    @Override
    public void accept(@NotNull final N it, @NotNull final ValueIteration<R> iteration) {
        myConsumer.accept(it, iteration);
    }

    @Override
    public void beforeStart(@NotNull final ValueIteration<R> iteration) {
        myConsumer.beforeStart(iteration);
        
    }

    @Override
    public void startRecursion(@NotNull final VoidIteration iteration) {
        myConsumer.startRecursion(iteration);
    }

    @Override
    public void endRecursion(@NotNull final VoidIteration iteration) {
        myConsumer.endRecursion(iteration);

    }

    @Override
    public void afterEnd(@NotNull final ValueIteration<R> iteration) {
        myConsumer.afterEnd(iteration);
    }
}
