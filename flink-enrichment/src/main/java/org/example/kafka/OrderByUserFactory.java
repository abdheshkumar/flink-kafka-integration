package org.example.kafka;

import org.apache.flink.api.common.typeinfo.TypeInfoFactory;
import org.apache.flink.api.common.typeinfo.TypeInformation;

import java.lang.reflect.Type;
import java.util.Map;

public class OrderByUserFactory extends TypeInfoFactory<OrderByUser> {
    @Override
    public TypeInformation<OrderByUser> createTypeInfo(Type t, Map<String, TypeInformation<?>> genericParameters) {
        return OrderByUserTypeInfo.ORDER_BY_USER_RECORD_INFO;
    }
}
