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

import org.jetbrains.annotations.NotNull;

public interface ValueLoopConsumerAdapter<P, T> {
    @NotNull
    <R> ValueLoopConsumer<? super P, R> getConsumer(ValueLoopConsumer<? super T, R> valueConsumer);

    @NotNull
    <R> ValueLoopConsumer<? super P, R> getConsumer(VoidLoopConsumer<? super T> voidConsumer);
}
