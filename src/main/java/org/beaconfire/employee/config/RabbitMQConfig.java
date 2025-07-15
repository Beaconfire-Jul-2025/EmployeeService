package org.beaconfire.employee.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EMPLOYEE_QUEUE = "employee.create.queue";
    public static final String EMPLOYEE_EXCHANGE = "employee.exchange";
    public static final String EMPLOYEE_ROUTING_KEY = "employee.create";

    @Bean
    public Queue employeeQueue() {
        return new Queue(EMPLOYEE_QUEUE);
    }

    @Bean
    public DirectExchange employeeExchange() {
        return new DirectExchange(EMPLOYEE_EXCHANGE);
    }

    @Bean
    public Binding binding(Queue employeeQueue, DirectExchange employeeExchange) {
        return BindingBuilder.bind(employeeQueue).to(employeeExchange).with(EMPLOYEE_ROUTING_KEY);
    }
    @Bean
    public Queue updateEmployeeQueue() {
        return new Queue("employee.update.queue", true);
    }
}

