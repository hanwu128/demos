package com.lenovo.iot.devicemanager.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class KafkaProducerService {
	private static SimpleDateFormat FF_SECOND = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private KafkaProducer<String, String> kafkaProducer;

	private static class SingletonHolder {
		/** 单例变量 */
		private static KafkaProducerService instance = new KafkaProducerService();
	}

	/**
	 * 私有化的构造方法，保证外部的类不能通过构造器来实例化。
	 */
	private KafkaProducerService() {

	}

	/**
	 * 获取单例对象实例
	 * 
	 * @return 单例对象
	 */
	public static KafkaProducerService getInstance() {
		return SingletonHolder.instance;
	}

	public void init(String broker) {
		Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, broker);
		properties.put(ProducerConfig.LINGER_MS_CONFIG, 1);
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class.getName());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class.getName());

		kafkaProducer = new KafkaProducer<String, String>(properties);
	}

	public void send(String topic, final String key, final String value) {
		ProducerRecord<String, String> kafkaRecord = new ProducerRecord<String, String>(topic, key, value);
		kafkaProducer.send(kafkaRecord, new Callback() {
			public void onCompletion(RecordMetadata metadata, Exception e) {
				if (null != e) {
					System.out.println(FF_SECOND.format(new Date()) + " Send to Kafka failed!");
					e.printStackTrace();
					// } else {
					// System.out.println(FF_SECOND.format(new Date()) +
					// " Send to Kafka successfully!");
				}
			}
		});
	}
}
