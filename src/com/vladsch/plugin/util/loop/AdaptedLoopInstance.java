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

import java.util.function.Function;

final public class AdaptedLoopInstance<N, T, R> implements ValueLoop<T, R> {
    private final Function<N, T> myAdapter;
    private final LoopInstance<N, R> myInstance;

    public AdaptedLoopInstance(final Function<N, T> adapter, final LoopInstance<N, R> instance) {
        myAdapter = adapter;
        myInstance = instance;
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

    @Override
    public boolean haveNext() {return myInstance.haveNext();}

    @Override
    public boolean isBreak() {return getInstance().isBreak();}

    @Override
    public boolean isTerminated() {return myInstance.isTerminated();}

    @Override
    public boolean isActive() {return myInstance.isActive();}

    @Override
    public int getLoopCount() {return myInstance.getLoopCount();}

    @Override
    public int getCount() {return myInstance.getCount();}

    @Override
    public int getTotalLoopCount() {return myInstance.getTotalLoopCount();}

    @Override
    public int getTotalCount() {return myInstance.getTotalCount();}

    @Override
    public int getRecursionCount() {return myInstance.getRecursionCount();}

    public void iterate(@NotNull final ValueLoopConsumer<N, R> consumer) {myInstance.iterate(consumer);}

    public void setValue(@NotNull final Object value) {myInstance.setValue(value);}

    @Override
    @NotNull
    public R getValue() {return myInstance.getValue();}

    public void Return(@NotNull final Object value) {myInstance.Return(value);}

    @Override
    public void Recurse() {myInstance.Recurse();}

    @Override
    public void Return() {myInstance.Return();}

    @Override
    public void Continue(final int outerLevels) {myInstance.Continue(outerLevels);}

    @Override
    public void Break(final int outerLevels) {myInstance.Break(outerLevels);}

    @Override
    public void Continue() {myInstance.Continue();}

    @Override
    public void Break() {myInstance.Break();}
}
