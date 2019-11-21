package com.myBusApp.filtering;

import java.util.function.Predicate;

import org.apache.commons.lang3.EnumUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.myBusApp.domainobject.DriverDO;
import com.myBusApp.domainvalue.BusAttributeField;
import com.myBusApp.domainvalue.RelationalOperator;
import com.myBusApp.filtering.abstractClasses.fieldtypes.ValueType;
import com.myBusApp.filtering.fields.Bus.BusManufacturer;
import com.myBusApp.filtering.fields.Bus.Convertible;
import com.myBusApp.filtering.fields.Bus.EngineTypeField;
import com.myBusApp.filtering.fields.Bus.Rating;
import com.myBusApp.filtering.fields.Bus.SeatCount;


/**
 * This class encapsulates a single criteria for a list of DriverDO objects.<br>
 * <b>The main difference with the class {@link DriverSingleCriteria} is that the current class delivers Predicates,<br> 
 * according to the values of the inner variable BusDO of a DriverDO object.</b><br>
 * A DriverSingleCriteria can be builded over the inner Class <code>DriverSingleCriteriaBuilder</code>.<br>
 * In order to call the method {@code getFilterCriteria} first the following field have to be initialized 
 * using the setters method:<br>
 *  <code>BusAttributeField field;</code><br>
 *  <code>RelationalOperator relationalOperator;</code><br>
 *	<code>String value;</code><br>
 *
 * When the constructor is being called it instantiates the variable {@code myFieldHandler} with the right class that handles the behavior of this field.
 * The field classes are to be found in packages:<br>
 * {@link com.myBusApp.filtering.fields.Bus}<br>
 * {@link com.myBusApp.filtering.fields.Driver}<br>
 * 
 * Its field Class extends a specific field type Class depending on the type of the field (integer,long, boolean).<br>
 * The field Classes deliver a specific Predicate, with whom a list of drivers is going to be filtered using stream.filter().<br><br>
 * 
 * The field type Classes are to be found in the package {@link com.myBusApp.filtering.abstractClasses.fieldtypes}.<br>
 * Those are abstract implementing just a validation logic for each type.<br>
 * 
 * 
 */
public class BusSingleCriteria {
	
	//Field specifies the field class that is going to handle the field behavior
	private ValueType myFieldHandler;
	
	//Field specifies which field an BusDTO instance are the class going to compare with
	private BusAttributeField field;
	//Variable specifies in which way will be the field compared with the variable
	private RelationalOperator relationalOperator;
	//Value to compare with the field of an BusDTO instance 
	private String value;
	
	private BusSingleCriteria(BusAttributeField field, RelationalOperator relationalOperator, String value) {
		super();
		this.field = field;
		this.relationalOperator = relationalOperator;
		this.value = value;
		initializeMyField();	
	}
	
	public static BusSingleCriteriaBuilder newBuilder()
    {
        return new BusSingleCriteriaBuilder();
    }

	public BusAttributeField getField() {
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
		case CAR_ENGINE:
			myFieldHandler=new EngineTypeField(value,relationalOperator);
			break;
		case CAR_MANUFACTURER:
			myFieldHandler=new BusManufacturer(value,relationalOperator);
			break;
		case CONVERTIBLE:
			myFieldHandler=new Convertible(value,relationalOperator);
			break;
		case RATING:
			myFieldHandler=new Rating(value,relationalOperator);
			break;
		case SEAT_COUNT:
			myFieldHandler=new SeatCount(value,relationalOperator);
			break;
		default:
			break;
		}
	}
	
	/**
	 * The method delivers Predicates for a list of drivers checking if the inner variable BusDTO busDetails meets some criteria.
	 * @return Predicate<BusDTO>
	 */
	@JsonIgnore
	public Predicate<DriverDO> getBusFilterCriteria() {
		return myFieldHandler.getFilterCriteria();
	}
	
	/**
	 * Checks if Value provided is valid.
	 * for example if an integer field has no alphanumeric value. 
	 * @return boolean
	 */
	@JsonIgnore
	public boolean isValueValid(){
		return value!=null && myFieldHandler.isDataValid();
	}
	
	@JsonIgnore
	public boolean isBusAttributeValid() {
		return field!=null
				&& EnumUtils.getEnumList(BusAttributeField.class).contains(field);
	}
	
	@JsonIgnore
	public boolean isRelationalOperatorValid() {
		return relationalOperator!=null
				&& EnumUtils.getEnumList(RelationalOperator.class).contains(relationalOperator);
	}
	
	@JsonIgnore
	public boolean isRelationalOperatorSupported() {
		return relationalOperator!=null
				&& myFieldHandler.isRelationalOperatorSupported();
	}
	
	public static class BusSingleCriteriaBuilder
    {
		private BusAttributeField field;
		private RelationalOperator relationalOperator;
		private String value;

		public BusSingleCriteriaBuilder setField(BusAttributeField field) {
			this.field = field;
			return this;
		}
		
		public BusSingleCriteriaBuilder setRelationalOperato(RelationalOperator relationalOperator) {
			this.relationalOperator = relationalOperator;
			return this;
		}
		
		public BusSingleCriteriaBuilder setValue(String value) {
			this.value = value;
			return this;
		}

        public BusSingleCriteria createBusSingleCriteria()
        {
            return new BusSingleCriteria(this.field,this.relationalOperator,this.value);
        }

    }

}
