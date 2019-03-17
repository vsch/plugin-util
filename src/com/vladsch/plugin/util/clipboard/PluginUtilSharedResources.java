/*
 * Copyright (c) 2016-2019 Vladimir Schneider <vladimir.schneider@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.vladsch.plugin.util.clipboard;

import com.vladsch.plugin.util.DelayedRunner;
import com.vladsch.plugins.sharedResourceRegistrar.SharedResource;
import com.vladsch.plugins.sharedResourceRegistrar.SharedResourceListener;
import com.vladsch.plugins.sharedResourceRegistrar.SharedResourceProvider;
import com.vladsch.plugins.sharedResourceRegistrar.SharedResourceRegistrar;
import org.jetbrains.annotations.NotNull;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.HashMap;
import java.util.function.BiFunction;

public class PluginUtilSharedResources implements SharedResourceProvider {
    private static SharedResourceRegistrar ourRegistrar;
    private static DelayedRunner ourDelayedRunner = null;
    
    private static SharedResource<HashMap<String, DataFlavor>> CreatedFlavours = new SharedResource<HashMap<String, DataFlavor>>() {
        private HashMap<String, DataFlavor> myData;

        @Override
        public HashMap<String, DataFlavor> get() {
            if (myData == null) myData = new HashMap<>();
            return myData;
        }

        @Override
        public String getId() {
            return "com.vladsch.plugin.util.clipboard.CreatedFlavours";
        }

        @Override
        public void informResourceListener(final SharedResourceListener<HashMap<String, DataFlavor>> listener) {

        }
    };

    private static SharedResource<HashMap<DataFlavor, BiFunction<Transferable, DataFlavor, Object>>> TransferDataLoaders = new SharedResource<HashMap<DataFlavor, BiFunction<Transferable, DataFlavor, Object>>>() {
        private HashMap<DataFlavor, BiFunction<Transferable, DataFlavor, Object>> myData;

        @Override
        public HashMap<DataFlavor, BiFunction<Transferable, DataFlavor, Object>> get() {
            if (myData == null) myData = new HashMap<>();
            return myData;
        }

        @Override
        public String getId() {
            return "com.vladsch.plugin.util.clipboard.TransferDataLoaders";
        }

        @Override
        public void informResourceListener(final SharedResourceListener<HashMap<DataFlavor, BiFunction<Transferable, DataFlavor, Object>>> listener) {

        }
    };

    private static SharedResource<DataFlavor> AugmentedDataFlavor = new SharedResource<DataFlavor>() {
        private DataFlavor myData;

        @Override
        public DataFlavor get() {
            if (myData == null) {
                myData = TextBlockDataFlavorRegistrar.getInstance().getOrCreateDataFlavor("application/augmented-transferable", "Data", null, null, false, null);
            }
            return myData;
        }

        @Override
        public String getId() {
            return "com.vladsch.plugin.util.clipboard.AugmentedDataFlavor";
        }

        @Override
        public void informResourceListener(final SharedResourceListener<DataFlavor> listener) {

        }
    };

    static HashMap<String, DataFlavor> getCreatedFlavours() {
        assert ourRegistrar != null;
        return ourRegistrar.getResource(CreatedFlavours).get();
    }

    static HashMap<DataFlavor, BiFunction<Transferable, DataFlavor, Object>> getTransferDataLoaders() {
        assert ourRegistrar != null;
        return ourRegistrar.getResource(TransferDataLoaders).get();
    }

    static DataFlavor getAugmentedDataFlavor() {
        assert ourRegistrar != null;
        return ourRegistrar.getResource(AugmentedDataFlavor).get();
    }

    @Override
    public void registerSharedResources(final SharedResourceRegistrar registrar) {
        if (ourRegistrar == null) {
            ourRegistrar = registrar;
            ourRegistrar.registerResource(CreatedFlavours);
            ourRegistrar.registerResource(TransferDataLoaders);
            ourRegistrar.registerResource(AugmentedDataFlavor);
            
            // run all who depend on shared resources being valid
            if (ourDelayedRunner != null) {
                ourDelayedRunner.runAll();
                ourDelayedRunner = null;
            }
        }
    }
    
    public static void onLoad(@NotNull Runnable runnable) {
        if (ourRegistrar != null) {
            runnable.run();
            return;
        }

        if (ourDelayedRunner == null) ourDelayedRunner = new DelayedRunner();
        ourDelayedRunner.addRunnable(runnable);
    }
}
