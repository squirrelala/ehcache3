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

package org.ehcache.spi.copy;

import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceConfiguration;

/**
 * @author Albin Suresh
 */
public interface CopyProvider extends Service {

  /**
   * Creates a key {@link Copier} with the given parameters.
   *
   * @param clazz the class of the type to copy to/from
   * @param serializer the serializer that can be used to perform the copying. The usage is optional though.
   * @param configs specific configurations
   * @param <T> the type to copy to/from
   * @return a {@link Copier} instance
   */
  <T> Copier<T> createKeyCopier(Class<T> clazz, Serializer<T> serializer, ServiceConfiguration<?>... configs);

  /**
   * Creates a value {@link Copier} with the given parameters.
   *
   * @param clazz the class of the type to copy to/from
   * @param serializer the serializer that can be used to perform the copying.  The usage is optional though.
   * @param configs specific configurations
   * @param <T> the type to copy to/from
   * @return a {@link Copier} instance
   */
  <T> Copier<T> createValueCopier(Class<T> clazz, Serializer<T> serializer, ServiceConfiguration<?>... configs);
}
