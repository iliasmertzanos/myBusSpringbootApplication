package com.myBusApp.domainvalue;


public enum AttributeField {
    CAR_ENGINE("engineType",Group.CAR), CAR_MANUFACTURER("manufacturer",Group.CAR), SEAT_COUNT("seatCount",Group.CAR), RATING("rating",Group.CAR), CONVERTIBLE("convertible",Group.CAR)
    ,DRIVER_ID("id",Group.DRIVER),USER_NAME("username",Group.DRIVER),DELETED("deleted",Group.DRIVER),ONLINE_STATUS("onlineStatus",Group.DRIVER);
	
	private final String fieldDescription;
	
	private Group group;

    private AttributeField(String value, Group group) {
        fieldDescription = value;
        this.group=group;
    }

    public String getFieldDescription() {
        return fieldDescription;
    }
    
    public boolean isInGroup(Group group) {
        return this.group == group;
    }
    
    public enum Group {
        CAR,DRIVER;
    }

}
