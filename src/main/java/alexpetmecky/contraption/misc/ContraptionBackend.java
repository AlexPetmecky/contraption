package alexpetmecky.contraption.misc;

import alexpetmecky.contraption.Contraption;

import java.util.ArrayList;
import java.util.HashMap;

public class ContraptionBackend {
    //ArrayList<HashMap<String,Integer>> storageList =new ArrayList<>();

    ArrayList<ContraptionStorage> storageList = new ArrayList<>();//i think i am going to use this

    public ContraptionBackend() {

    }

    public void addStorage(){
        //HashMap<String,Integer> storageMap = new HashMap<>();
        ContraptionStorage newStorage = new ContraptionStorage();
        storageList.add(newStorage);


    }

    public int getLength(){
        return storageList.size();
    }


    public boolean shouldProduce(int contraptionIndex){
        ContraptionStorage currStorage = storageList.get(contraptionIndex);

        return currStorage.shouldProduce();

    }
    public HashMap<String, Double> produce(int contraptionIndex){
        ContraptionStorage currStorage = storageList.get(contraptionIndex);
        return currStorage.produce();
    }
    public void showInventory(int contraptionIndex){
        ContraptionStorage currStorage = storageList.get(contraptionIndex);
        HashMap<String,Double> storage= currStorage.getCurrStorage();
        for(String key: storage.keySet()){
            System.out.println(key + " "+storage.get(key));
        }
    }
    public void checkItem(int contraptionIndex){
        ContraptionStorage currStorage =  storageList.get(contraptionIndex);
        HashMap<String,Double> storage= currStorage.getCurrStorage();
        for(String key: storage.keySet()){
            System.out.println(key);
        }
    }

    public void insertToProducedPerItem(int contraptionIndex,String item, double amount){
        ContraptionStorage currStorage = storageList.get(contraptionIndex);
        currStorage.setAmountProducedPerItem(item,amount);
        storageList.set(contraptionIndex,currStorage);
    }
    public void insertToNeededPerOutput(int contraptionIndex, String item, int amount){
        //ContraptionStorage currStorage = storageList.get(contraptionIndex);
        //currStorage.setAmountNeededPerSet(item,amount);
        //storageList.set(contraptionIndex,currStorage);
    }
    public void insertToCurrStorages(int contraptionIndex, String item){
        ContraptionStorage currStorage = storageList.get(contraptionIndex);
        //currStorage.setAmountProducedPerItem(item,0);
        currStorage.setCurrStorage(item);
        storageList.set(contraptionIndex,currStorage);
    }

    public void increaseItemInStorage(int contraptionIndex,String item,int amount){
        ContraptionStorage currStorage = storageList.get(contraptionIndex);
        currStorage.updateStored(item,amount);
        storageList.set(contraptionIndex,currStorage);


    }
    public boolean itemExistsInStorage(int contraptionIndex,String passedItemToCheck){
        ContraptionStorage currStorage = storageList.get(contraptionIndex);
        if(currStorage.getCurrStorage().containsKey(passedItemToCheck)){
            return true;
        }else{
            return false;
        }
    }

}
