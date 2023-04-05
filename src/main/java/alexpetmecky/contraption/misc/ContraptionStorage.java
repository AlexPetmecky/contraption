package alexpetmecky.contraption.misc;

import java.util.ArrayList;
import java.util.HashMap;

public class ContraptionStorage {

    HashMap<String,Integer> amountNeededPerSet = new HashMap<>();//this will be in reference to the amountMadeBeforeFinal
                                                //so in the example of logs to pickaxe, it should store stick:2, iron_ingot:3
    HashMap<String,Double> amountProducedPerSet = new HashMap<>(); //this is how many outputs 1 input makes; example(1 oak log makes 4 pickaxes, 1 iron_ingot makes .33333 pickaxes)

    HashMap<String,Double> amountMadeBeforeFinal = new HashMap<>();//this is going to be 1 item node before the final output item
                                                    //so producing logs to pickaxes; this will store sticks:8

    HashMap<String,Integer> currStorage = new HashMap<>(); //this will also store the items tracked amountMadeBeforeFinal
                                        //in the example of logs and iron ingots to pickaxes; it will store sticks and ingots




    public void setAmountMadeBeforeFinal(String item,double amount){
        amountMadeBeforeFinal.put(item,amount);
    }
    public void setAmountNeededPerSet(String item,int amount){
        amountNeededPerSet.put(item,amount);
    }
    public void setAmountProducedPerSet(String item,double amount){
        amountProducedPerSet.put(item,amount);
    }
    public void setCurrStorage(String item){
        currStorage.put(item,0);
    }
    public void updateStored(String name, int amount){
        int storedAmount = currStorage.get(name);
        storedAmount +=amount;
        currStorage.put(name,storedAmount);
    }

    public boolean shouldProduce(){
        for(String key:currStorage.keySet()){
            if (currStorage.get(key) < amountNeededPerSet.get(key)){
                return false;
            }
        }
        return true;

    }

    public HashMap<String, Double> produce(){//should return a hashmap of all the items that can produce
        for(String key:currStorage.keySet()){
            int newAmount = currStorage.get(key)-amountNeededPerSet.get(key);
            currStorage.put(key,newAmount);
        }
        return amountProducedPerSet;
    }









}
