package com.myBusApp.filtering;

import java.util.function.Predicate;

import org.apache.commons.lang3.EnumUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.myBusApp.domainobject.DriverDO;
import com.myBusApp.domainvalue.DriverAttributeField;
import com.myBusApp.domainvalue.RelationalOperator;
import com.myBusApp.filtering.abstractClasses.fieldtypes.ValueType;
import com.myBusApp.filtering.fields.Driver.Deleted;
import com.myBusApp.filtering.fields.Driver.DriverId;
import com.myBusApp.filtering.fields.Driver.OnlineStatusField;
import com.myBusApp.filtering.fields.Driver.UserName;


/**
 * This class encapsulates a single criteria for a list of DriverDO objects,<br>
 * that is going to be filtered according to the values of the inner variables<br>
 * A DriverSingleCriteria can be builded over the inner Class <code>DriverSingleCriteriaBuilder</code>.<br>
 * In order to call the method {@code getFilterCriteria} first the following field have to be initialized 
 * using the setters method:<br><br>
 *  <code>BusAttributeField field;</code><br>
 *  <code>RelationalOperator relationalOperator;</code><br>
 *	<code>String value;</code><br><br>
 *
 * When the constructor is being called it instantiates the variable myFieldHandler with the right class that handles the behavior of this field.
 * The field classes are to be found in packages:<br>
 * {@link com.myBusApp.filtering.fields.Bus}<br>
 * {@link com.myBusApp.filtering.fields.Driver}<br><br>
 *  
 * Its field Class extends a specific field type Class depending on the type of the field (integer,long, boolean).<br>
 * The field Classes deliver a specific Predicate, with whom a list of drivers is going to be filtered using stream.filter().<br><br>
 * 
 * Since the class will be used over the controller layer, the class provides also validation methods to check if the parsed value
 * is appropriate according to the field type.
 * 
 * The field type Classes are to be found in the package {@link com.myBusApp.filtering.abstractClasses.fieldtypes}.<br>
 * Those are abstract implementing just a validation logic for each type.<br>
 * 
 */
public class DriverSingleCriteria {
	
	private ValueType myFieldHandler;
	
	//Field specifies which field an BusDTO instance are the class going to compare with
	private DriverAttributeField field;
	//Variable specifies in which way will be the field compared with the variable
	private RelationalOperator relationalOperator;
	//Value to compare with the field of an BusDTO instance 
	private String value;
	
	private DriverSingleCriteria(DriverAttributeField field, RelationalOperator relationalOperator, String value) {
		super();
		this.field = field;
		this.relationalOperator = relationalOperator;
		this.value = value;
		initializeMyField();		
	}
	
	public static DriverSingleCriteriaBuilder newBuilder()
    {
        return new DriverSingleCriteriaBuilder();
    }

	public DriverAttributeField getField() {
		return field;
	}

	public RelationalOperator getRelationalOperator() {
		return relationalOperator;
	}

	public String getValue() {
		return value;
	}	
	
	/**
	 * The method assign a field class to variable myFieldHandler
	 * according to the field given in the variable BusAttributeField field;
	 *
	 */
	private void initializeMyField() {
		switch(this.field){
			case DELETED:
				myFieldHandler=new Deleted(value,relationalOperator);
				break;
			case ID:
				myFieldHandler=new DriverId(value,relationalOperator);
				break;
			case ONLINE_STATUS:
				myFieldHandler=new OnlineStatusField(value,relationalOperator);
				break;
			case USER_NAME:
				myFieldHandler=new UserName(value,relationalOperator);
				break;
			default:
				break;			
		}
	}
	
	/**
	  * The method delivers Predicates for a list of drivers checking if the the field specified in the variable field meets some criteria.
	 * @return Predicate<BusDTO>
	 */
	@JsonIgnore
	public Predicate<DriverDO> getDriverFilterCriteria() {
		
		return myFieldHandler.getFilterCriteria();
	}
	
	/**
	 * Checks if Value provided is valid.
	 * for example if an integer field has no alphanumeric value. 
	 * @return
	 */
	@JsonIgnore
	public boolean isValueValid(){
		return this.value!=null && this.myFieldHandler.isDataValid();
	}
	
	@JsonIgnore
	public boolean isBusAttributeValid() {
		return this.field!=null
				&& EnumUtils.getEnumList(DriverAttributeField.class).contains(this.field);
	}
	
	@JsonIgnore
	public boolean isRelationalOperatorValid() {
		return this.relationalOperator!=null
				&& EnumUtils.getEnumList(RelationalOperator.class).contains(this.relationalOperator);
	}
	
	@JsonIgnore
	public boolean isRelationalOperatorSupported() {
		return myFieldHandler.isRelationalOperatorSupported();
	}
	
	public static class DriverSingleCriteriaBuilder
    {
		private DriverAttributeField field;
		private RelationalOperator relationalOperator;
		private String value;

		public DriverSingleCriteriaBuilder setField(DriverAttributeField field) {
			this.field = field;
			return this;
		}
		
		public DriverSingleCriteriaBuilder setRelationalOperato(RelationalOperator relationalOperator) {
			this.relationalOperator = relationalOperator;
			return this;
		}
		
		public DriverSingleCriteriaBuilder setValue(String value) {
			this.value = value;
			return this;
		}

        public DriverSingleCriteria createDriverSingleCriteria()
        {
            return new DriverSingleCriteria(this.field,this.relationalOperator,this.value);
        }

    }
	
}
