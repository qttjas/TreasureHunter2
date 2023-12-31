/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all the things a Hunter can do in town.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Town {
    // instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;
    private String assignedTreasure;
    private boolean easy = true;
    private boolean dug;

    private boolean searchedTown;
    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     *
     * @param shop The town's shoppe.
     * @param toughness The surrounding terrain.
     */
    public Town(Shop shop, double toughness) {
        this.shop = shop;
        this.terrain = getNewTerrain();

        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;

        printMessage = "";

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);

        searchedTown = false;
    }

    public String getLatestNews() {
        return printMessage;
    }
    /////treasure
    //random treasure
    private void randomTreasure() {
        double x = Math.random();
        if (x < 0.33) {
            assignedTreasure = "Gem";
        }else if ( x < 0.66) {
            assignedTreasure = "Trophy";
        }else {
            assignedTreasure = "Crown";
        }
    }

    public void huntForTreasure() {
        if (!searchedTown) {
            double x = Math.random();
            String t;
            if (x < 0.25) {
                t = "a crown";
            }else if (x < 0.5) {
                t = "a trophy";
            }else if (x < 0.75) {
                t = "a gem";
            }else {
                t = "dust";
            }
            System.out.println();
            System.out.println("you found " + t + "!");
            searchedTown = true;
            if (!t.equals("dust")) {
                if (!hunter.hasTreasure(t)) {
                    hunter.addTreasure(t);
                }else {
                    System.out.println("you've already collected this treasure");
                }

            }
        }else {
            System.out.println();
            System.out.println("you've already searched this town");
        }
    }

    //////^^^^treasure^^^^/////
    /**
     * Assigns an object to the Hunter in town.
     *
     * @param hunter The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter) {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";

        if (toughTown) {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        } else {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
    }

    /**
     * Handles the action of the Hunter leaving the town.
     *
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown() {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown && easy) {
            String item = terrain.getNeededItem();
            printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
            return true;
        } else if (canLeaveTown) {
            String item = terrain.getNeededItem();
            printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
            if (!easy && checkItemBreak()) { // check
                hunter.removeItemFromKit(item);
                printMessage += "\nUnfortunately, you lost your " + item + ".";
            }

            return true;
        }

        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
        return false;
    }

    /**
     * Handles calling the enter method on shop whenever the user wants to access the shop.
     *
     * @param choice If the user wants to buy or sell items at the shop.
     */
    public void enterShop(String choice) {
        shop.enter(hunter, choice);
        printMessage = "You left the shop. ";
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble() {
        double noTroubleChance;
        if (toughTown) {
            noTroubleChance = 0.66;
        } else {
            noTroubleChance = 0.33;
        }

        if (Math.random() > noTroubleChance) {
            printMessage = "You couldn't find any trouble";
        } else {
            printMessage = Colors.RED + "You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n";
            int goldDiff = (int) (Math.random() * 10) + 1;
            if (TreasureHunter.samuraiMode) {
                printMessage += "Your sword has taken a life. Once again, a great victory for our Japan.";
                printMessage += "\nGlory to the Almighty Shogun";
                hunter.changeGold(goldDiff);
            } else if (Math.random() > noTroubleChance) {
                printMessage += "Okay, stranger! You proved yer mettle. Here, take my gold.";
                printMessage += "\nYou won the brawl and receive " + Colors.YELLOW + goldDiff + Colors.RED + " gold." + Colors.RESET;
                hunter.changeGold(goldDiff);
            } else {
                printMessage += "That'll teach you to go lookin' fer trouble in MY town! Now pay up!";
                printMessage += "\nYou lost the brawl and pay " + Colors.YELLOW + goldDiff + Colors.RED + " gold." + Colors.RESET;
                hunter.changeGold(-goldDiff);
            }
        }
    }

    public void easyLookForTrouble() {
        double noTroubleChance;
        if (toughTown) {
            noTroubleChance = 0.66;
        } else {
            noTroubleChance = 0.33;
        }

        if (Math.random() > noTroubleChance) {
            printMessage = "You couldn't find any trouble";
        } else {
            printMessage = Colors.RED + "You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n" + Colors.RESET;
            int goldDiff = (int) (Math.random() * 10) + 1;
            if (Math.random() > .2) {
                printMessage += "Okay, stranger! You proved yer mettle. Here, take my gold.";
                printMessage += "\nYou won the brawl and receive " + Colors.YELLOW + goldDiff + Colors.RED + " gold." + Colors.RESET;
                hunter.changeGold(goldDiff);
            } else {
                printMessage += "That'll teach you to go lookin' fer trouble in MY town! Now pay up!";
                printMessage += "\nYou lost the brawl and pay " + Colors.YELLOW + goldDiff + Colors.RED + " gold." + Colors.RESET;
                hunter.changeGold(-goldDiff);
            }
        }
    }


    public String toString() {
        return "This nice little town is surrounded by " + Colors.CYAN + terrain.getTerrainName() + Colors.RESET + ".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain() {
        double rnd = Math.random();
        if (rnd < 1.0/6) {
            return new Terrain("Mountains", "Rope");
        } else if (rnd < 2.0/6) {
            return new Terrain("Ocean", "Boat");
        } else if (rnd < 3.0/6) {
            return new Terrain("Plains", "Horse");
        } else if (rnd < 4.0/6) {
            return new Terrain("Desert", "Water");
        } else if (rnd < 5.0/6){
            return new Terrain("Jungle", "Machete");
        } else {
            return new Terrain("Marsh", "Boot");
        }
    }

    /**
     * Determines whether a used item has broken.
     *
     * @return true if the item broke.
     */
    private boolean checkItemBreak() {
        double rand = Math.random();
        return (rand < 0.5);
    }
    public void digForTreasure(Hunter hunter){
        if (!hunter.hasItemInKit("shovel")){
            System.out.println("You can't dig for gold without a shovel\n");
        } else {
            if (dug) {
                System.out.println("You already dug for gold in this town\n");
            } else {
                double chance = Math.random();
                if (chance >= .5) {
                    int goldFound = (int) (Math.random() * 20 + 1);
                    hunter.changeGold(goldFound);
                    System.out.println("You found " + goldFound + " gold\n");
                } else {
                    System.out.println("You dug but only found dirt.\n");
                }
                dug = true;
            }
        }
    }
}