/*
 * Copyright 2020 Confluent Inc.
 *
 * Licensed under the Confluent Community License (the "License"); you may not use
 * this file except in compliance with the License.  You may obtain a copy of the
 * License at
 *
 * http://www.confluent.io/confluent-community-license
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 *
 */

package io.confluent.kafka.serializers.protobuf;

import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.schemaregistry.protobuf.ProtobufSchema;
import io.confluent.kafka.schemaregistry.protobuf.ProtobufSchemaUtils;

public class KafkaProtobufSerializer extends AbstractKafkaProtobufSerializer
    implements Serializer<Object> {

  private boolean isKey;

  /**
   * Constructor used by Kafka producer.
   */
  public KafkaProtobufSerializer() {

  }

  public KafkaProtobufSerializer(SchemaRegistryClient client) {
    schemaRegistry = client;
  }

  public KafkaProtobufSerializer(SchemaRegistryClient client, Map<String, ?> props) {
    schemaRegistry = client;
    configure(serializerConfig(props));
  }

  @Override
  public void configure(Map<String, ?> configs, boolean isKey) {
    this.isKey = isKey;
    configure(new KafkaProtobufSerializerConfig(configs));
  }

  @Override
  public byte[] serialize(String topic, Object record) {
    if (record == null) {
      return null;
    }
    // TODO also support reflection?
    ProtobufSchema schema = ProtobufSchemaUtils.getSchema(record);
    return serializeImpl(getSubjectName(topic, isKey, record, schema), record, schema);
  }

  @Override
  public void close() {
  }
}
