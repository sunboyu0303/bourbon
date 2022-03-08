package com.github.bourbon.pfinder.profiler.service.tribe;

import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.CollectionUtils;
import com.github.bourbon.pfinder.profiler.service.tribe.annotation.OrderedTribe;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/6 23:38
 */
public class OrderedServiceTribe implements ServiceTribe {
    /**
     * 是否为正序遍历
     */
    private final boolean chiefInHead;
    /**
     * 比较器
     */
    private final Comparator comparator;
    /**
     * 首节点
     */
    private volatile Object chief;
    /**
     * 订阅成员列表
     */
    private final List<Object> members = new CopyOnWriteArrayList<>();

    public OrderedServiceTribe(Comparator comparator, boolean chiefInHead) {
        this.comparator = comparator;
        this.chiefInHead = chiefInHead;
    }

    @Override
    public Object chief() {
        return this.chief;
    }

    @Override
    public Collection<Object> all() {
        return this.members;
    }

    @Override
    public boolean add(Object member) {
        // 节点不存在
        if (!CollectionUtils.contains(members, member)) {
            // 添加
            members.add(member);
            // 排序并更新首节点
            sortAndUpdateChief();
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Object member) {
        // 移除成功
        if (members.remove(member)) {
            // 排序并更新首节点
            sortAndUpdateChief();
            return true;
        }
        return false;
    }

    private void sortAndUpdateChief() {
        // 节点数量
        int size = members.size();
        // 数量大于1
        if (size > 1) {
            // 根据指定比较器排序
            members.sort(comparator);
        }
        // 更新首节点
        this.chief = BooleanUtils.defaultSupplierIfPredicate(members, CollectionUtils::isNotEmpty, m -> chiefInHead ? m.get(0) : m.get(size - 1));
    }

    public static final class Factory implements ServiceTribeFactory {
        @Override
        public ServiceTribe build(Class<?> type) {
            OrderedTribe orderPolicy = type.getAnnotation(OrderedTribe.class);
            Assert.notNull(orderPolicy, "can't found @OrderedTribe annotation");
            try {
                return new OrderedServiceTribe(orderPolicy.value().newInstance(), orderPolicy.chiefInHead());
            } catch (Exception e) {
                throw new RuntimeException("can't build OrderedServiceTribe Comparator instance.", e);
            }
        }
    }
}