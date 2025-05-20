package org.example.kafka;

import org.apache.flink.api.common.typeinfo.TypeInfoFactory;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class OrderByUserFactory extends TypeInfoFactory<OrderByUser> {
    @Override
    public TypeInformation<OrderByUser> createTypeInfo(Type t, Map<String, TypeInformation<?>> genericParameters) {
        return OrderByUserTypeInfo.ORDER_BY_USER_RECORD_INFO;
    }

    //TypeInformation<List<OrderByUser>> ORDER_BY_USER_TYPE_INFO = Types.LIST(Types.POJO(OrderByUser.class));
}
