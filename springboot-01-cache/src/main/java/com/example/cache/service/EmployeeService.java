package com.example.cache.service;

import com.example.cache.bean.Employee;
import com.example.cache.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

@CacheConfig(cacheNames = "emp", cacheManager = "empCacheManager") //抽取缓存的公共配置
@Service
public class EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 将方法的结果进行缓存，以后再要相同的数据，直接从缓存中获取，不用再调用方法
     * <p>
     * CacheManager管理多个Cache组件的，对缓存真正的CRUD操作在Cache组建中，每一个缓存组件都有自己惟一一个名字；
     * 几个属性：
     * cacheName/value  指定缓存中组件的名字；将方法的返回结果放到哪个缓存中，是数组的方式，可以指定多个缓存
     * key  缓存数据使用的key；可以不用指定。默认是使用方法的参数的值 例如：1-方法的返回值
     * 编写SpEL：   #id：参数id的值   #a0   #p0  #root.args[0]
     * keyGenerator:key的生成器；可以自己指定key的生成器的组件id
     * key与keyGenerator二选一使用
     * cacheManager：指定缓存管理器，获取cacheResolver：指定获取解析器
     * condition：指定符合条件才缓存
     * unless：否定缓存，当unless指定的条件为true，方法的返回值不会缓存，可以获取到结果进行判断，例如unless="#result==null"
     * sync:是否使用异步模式
     * <p>
     * 原理
     * 1、自动配置类：CacheAutoConfiguration
     * 2、缓存的配置类
     * org.springframework.boot.autoconfigure.cache.GenericCacheConfiguration
     * org.springframework.boot.autoconfigure.cache.JCacheCacheConfiguration
     * org.springframework.boot.autoconfigure.cache.EhCacheCacheConfiguration
     * org.springframework.boot.autoconfigure.cache.HazelcastCacheConfiguration
     * org.springframework.boot.autoconfigure.cache.InfinispanCacheConfiguration
     * org.springframework.boot.autoconfigure.cache.CouchbaseCacheConfiguration
     * org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration
     * org.springframework.boot.autoconfigure.cache.CaffeineCacheConfiguration
     * org.springframework.boot.autoconfigure.cache.GuavaCacheConfiguration
     * org.springframework.boot.autoconfigure.cache.SimpleCacheConfiguration
     * org.springframework.boot.autoconfigure.cache.NoOpCacheConfiguration
     * 3、哪个配置类默认生效：SimpleCacheConfiguration
     * 4、给容器注册一个CacheManager：ConcurrentMapCacheManager
     * 5、可以获取和创建ConcurrentMapCacheManager类型的缓存组件，他的作用间数据保存在ConcurrentMap中；
     * <p>
     * 运行流程
     *
     * @param id
     * @return
     * @Cacheable 1、方法运行之前，先去查询Cache缓存组件，按照cacheNames指定的名字获取，
     * CacheManager先获取相应的缓存，第一次获取缓存如果没有Cache组件会自动创建
     * 2、去Cache中查找缓存的内容，使用一个key，默认就是方法的参数，key是按照某种生成策略生成的，
     * 默认是使用keyGenerator生成的，默认使用SimpleKeyGenerator生成key
     * SimpleKeyGenerator生成key的默认生成策略：
     * 如果没有参数：key = new SimpleKey()；
     * 如果有一个参数：key = 参数的值；
     * 如果有多个参数：key = new SimpleKey(params)；
     * 3、没有查到缓存就调用目标方法
     * 4、将目标方法返回结果、放进缓存中
     * @Cacheable标注的方法运行之前先检查缓存中有没有这个数据，默认按照参数的值作为key去查询缓存， 如果没有就运行方法并将结果放进缓存，以后再来调用就可以直接使用缓存中的数据
     * <p>
     * 核心：
     * 1、使用CacheManager【ConcurrentMapCacheManager】按照名字得到Cache【ConcurrentMapCache】组件
     * 2、key使用keyGenerato生成的，默认是SimpleKeyGenerator
     */
    @Cacheable(cacheNames = {"emp"}, key = "#id")
    public Employee getEmp(Integer id) {
        System.out.println("查询" + id + "号员工");
        return employeeMapper.getEmpById(id);
    }

    /**
     * @param employee
     * @return
     * @CachePut 既调用方法，又重新缓存数据
     * 修改了数据库的某个数据，同时更新缓存
     * 运行时机：
     * 1、先调用目标方法
     * 2、将目标方法的结果缓存起来
     */
    @CachePut(value = "emp", key = "#employee.id")
    public Employee updateEmp(Employee employee) {
        System.out.println("更新" + employee.getId() + "号员工信息");
        employeeMapper.updateEmp(employee);
        return employee;
    }

    /**
     * @param id
     * @CacheEvict 缓存清除
     */
    @CacheEvict(value = "emp", key = "#id")
    public void deleteEmp(Integer id) {
        System.out.println("删除" + id + "号员工");
        employeeMapper.deleteEmpById(id);
    }

    /**
     * @param lastName
     * @return
     * @Caching 定义复杂的缓存规则
     */
    @Caching(
            cacheable = {
                    @Cacheable(value = "emp", key = "#lastName")
            },
            put = {
                    @CachePut(value = "emp", key = "#result.id"),
                    @CachePut(value = "emp", key = "#result.email")
            }
    )
    public Employee getEmpByName(String lastName) {
        return employeeMapper.getEmpByName(lastName);
    }
}
