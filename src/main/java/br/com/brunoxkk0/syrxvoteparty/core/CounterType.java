package br.com.brunoxkk0.syrxvoteparty.core;

public enum CounterType {

    DEFAULT,
    INVERTED;

    public static CounterType getByName(String name){

        for(CounterType type : CounterType.values()){
            if(type.name().equalsIgnoreCase(name)){
                return type;
            }
        }

        return DEFAULT;
    }

}
