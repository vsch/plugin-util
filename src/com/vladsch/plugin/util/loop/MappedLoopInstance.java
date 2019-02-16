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

import java.util.function.Function;

final public class MappedLoopInstance<N, T, R> extends LoopInstanceDelegate<N, T, R> implements ValueLoop<T, R> {
    private final Function<N, T> myAdapter;

    public MappedLoopInstance(final Function<N, T> adapter, final LoopInstance<N, R> instance) {
        super(instance);
        myAdapter = adapter;
    }

    public LoopInstance<N, R> getInstance() {
        return myInstance;
    }

    @Override
    public void handle(final ValueLoopConsumer<T, R> consumer) {
        if (myInstance.getMatch() != null) {
            T applied = myAdapter.apply(myInstance.getMatch());
            if (applied != null) {
                consumer.accept(applied, this);
            }
        }
    }

    @Override
    public void handle(final VoidLoopConsumer<T> consumer) {
        if (myInstance.getMatch() != null) {
            T applied = myAdapter.apply(myInstance.getMatch());
            if (applied != null) {
                consumer.accept(applied, this);
            }
        }
    }
}
