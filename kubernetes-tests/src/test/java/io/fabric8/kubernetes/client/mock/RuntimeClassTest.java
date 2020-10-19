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
package io.fabric8.kubernetes.client.mock;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.node.v1beta1.RuntimeClass;
import io.fabric8.kubernetes.api.model.node.v1beta1.RuntimeClassBuilder;
import io.fabric8.kubernetes.api.model.node.v1beta1.RuntimeClassList;
import io.fabric8.kubernetes.api.model.node.v1beta1.RuntimeClassListBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.server.mock.KubernetesServer;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport;

import java.net.HttpURLConnection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnableRuleMigrationSupport
class RuntimeClassTest {
  @Rule
  public KubernetesServer server = new KubernetesServer();

  @Test
  void testLoad() {
    // Given
    KubernetesClient client = server.getClient();

    // When
    List<HasMetadata> itemList = client.load(getClass().getResourceAsStream("/test-runtimeclass.yml")).get();

    // Then
    assertEquals(1, itemList.size());
    assertTrue(itemList.get(0) instanceof RuntimeClass);
    RuntimeClass runtimeClass = (RuntimeClass) itemList.get(0);
    assertEquals("myclass", runtimeClass.getMetadata().getName());
    assertEquals("myconfiguration", runtimeClass.getHandler());
  }

  @Test
  void testCreate() {
    // Given
    server.expect().post().withPath("/apis/node.k8s.io/v1beta1/runtimeclasses")
      .andReturn(HttpURLConnection.HTTP_OK, getMockRuntimeClass())
      .once();
    KubernetesClient client = server.getClient();

    // When
    RuntimeClass runtimeClass = client.runtimeClasses().create(getMockRuntimeClass());

    // Then
    assertNotNull(runtimeClass);
    assertEquals("test-class", runtimeClass.getMetadata().getName());
  }

  @Test
  void testGet() {
    // Given
    server.expect().get().withPath("/apis/node.k8s.io/v1beta1/runtimeclasses/test-class")
      .andReturn(HttpURLConnection.HTTP_OK, getMockRuntimeClass())
      .once();
    KubernetesClient client = server.getClient();

    // When
    RuntimeClass runtimeClass = client.runtimeClasses().withName("test-class").get();

    // Then
    assertNotNull(runtimeClass);
    assertEquals("test-class", runtimeClass.getMetadata().getName());
  }

  @Test
  void testList() {
    // Given
    server.expect().get().withPath("/apis/node.k8s.io/v1beta1/runtimeclasses")
      .andReturn(HttpURLConnection.HTTP_OK, new RuntimeClassListBuilder()
        .addToItems(getMockRuntimeClass())
        .build())
      .once();
    KubernetesClient client = server.getClient();

    // When
    RuntimeClassList runtimeClassList = client.runtimeClasses().list();

    // Then
    assertNotNull(runtimeClassList);
    assertEquals(1, runtimeClassList.getItems().size());
    assertEquals("test-class", runtimeClassList.getItems().get(0).getMetadata().getName());
  }

  @Test
  void testDelete() {
    // Given
    server.expect().delete().withPath("/apis/node.k8s.io/v1beta1/runtimeclasses/test-class")
      .andReturn(HttpURLConnection.HTTP_OK, getMockRuntimeClass())
      .once();
    KubernetesClient client = server.getClient();

    // When
    Boolean isDeleted = client.runtimeClasses().withName("test-class").delete();

    // Then
    assertTrue(isDeleted);
  }

  private RuntimeClass getMockRuntimeClass() {
    return new RuntimeClassBuilder()
      .withNewMetadata().withName("test-class").endMetadata()
      .withNewHandler("handler2")
      .build();
  }
}
