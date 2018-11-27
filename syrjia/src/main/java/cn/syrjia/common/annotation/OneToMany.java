package cn.syrjia.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author liwenlong 一对多实体 2016-6-23
 */
/**
 * 
 *CONSTRUCTOR:用于描述构造器
 *FIELD:用于描述域
 *LOCAL_VARIABLE:用于描述局部变量
 *METHOD:用于描述方法
 *PACKAGE:用于描述包
 *PARAMETER:用于描述参数
 *TYPE:用于描述类、接口(包括注解类型) 或enum声明
 */
/**
 * 
 *SOURCE:在源文件中有效（即源文件保留）
 *CLASS:在class文件中有效（即class保留）
 *RUNTIME:在运行时有效（即运行时保留）
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OneToMany {


	/**
	 * 中间表名
	 * 
	 * @return
	 */
	String contentTable() default "";
	
	/**
	 * 中间表关联字段
	 * @return
	 */
	String correlationTableField() default "";
	
	/**
	 * 中间表被关联字段
	 * @return
	 */
	String correlationTableOtherField() default "";
	
	/**
	 * 关联字段
	 * 
	 * @return
	 */
	String correlationField();
	
	/**
	 * 被关联字段
	 * 
	 * @return
	 */
	String correlationOtherField();
}
