package alexpetmecky.contraption.misc;

import java.util.ArrayList;
import java.util.HashMap;

public class ContraptionStorage {

    HashMap<String,Integer> amountNeededPerSet = new HashMap<>();//this will be in reference to the amountMadeBeforeFinal
                                                //so in the example of logs to pickaxe, it should store stick:2, iron_ingot:3
    HashMap<String,Double> amountProducedPerItem = new HashMap<>(); //this is how many outputs 1 input makes; example(1 oak log makes 4 pickaxes, 1 iron_ingot makes .33333 pickaxes)

    //HashMap<String,Double> amountMadeBeforeFinal = new HashMap<>();//this is going to be 1 item node before the final output item
                                                    //so producing logs to pickaxes; this will store sticks:8

    HashMap<String,Double> currStorage = new HashMap<>(); //this will also store the items tracked amountMadeBeforeFinal
                                        //in the example of logs and iron ingots to pickaxes; it will store sticks and ingots

    HashMap<String,Double> amountProducedPerSet = new HashMap<>(); //this keeps how many of an item are created, for crafting it will normally just store 1 item, byt for recycling it will store all the componenets and the amount made


    public void setAmountNeededPerSet(String item,int amount){
        amountNeededPerSet.put(item,amount);
    }
    public void setAmountProducedPerItem(String item,double amount){
        amountProducedPerItem.put(item,amount);
    }
    public void setCurrStorage(String item){
        currStorage.put(item,0.0);
    }
    public HashMap<String,Double> getCurrStorage(){
        return currStorage;
    }
    public void updateStored(String name, double amount){
        double storedAmount = currStorage.get(name);
        storedAmount +=amount;
        currStorage.put(name,storedAmount);
    }

    public boolean shouldProduce(){
        double amountNeeded;
        for(String key:currStorage.keySet()){
            amountNeeded = 1.0 / amountProducedPerItem.get(key);
            if (currStorage.get(key) < amountNeeded){
                return false;
            }
        }
        return true;

    }

    public HashMap<String, Double> produce(){//should return a hashmap of all the items that can produce
        //something to keep in mind, when crafting i just need to subtract from currStroage and return the amountProducedPerSet
        //when recycling I need to do it enough times that it gives all outputs, this may be solved by the prev math, check it

        for(String key:currStorage.keySet()){
            double amountNeeded = 1 / amountProducedPerItem.get(key);//this may not be right because it assumes that it produces 1 item per , but in recycling it may produce mroe
            double newAmount = currStorage.get(key)-amountNeeded;//could try multipiying this, but i would need to make sure there is enough off all the other items
            currStorage.put(key,newAmount);
        }
        return amountProducedPerSet;
    }









}
