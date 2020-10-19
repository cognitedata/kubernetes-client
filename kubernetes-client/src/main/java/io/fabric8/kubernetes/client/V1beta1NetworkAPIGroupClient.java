/**
 * Copyright (C) 2015 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.fabric8.kubernetes.client;

import io.fabric8.kubernetes.api.model.networking.v1beta1.DoneableIngress;
import io.fabric8.kubernetes.api.model.networking.v1beta1.DoneableIngressClass;
import io.fabric8.kubernetes.api.model.networking.v1beta1.Ingress;
import io.fabric8.kubernetes.api.model.networking.v1beta1.IngressClass;
import io.fabric8.kubernetes.api.model.networking.v1beta1.IngressClassList;
import io.fabric8.kubernetes.api.model.networking.v1beta1.IngressList;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.fabric8.kubernetes.client.dsl.internal.networking.v1beta1.IngressClassOperationsImpl;
import io.fabric8.kubernetes.client.dsl.internal.networking.v1beta1.IngressOperationsImpl;
import okhttp3.OkHttpClient;

public class V1beta1NetworkAPIGroupClient extends BaseClient implements V1beta1NetworkAPIGroupDSL {
  public V1beta1NetworkAPIGroupClient() {
    super();
  }

  public V1beta1NetworkAPIGroupClient(OkHttpClient httpClient, final Config config) {
    super(httpClient, config);
  }

  @Override
  public MixedOperation<Ingress, IngressList, DoneableIngress, Resource<Ingress, DoneableIngress>> ingresses() {
    return new IngressOperationsImpl(httpClient, getConfiguration());
  }

  @Override
  public MixedOperation<IngressClass, IngressClassList, DoneableIngressClass, Resource<IngressClass, DoneableIngressClass>> ingressClasses() {
    return new IngressClassOperationsImpl(httpClient, getConfiguration());
  }
}
