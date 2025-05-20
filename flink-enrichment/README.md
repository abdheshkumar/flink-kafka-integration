## User JSON
```json
{
  "user_id": "12345",
  "user_name": "John Doe"
}
```
## Order JSON
```json
{
  "order_id": "12",
  "user_id": "12345",
  "amount": 20.5
}
```

## OrderByUser:
This is a POJO (Plain Old Java Object) that represents the enriched data structure combining order and user information. Flink requires such well-defined data types for efficient serialization and deserialization. By adhering to Flink's POJO rules (public fields, no-arg constructor), it ensures optimized performance using Flink's built-in POJO serializer.


## OrderByUserFactory:
This factory class is used to provide custom TypeInformation for the OrderByUser class. Flink uses TypeInformation to understand the structure of data types for serialization, deserialization, and type checking. The factory allows you to define how Flink should interpret the OrderByUser class, especially when the default POJO detection is insufficient.


## OrderByUserTypeInfo:
This class defines the TypeInformation for the OrderByUser class explicitly. It maps the fields of OrderByUser to their respective types (e.g., orderId as String, amount as Double). This is essential for Flink to handle the data correctly during transformations, state management, and serialization.

## Check which serializer is used by process
```code
TypeInformation<OrderByUser> typeInfo = TypeInformation.of(OrderByUser.class);
TypeSerializer<OrderByUser> serializer = typeInfo.createSerializer(env.getConfig().getSerializerConfig());
System.out.println(serializer);
```
## Flink Serialization

#### Flink's Built-in / POJO Serializer
If your class is a Java-style POJO:
Public no-arg constructor
Non-final, public fields or getters/setters
Flink uses its optimized POJO serializer (much faster than Kryo).

#### Fallback: Kryo
If Flink can't identify your object as a POJO (e.g., case class, anonymous class, complex generics):
It falls back to Kryo serialization.

#### Kryo Requires Registration in Strict Mode
By default, Flink uses Kryo in non-strict mode, but in production, you should register custom types:
env.getConfig().registerKryoType(OrderByUser.class);

**POJO serialization is 2–5X faster than Kryo.
Kryo creates more GC pressure.**

## Use avro serialization
```code
ConfluentRegistryAvroDeserializationSchema.forSpecific
```
