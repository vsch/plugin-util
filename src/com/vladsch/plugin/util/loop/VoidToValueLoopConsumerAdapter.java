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

import org.jetbrains.annotations.NotNull;

public class VoidToValueLoopConsumerAdapter<N,R> implements ValueLoopConsumer<N,R> {
    final private  @NotNull VoidLoopConsumer<N> myConsumer;

    public VoidToValueLoopConsumerAdapter(@NotNull final VoidLoopConsumer<N> consumer) {
        myConsumer = consumer;
    }

    @Override
    public void accept(@NotNull final N it, @NotNull final ValueLoop<R> loop) {
        myConsumer.accept(it, loop);
    }

    @Override
    public void afterEnd(@NotNull final ValueLoop<R> loop) {
        myConsumer.afterEnd(loop);
    }

    @Override
    public void beforeStart(@NotNull final ValueLoop<R> loop) {
        myConsumer.beforeStart(loop);
    }
}
