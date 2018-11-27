package cn.syrjia.util;

import com.sohu.jafka.common.InvalidPartitionException;
import com.sohu.jafka.common.NoBrokersForPartitionException;
import com.sohu.jafka.producer.ProducerConfig;
import com.sohu.jafka.producer.StringProducerData;
import com.sohu.jafka.producer.serializer.StringEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class Producer {

    private static final Logger logger = LogManager.getLogger(Producer.class);

    public static void producer(String orderNo) {
        Properties props = new Properties();
//        props.put("broker.list", "0:39.106.16.93:9092");
        props.put("broker.list", "0:172.17.59.70:9092");
        props.put("serializer.class", StringEncoder.class.getName());

        ProducerConfig config = new ProducerConfig(props);
        com.sohu.jafka.producer.Producer<String, String> producer = new com.sohu.jafka.producer.Producer<String, String>(config);

        StringProducerData data = new StringProducerData("divided");
        data.add(orderNo);

        logger.info("往jafka推送数据【orderNo={}】开始", orderNo);
        producer.send(data);
        logger.info("往jafka推送数据【orderNo={}】成功", orderNo);

        producer.close();
    }

}
