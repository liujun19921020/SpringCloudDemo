package com.lj.commonsutils.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelCell {
    /**
     * 顺序 default 100
     * @return index
     */
    int index();

    /**
     * 
     * @return defaultValue
     */
    String defaultValue() default "";

    /**
     * 用于验证
     * @return valid
     */
    Valid valid() default @Valid();

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Valid {
        /**
         * @return e.g. {"key","value"}
         */
        String[] in() default {};

        /**
         * 是否允许为空
         * @return allowNull
         */
        boolean allowNull() default true;

        /**
         * Apply a "greater than" constraint to the named property
         * 
         * @return gt
         */
        double gt() default Double.NaN;

        /**
         * Apply a "less than" constraint to the named property
         * @return lt
         */
        double lt() default Double.NaN;

        /**
         * Apply a "greater than or equal" constraint to the named property
         * 
         * @return ge
         */
        double ge() default Double.NaN;

        /**
         * Apply a "less than or equal" constraint to the named property
         * 
         * @return le
         */
        double le() default Double.NaN;
    }
}
