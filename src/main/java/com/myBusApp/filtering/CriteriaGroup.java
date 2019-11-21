package com.myBusApp.filtering;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.data.jpa.domain.Specification;

import com.myBusApp.domainobject.DriverDO;
import com.myBusApp.domainvalue.AttributeField;
import com.myBusApp.domainvalue.BusAttributeField;
import com.myBusApp.domainvalue.LogicalOperator;
import com.myBusApp.domainvalue.RelationalOperator;
import com.myBusApp.filtering.abstractClasses.fieldtypes.FieldSpecification;
import com.myBusApp.filtering.abstractClasses.fieldtypes.NumericalType;

/**
 * This class encapsulates a list of multiple criteria to filter a list of drivers<br>
 * according their attributes.<br>
 * The parameter <code>LogicalOperator logicalOperator </code> defines if the filtering should be done via conjunction (and) or disjunction (or).<br>
 */
public class CriteriaGroup {
	
	private List<FieldSpecification> criteriaList=new ArrayList<FieldSpecification>();
	
	private LogicalOperator logicalOperator;

	public CriteriaGroup(List<FieldSpecification> criteriaList, 
			LogicalOperator logicalOperator) {
		this.criteriaList = criteriaList;
		this.logicalOperator = logicalOperator;
	}
	
	public CriteriaGroup with(String key, String operation, String value) {
		criteriaList.add(new FieldSpecification(value, EnumUtils.getEnum(RelationalOperator.class,operation), EnumUtils.getEnum(AttributeField.class,key)));
        return this;
    }

	public List<FieldSpecification> getcriteriaList() {
		return criteriaList;
	}

	public void setcriteriaList(List<FieldSpecification> criteriaList) {
		this.criteriaList = criteriaList;
	}
	
	public LogicalOperator getBusLogicalOperator() {
		return logicalOperator;
	}

	public void setBusLogicalOperator(LogicalOperator logicalOperator) {
		this.logicalOperator = logicalOperator;
	}
	
	public Specification<DriverDO> build() {
		
        if (criteriaList.size() == 0) {
            return null;
        }
         
        Specification result = criteriaList.get(0);
 
        for (int i = 1; i < criteriaList.size(); i++) {
        	result = (logicalOperator==LogicalOperator.OR
                    ? Specification.where(result).or(criteriaList.get(i)) 
                    : Specification.where(result).and(criteriaList.get(i))
                    );
        
        }       
        return result;
    }

	/**
	 * Method for web service layer to check validity of parsed criteria type
	 *
	 */
	public boolean isCriteriaTypeValid(String myType) {
		return myType!=null && EnumUtils.isValidEnum(LogicalOperator.class, myType);
	}
	
	
}
