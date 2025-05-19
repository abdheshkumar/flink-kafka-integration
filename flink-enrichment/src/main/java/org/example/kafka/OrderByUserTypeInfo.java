package org.example.kafka;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.formats.avro.typeutils.AvroTypeInfo;
import org.apache.flink.shaded.guava32.com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public class OrderByUserTypeInfo {
    /*
    public static final TypeInformation<TraceId> TRACE_ID_INFO = Types.POJO(TraceId.class,
            ImmutableMap.of(
                    "traceId", Types.STRING
            ));
    public static final TypeInformation<UserAvroRecord> USER_AVRO_RECORD_TYPE_INFO = new AvroTypeInfo<>(UserAvroRecord.class);
    public static final TypeInformation<OrderAvro> ORDER_AVRO_TYPE_INFO = new AvroTypeInfo<>(OrderAvro.class);
    */

    static final Map<String, TypeInformation<?>> FIELD_MAP = new HashMap<>();
    static {
        //FIELD_MAP.put("traceId", TRACE_ID_INFO);
        //FIELD_MAP.put("user", Types.LIST(USER_AVRO_RECORD_TYPE_INFO));
        //FIELD_MAP.put("user", USER_AVRO_RECORD_TYPE_INFO);
        //FIELD_MAP.put("order", Types.LIST(ORDER_AVRO_TYPE_INFO));
        //FIELD_MAP.put("order", ORDER_AVRO_TYPE_INFO)
        FIELD_MAP.put("orderId", Types.STRING);
        FIELD_MAP.put("userName", Types.STRING);
        FIELD_MAP.put("amount", Types.DOUBLE);
    }
    public static final TypeInformation<OrderByUser> ORDER_BY_USER_RECORD_INFO = Types.POJO(OrderByUser.class, FIELD_MAP);

}

