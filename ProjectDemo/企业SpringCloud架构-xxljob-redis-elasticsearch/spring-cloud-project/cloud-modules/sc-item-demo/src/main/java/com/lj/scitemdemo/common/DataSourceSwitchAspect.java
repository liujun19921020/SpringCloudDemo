package com.lj.scitemdemo.common;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 *  数据源AOP注解实现
 */
@Component
@Aspect
@Order(-100)
public class DataSourceSwitchAspect {

    private Logger log= LoggerFactory.getLogger(DataSourceSwitchAspect.class);

    @Pointcut("execution(* com.lj.scitemdemo.service.cs1db..*.*(..))")
    private void cs1dbAspect() {
    }

    @Pointcut("execution(* com.lj.scitemdemo.service.cs2db..*.*(..))")
    private void cs2dbAspect() {
    }


    @Before( "cs1dbAspect()" )
    public void cs1db(JoinPoint joinPoint) {
        log.info("切换到cs1db 数据源...");
        setDataSource(joinPoint,DBTypeEnum.cs1db);
    }

    @Before("cs2dbAspect()" )
    public void cs2db(JoinPoint joinPoint) {
        log.info("切换到cs2db 数据源...");
        setDataSource(joinPoint,DBTypeEnum.cs2db);
    }

    /**
     * 添加注解方式,如果有注解优先注解,没有则按传过来的数据源配置
     * @param joinPoint
     * @param dbTypeEnum
     */
    private void setDataSource(JoinPoint joinPoint, DBTypeEnum dbTypeEnum) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        DataSourceSwitch dataSourceSwitch = methodSignature.getMethod().getAnnotation(DataSourceSwitch.class);
        if (Objects.isNull(dataSourceSwitch) || Objects.isNull(dataSourceSwitch.value())) {
            DbContextHolder.setDbType(dbTypeEnum);
        }else{
            log.info("根据注解来切换数据源,注解值为:"+dataSourceSwitch.value());
            switch (dataSourceSwitch.value().getValue()) {
                case "cs1-db":
                    DbContextHolder.setDbType(DBTypeEnum.cs1db);
                    break;
                case "cs2-db":
                    DbContextHolder.setDbType(DBTypeEnum.cs2db);
                    break;
                default:
                    DbContextHolder.setDbType(dbTypeEnum);
            }
        }
    }
}
