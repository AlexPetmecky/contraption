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


    public boolean shouldProduct(int contraptionIndex){
        ContraptionStorage currStorage = storageList.get(contraptionIndex);

        return currStorage.shouldProduce();

    }

    public void insertToAmountMadeBeforeFinal(int contraptionIndex,String item, double amount){
        ContraptionStorage currStorage = storageList.get(contraptionIndex);
        currStorage.setAmountMadeBeforeFinal(item,amount);
    }

    public void insertToProducedPer234(int contraptionIndex,String item, double amount){
        ContraptionStorage currStorage = storageList.get(contraptionIndex);
        currStorage.setAmountProducedPerSet(item,amount);
        storageList.set(contraptionIndex,currStorage);
    }
    public void insertToNeededPerOutput(int contraptionIndex, String item, int amount){
        ContraptionStorage currStorage = storageList.get(contraptionIndex);
        currStorage.setAmountNeededPerSet(item,amount);
        storageList.set(contraptionIndex,currStorage);
    }
    public void insertToCurrStorages(int contraptionIndex, String item){
        ContraptionStorage currStorage = storageList.get(contraptionIndex);
        currStorage.setAmountProducedPerSet(item,0);
        currStorage.setCurrStorage(item);
        storageList.set(contraptionIndex,currStorage);
    }

    public void increaseItemInStorage(int contraptionIndex,String item,int amount){
        ContraptionStorage currStorage = storageList.get(contraptionIndex);
        currStorage.updateStored(item,amount);
        storageList.set(contraptionIndex,currStorage);


    }

}
