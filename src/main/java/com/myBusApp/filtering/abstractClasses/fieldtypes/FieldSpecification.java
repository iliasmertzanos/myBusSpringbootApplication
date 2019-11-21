package com.myBusApp.filtering.abstractClasses.fieldtypes;

import java.util.Arrays;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.data.jpa.domain.Specification;

import com.myBusApp.domainobject.BusDO;
import com.myBusApp.domainobject.DriverDO;
import com.myBusApp.domainvalue.AttributeField;
import com.myBusApp.domainvalue.BusAttributeField;
import com.myBusApp.domainvalue.EngineType;
import com.myBusApp.domainvalue.OnlineStatus;
import com.myBusApp.domainvalue.RelationalOperator;


/**
 * An abstract class for subclasses that handle the behavior of 
 * Long, Integer etc. fields.
 *
 */
public class FieldSpecification  implements Specification<DriverDO> {
	
//	private static Predicate predicate;
	private String value;
	private AttributeField field;
	private RelationalOperator operator;
	
	public FieldSpecification() {}
	
	public FieldSpecification(String value, RelationalOperator compateType,AttributeField field) {
		this.value=value;
		this.operator=compateType;
		this.field=field;
		// TODO Auto-generated constructor stub
	}
	
	public String getValue() {
		return value;
	}

	public  void setValue(String value) {
		this.value = value;
	}

	public  AttributeField getField() {
		return field;
	}

	public void setField(AttributeField field) {
		this.field = field;
	}

	public RelationalOperator getOperator() {
		return operator;
	}

	public void setOperator(RelationalOperator operator) {
		this.operator = operator;
	}

	/**
	 * Checks if the current field can be compared using the given comparator
	 *
	 */
	
	public boolean isRelationalOperatorSupported() {
		return !Arrays.asList(RelationalOperator.NOTNULL,RelationalOperator.LIKE)
				.stream()
				.anyMatch(type->type.equals(this.operator));
	}
	
	

	@Override
	public Predicate toPredicate(Root<DriverDO> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		Join<DriverDO, BusDO> busDetailsJoin = root.join("busDetails", JoinType.INNER);
		Predicate predicate=null;
   	 	Path myPathObject =
   			 field.isInGroup(AttributeField.Group.CAR)
   			 ?busDetailsJoin.get(field.getFieldDescription())
   			 :root.get(field.getFieldDescription());
		Comparable comparableValue=convertValueToComparable();
		 switch(operator) {
		 	case EQUALS:
				predicate=criteriaBuilder.equal(myPathObject,comparableValue );
				break;
			case GREATERTHAN:
				predicate=criteriaBuilder.greaterThan(myPathObject,comparableValue);
				break;
			case LESSTHAN:
				predicate=criteriaBuilder.lessThan(myPathObject,comparableValue);
				break;
			case NOTEMPTY:
				break;
			default:
				break;
		 }
		 return predicate;
	}
	
	private Comparable convertValueToComparable() {
		if(field==AttributeField.CAR_ENGINE) {
			return (Comparable) EnumUtils.getEnum(EngineType.class, value);
		}else if(field==AttributeField.CONVERTIBLE) {
			return Boolean.parseBoolean(value);
		}else if(field==AttributeField.DELETED) {
			return Boolean.parseBoolean(value);
		}else if(field==AttributeField.ONLINE_STATUS) {
			return (Comparable) EnumUtils.getEnum(OnlineStatus.class, value);
		}else {
			return (Comparable)value;
		}
	}
	
}
