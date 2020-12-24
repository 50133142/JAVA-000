/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hmily.demo.springcloud.order.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.dromara.hmily.common.utils.IdWorkerUtils;
import org.dromara.hmily.demo.common.order.entity.Order;
import org.dromara.hmily.demo.common.order.enums.OrderStatusEnum;
import org.dromara.hmily.demo.common.order.mapper.OrderMapper;
import org.dromara.hmily.demo.springcloud.order.service.OrderService;
import org.dromara.hmily.demo.springcloud.order.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author xiaoyu
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderMapper orderMapper;

    private final PaymentService paymentService;

    @Autowired(required = false)
    public OrderServiceImpl(OrderMapper orderMapper,PaymentService paymentService){
        this.orderMapper = orderMapper;
        this.paymentService = paymentService;
    }

    @Override
    public String orderPay(Integer count,BigDecimal amount){
        Order order = saveOrder(count,amount);
        long start = System.currentTimeMillis();
        paymentService.makePayment(order);
        System.out.println("hmily-cloud分布式事务耗时：" + (System.currentTimeMillis() - start));
        return "success";
    }

    @Override
    public String testOrderPay(Integer count,BigDecimal amount){
        Order order = saveOrder(count,amount);
        paymentService.testMakePayment(order);
        return "success";
    }

    @Override
    public String mockInventoryWithTryException(Integer count,BigDecimal amount){
        Order order = saveOrder(count,amount);
        return paymentService.mockPaymentInventoryWithTryException(order);
    }

    @Override
    public String mockAccountWithTryException(Integer count,BigDecimal amount){
        Order order = saveOrder(count,amount);
        return paymentService.mockPaymentAccountWithTryException(order);
    }

    /**
     * 模拟在订单支付操作中，库存在try阶段中的timeout
     *
     * @param count  购买数量
     * @param amount 支付金额
     * @return string
     */
    @Override
    public String mockInventoryWithTryTimeout(Integer count,BigDecimal amount){
        Order order = saveOrder(count,amount);
        return paymentService.mockPaymentInventoryWithTryTimeout(order);
    }

    @Override
    public String mockAccountWithTryTimeout(Integer count,BigDecimal amount){
        Order order = saveOrder(count,amount);
        return paymentService.mockPaymentAccountWithTryTimeout(order);
    }

    @Override
    public String orderPayWithNested(Integer count,BigDecimal amount){
        Order order = saveOrder(count,amount);
        return paymentService.makePaymentWithNested(order);
    }

    @Override
    public String orderPayWithNestedException(Integer count,BigDecimal amount){
        Order order = saveOrder(count,amount);
        return paymentService.makePaymentWithNestedException(order);
    }

    @Override
    public void updateOrderStatus(Order order){
        orderMapper.update(order);
    }


    private Order saveOrder(Integer count,BigDecimal amount){
        final Order order = buildOrder(count,amount);
        orderMapper.save(order);
        return order;
    }


    private Order buildOrder(Integer count,BigDecimal amount){
        LOGGER.debug("构建订单对象");
        Order order = new Order();
        order.setCreateTime(new Date());
        order.setNumber(String.valueOf(IdWorkerUtils.getInstance().createUUID()));
        //demo中的表里只有商品id为 1的数据
        order.setProductId("1");
        order.setStatus(OrderStatusEnum.NOT_PAY.getCode());
        order.setTotalAmount(amount);
        order.setCount(count);
        //demo中 表里面存的用户id为10000
        order.setUserId(IdWorkerUtils.getInstance().createUUID());
        return order;
    }




    public static void main(String[] args){

        long uuid = IdWorkerUtils.getInstance().createUUID();

    }

    /**
     * 生成8位随机数
     * @return
     */
    public  static String getNonceStr(int  num) {
        String SYMBOLS = "123456789";
        Random RANDOM = new SecureRandom();
        char[] nonceChars = new char[num];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }

    @Override
    public String writeOrderPay(Integer count,BigDecimal amount){
        Order order = saveOrder(count,amount);
        return "SUCCESS";
    }

    @Override
    public Order queryOrderPay(Long id){
        Order order = new Order();
        order.setId(id);

//        List<Order> orders1 = orderMapper.listAll();
        List<Order> orders = orderMapper.listById(order);
        if(CollectionUtils.isNotEmpty(orders)){
            return orders.get(0);
        }
        return order;
    }

    @Override
    public  void batchInsertOrser() {
        insertOrser();

        ExecutorService executorService = Executors.newFixedThreadPool(8);
        for (int i = 0; i < 200000; i++) {
            final int no = i;
            executorService.execute(() -> {
                insertOrser();
            });
        }
        executorService.shutdown();
        System.out.println("Main Thread End!================================================================");
    }



    private Order insertOrser(){
        LOGGER.debug("构建订单对象");
        Order order = new Order();
//        order.setId(Integer.valueOf(getNonce_str()));
        order.setCreateTime(new Date());
        order.setNumber(String.valueOf(IdWorkerUtils.getInstance().createUUID()));
        //demo中的表里只有商品id为 1的数据
        order.setProductId(String.valueOf(getNonceStr(6)));
        order.setStatus(OrderStatusEnum.NOT_PAY.getCode());
        order.setTotalAmount(new BigDecimal(getNonceStr(3)));
        order.setCount(Integer.valueOf(getNonceStr(1)));
        //demo中 表里面存的用户id为10000
        order.setUserId(IdWorkerUtils.getInstance().createUUID());

        orderMapper.save(order);
        return order;
    }
}
