package alexpetmecky.contraption.listeners;

import alexpetmecky.contraption.Contraption;
import alexpetmecky.contraption.ContraptionRun;
import alexpetmecky.contraption.inventories.dropperInventory;
import alexpetmecky.contraption.misc.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Array;
import java.util.*;


public class InventoryListener implements Listener {
    Contraption plugin;
    ContraptionBackend backend = new ContraptionBackend();
    public InventoryListener(Contraption plugin){
        this.plugin=plugin;
    }
    @EventHandler
    public void invEvent(InventoryOpenEvent event){
        System.out.println(event.getView().getTitle());
        if(event.getView().getTitle().equals("Dropper")){
            //creates the contraption block
            event.setCancelled(true);
            Player p = (Player) event.getPlayer();
            dropperInventory customInv = new dropperInventory();
            //Inventory customInv =
            p.openInventory(customInv.getInventory());
        }else if(event.getView().getTitle().equals("Dispenser")){
            //dont use this
            //use the eventhandler for item move events
            //ContraptionRun runner = new ContraptionRun();
            //runner.run();
        }

    }

    @EventHandler
    public void invMove(InventoryMoveItemEvent event){//triggered from hopper
        //uses the contraption block
        //add a check to make sure that it is the right items




        //System.out.println("HERE");
        //contraption execution called here
        Inventory invDest = event.getDestination();//a memory address
        System.out.println(invDest.getType());
        if(invDest.getType() != InventoryType.DISPENSER){
            return;
        }
        Location destLoc = event.getDestination().getLocation();
        System.out.println(destLoc);

        List<Entity> nearbyEntities= HelperFunctions.getNearbyEntities(destLoc,2);

        //List<Block> nearbyBlocks= HelperFunctions.getNearbyBlocks(destLoc,1);
        //System.out.println("Size of list: "+nearbyEntities.size());

        //Location item_frame_loc =null;
        ItemFrame contrapFrame = null;
        for (Entity e:nearbyEntities) {
            System.out.println(e.getType());
            //Boolean check= HelperFunctions.checkBlock(b,Material.ITEM_FRAME);
            Boolean check = HelperFunctions.checkEntity(e, EntityType.ITEM_FRAME);
            if(check){
                System.out.println("Check passes");
                //Location item_frame_loc = new Location(Bukkit.getWorld("world"), e.getX(), e.getY(), e.getZ());
                //contrapFrame = (ItemFrame) item_frame_loc.getBlock().getState();
                contrapFrame = (ItemFrame) e;
                break;
            }
        }

        if(contrapFrame != null){
            //itemframe is found on the contraption
            System.out.println("Contraption Frame Not null");
            ItemStack insideItem = contrapFrame.getItem();
            System.out.println(insideItem.getItemMeta().getDisplayName());

            String insideItemName= insideItem.getItemMeta().getDisplayName();
            String insideItemPart = insideItemName.substring(0,17);
            String[] brokenName = insideItemName.split(" ");
            int contraptionNumber = Integer.parseInt(brokenName[2]);

            if(insideItemPart.equals("Contraption Block")){
                //in the recycler make sure to change the contrapion block into the first part of it and to delete associated arraylist // it may be ok?
                System.out.println("IF PASSED");

                //i need the item that is being passes in the event
                Material passedItem = event.getItem().getType();
                //passedItem may need to become a string, but it is in the same format as what is stored in the backend
                //it does not include minecraft: --> I do not need to add it
                System.out.println("Passed Item: "+passedItem);

                System.out.println("Keys: ");
                backend.checkItem(contraptionNumber);


                if(backend.itemExistsInStorage(contraptionNumber,passedItem.toString())){
                    //it is in the storage, update it in currStorge and check if it can produce
                    //event.setCancelled(true);
                    backend.increaseItemInStorage(contraptionNumber,passedItem.toString(),1);


                    //the item needs to be deleted after being passed into the function
                    ///////////////////////
                    //delete the given item
                    plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {

                        public void run() {

                            event.getDestination().removeItem(event.getItem());

                        }
                    }, 1L);

                    ///////////////////////



                    if(backend.shouldProduce(contraptionNumber)){
                        System.out.println("PRODUCING");
                        //production should happen
                        HashMap<String,Double> produced =backend.produce(contraptionNumber);
                        //this only works for crafting i think

                        System.out.println("LENGHT OF PRODUCED: "+produced.size());
                        //as of APRIL 9 length of produced = 0
                        //that is the current problem, it should contain what is produced and its values
                        for(Map.Entry<String,Double> itemSet:produced.entrySet()){
                            System.out.println("Produce: "+itemSet.getKey() + " "+itemSet.getValue());

                            Material producedMaterial = Material.getMaterial(itemSet.getKey());
                            ItemStack createdItem = new ItemStack(producedMaterial);
                            event.getDestination().addItem(createdItem);
                        }

                    }else{
                        System.out.println("NOT ENOUGH TO PRODUCE");
                        //production should not happen
                        //not enough items in storage

                        backend.showInventory(contraptionNumber);

                        /////checking inventory //production does not happen, between comment needs to be deleted
                        //HashMap<String,Double> produced =backend.produce(contraptionNumber);
                        //this only works for crafting i think

                        //for(Map.Entry<String,Double> itemSet:produced.entrySet()){
                         //   System.out.println("CANNOT Produce: "+itemSet.getKey() + " "+itemSet.getValue());
                        //}

                        //////

                    }


                }else{
                    //not in the storage, do something
                    //cause a backup or something?
                    //maybe make it an infinate loop that puts it back in the hopper
                }
                //System.out.println()
                //backend.insertToCurrStorages(contraptionNumber,);


                //all of this dealt with storing the path inside the contraption block, i am no longer doing this
                /*
                ItemMeta contraptonMeta =  insideItem.getItemMeta();
                PersistentDataContainer data = contraptonMeta.getPersistentDataContainer();

                Set<NamespacedKey> keys = data.getKeys();
                System.out.println(keys);
                System.out.println(data.isEmpty());

                String path = data.get(keys.iterator().next(),PersistentDataType.STRING);

                System.out.println(path);

                //gets the index that the contraption is sotred in
                String[] brokenName = insideItemName.split(" ");

                int indexOfContrap = Integer.valueOf(brokenName[2]);
                //save space
                String[] sepPaths = path.split(";");

                for(String longPath:sepPaths){
                    //goes through each path
                    String[] pathParts = longPath.split(",");

                    ArrayList pathWeights = new ArrayList();

                    int currentPathValue = 0;//current path value is the total amount that can be produced when 1 of the initial item is given
                    String startingNode = pathParts[pathParts.length-1];
                    String prevItemTOFinal = pathParts[2];//0 is the final item node, 1 is the recipe that points to final, 2 is the item pointing to that recipe
                    for (int i=pathParts.length-1; i>=0;i--){//goes through each node in a given path
                        String[] node = pathParts[i].split("\\|");
                        int weight = Integer.valueOf(node[2]);
                        if(i == pathParts.length-1){
                            currentPathValue = weight;
                        }else if(weight < 0){
                            currentPathValue = currentPathValue / Math.abs(weight);
                        } else if (weight > 0) {
                            currentPathValue = currentPathValue * weight;
                        }


                    }


                }

*/
            }
        }

    }
    @EventHandler
    public void invClose(InventoryCloseEvent event){
        //creates the contraption block

        if(event.getView().getTitle().equals("Contraption Creator")){
            //System.out.println("INV CLOSE EVENT");
            //System.out.println("I AM HERE");
            HyperApi myApi = new HyperApi("STRING");
            //System.out.println("1");
            //myApi.searchGraph();
            //System.out.println("2");
            ArrayList<ItemStack> input = new ArrayList<>();
            ArrayList<ItemStack> output = new ArrayList<>();
            ArrayList<ItemStack> middle = new ArrayList<>();

            ArrayList<String> stringInput = new ArrayList<>();
            ArrayList<String> stringOutput = new ArrayList<>();
            ArrayList<String> stringMiddle = new ArrayList<>();
            //System.out.println("3");

            int invSlot=0;
            for(ItemStack item:event.getInventory().getContents()){
                //System.out.print(invSlot+" ");
                //System.out.println(item);


                //slots (0-3,9-12,18-21) are input
                //slots(5-8,14-17,23-26) are output
                //slots(4,13,22) are redstone
                if(item != null){
                    if( (invSlot >=0 && invSlot <=3)||(invSlot >=9 && invSlot <=12) || (invSlot >=18 && invSlot <=21) ){
                        //input
                        input.add(item);
                        stringInput.add(item.getType().name());
                        System.out.println("ADDED TO INPUT");
                        //System.out.println("TOSTRING: "+item.getType().);
                        //System.out.println("TOSTRING: "+item.getType().name());
                        //System.out.println("INPUT:"+item.getType().name());
                    } else if ((invSlot >=5 && invSlot <=8)||(invSlot >=14 && invSlot <=17) || (invSlot >=23 && invSlot <=26)) {
                        //output
                        output.add(item);
                        stringOutput.add(item.getType().name());
                        System.out.println("ADDED TO OUTPUT");
                        //System.out.println("Output:"+item.getType().name());
                    }else if( invSlot == 4 || invSlot == 13 || invSlot == 22){
                        //needs to have at least 1? redstone
                        middle.add(item);
                        stringMiddle.add(item.getType().name());
                        System.out.println("MIDDLE:"+item.getType().name());
                    }else{
                        System.out.println("ERROR--> SLOT: "+invSlot+" NOT CAUGHT IN IF-ELSE CHAIN");
                    }
                }



                invSlot +=1;

            }
            System.out.println("4");
        //do math here
            String file = "/Users/alexpetmecky/Desktop/pluginStuff/ContraptionFull/src/main/resources/HyperGraphMini.csv";
            //HyperApi myAPI = new HyperApi(file);

            //to make this more efficint have them share as many variables as possible
            LinkedList<NodeMeta> path = null;
            ItemStack contraptionBlock = new ItemStack(Material.BLUE_GLAZED_TERRACOTTA);
            ItemMeta contrapMeta = contraptionBlock.getItemMeta();



            //naming the contraption
            //////////
            //if the code fails to find a path and create a contraption, make sure to delete the contraption from the storage
            int contrapNum = backend.getLength();
            contrapMeta.setDisplayName("Contraption Block "+Integer.toString(contrapNum));
            //add this contraption block to the list
            backend.addStorage();

            //////////

            //adding output pieces to storage
            for(String produced: stringOutput){

                //System.out.println("Adding to amountProducedPerSet"+produced);
                backend.insertToAmountProducedPerSet(contrapNum,produced,1);
            }


            if(stringInput.size() == 1 && stringOutput.size() >1){
                //This is now recycling
                System.out.println("Recycling");

                //this is currently just testing it may need to be reworked
                for(String item:stringOutput){
                    SearchReturn tempReturn = myApi.searchGraphSingle(stringInput.get(0), item);
                    path = HelperFunctions.generatePath(tempReturn);
                    System.out.println("SIZE OF PATH: " + path.size());

                    HelperFunctions.printPath(path);//use this as a test function

                }



            }else if(stringOutput.size() == 1 && stringInput.size() >1) {
                //this is now crafting
                //input here means input on the graph, output is what is being made
                System.out.println("Crafting");
                //uncrafting-->switching it to crafting

                //my testing and direction of the arrows has made this crafting

                //System.out.println("5");
                //System.out.println("SIZE OF stringOutput: " + stringInput.size());

                //LinkedList<NodeMeta> path = null;

                //ItemStack contraptionBlock = new ItemStack(Material.BLUE_GLAZED_TERRACOTTA);
                //ItemMeta contrapMeta = contraptionBlock.getItemMeta();


                //for(String produced: stringOutput){

                    //System.out.println("Adding to amountProducedPerSet"+produced);
                //    backend.insertToAmountProducedPerSet(contrapNum,produced,1);
                //}





                for (String item : stringInput) {


                    SearchReturn tempReturn = myApi.searchGraphSingle(stringOutput.get(0), item);
                    //item here is the "final node" in the search (for breaking down pick, this could be ingot)

                    path = HelperFunctions.generatePath(tempReturn);
                    System.out.println("SIZE OF PATH: " + path.size());

                    HelperFunctions.printPath(path);//use this as a test function


                    double amountPerStartItem = HelperFunctions.findAmountPerPath(path);//i need to test this function still
                    //amountPerStartItem is the amount that can be produced for 1 unit of input


                    //im not going to use all the item before stuff
                    System.out.println(item + " output: " + amountPerStartItem);

                    backend.insertToProducedPerItem(contrapNum, item, amountPerStartItem);//putting in how much is created per item given

                    backend.insertToCurrStorages(contrapNum,item);
                    //String itemBeforeFinal = amountPerStartItemMinus2.keySet();

                }


                    //setting lore
                    String lore = stringInput + " To " + stringOutput;
                    ArrayList<String> loreList = new ArrayList<String>();
                    loreList.add(lore);
                    contrapMeta.setLore(loreList);

                    //putting block together // it gets named above, where contrapNum is initialized
                    contraptionBlock.setItemMeta(contrapMeta);
                    //giving the player the contraption block
                    event.getPlayer().getInventory().addItem(contraptionBlock);
                    System.out.println("Done");





            }else if(stringInput.size() == 1 && stringOutput.size() ==1) {
                //unclear if crafting or uncrafting
                System.out.println("what");
            }else{
                //contitions not satisfied, user messed up

                /*

                System.out.println("OUTPUT SIZE: "+stringOutput.size());
                for (String out:stringOutput) {
                    System.out.println(out);
                }

                System.out.println("INPUT SIZE: "+stringInput.size());
                for (String in:stringOutput) {
                    System.out.println(in);
                }
                */
            }

            }

        }


    @EventHandler
    public void invInteract(InventoryClickEvent event){
        //String displayName = event.getCurrentItem().getItemMeta().getDisplayName();
        if(event.getCurrentItem() != null){
            if(event.getCurrentItem().hasItemMeta()){
                if(event.getCurrentItem().getItemMeta().getDisplayName().equals(" ")){
                    event.setCancelled(true);
                }else if(event.getCurrentItem().getItemMeta().getDisplayName().equals("Redstone Dust Here")){
                    //event.getCurrentItem().
                    event.getClickedInventory().removeItem(event.getCurrentItem());
                }
            }
        }





    }


}

