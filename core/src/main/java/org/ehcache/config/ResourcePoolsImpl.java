/*
 * Copyright Terracotta, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ehcache.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Ludovic Orban
 */
class ResourcePoolsImpl implements ResourcePools {

  private final Map<ResourceType, ResourcePool> pools;

  ResourcePoolsImpl(Map<ResourceType, ResourcePool> pools) {
    validateResourcePools(pools.values());
    this.pools = pools;
  }

  @Override
  public ResourcePool getPoolForResource(ResourceType resourceType) {
    return pools.get(resourceType);
  }

  @Override
  public Set<ResourceType> getResourceTypeSet() {
    return pools.keySet();
  }
  
  public static void validateResourcePools(Collection<? extends ResourcePool> pools) {
    EnumMap<ResourceType.Core, ResourcePool> coreResources = new EnumMap<ResourceType.Core, ResourcePool>(ResourceType.Core.class);
    for (ResourcePool pool : pools) {
      if (pool.getType() instanceof ResourceType.Core) {
        coreResources.put((ResourceType.Core) pool.getType(), pool);
      }
    }
    
    List<ResourcePool> ordered = new ArrayList<ResourcePool>(coreResources.values());
    for (int i = 0; i < ordered.size(); i++) {
      for (int j = 0; j < i; j++) {
        ResourcePool upper = ordered.get(j);
        ResourcePool lower = ordered.get(i);

        boolean inversion;
        try {
          inversion = (upper.getUnit().compareTo(upper.getSize(), lower.getSize(), lower.getUnit()) >= 0)
                  || (lower.getUnit().compareTo(lower.getSize(), upper.getSize(), upper.getUnit()) <= 0);
        } catch (IllegalArgumentException e) {
          inversion = false;
        }
        if (inversion) {
          throw new IllegalArgumentException("Tiering Inversion: '" + upper + "' is not smaller than '" + lower + "'");
        }
      }
    }
  }
}
