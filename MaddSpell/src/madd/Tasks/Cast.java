package madd.Tasks;

import org.powbot.api.Condition;
import org.powbot.api.Input;
import org.powbot.api.Random;
import org.powbot.api.rt4.*;
import org.powbot.mobile.script.ScriptManager;
import madd.SharedPackage.Task;
import madd.MaddSpell;
import madd.Utility.GV;

import java.util.concurrent.Callable;

public class Cast extends Task {

    MaddSpell main;

    public Cast(MaddSpell main) {
        super();
        super.name = "Cast";
        this.main = main;
    }

    @Override
    public boolean activate() {
        return !Bank.opened()
                && Players.local().animation() == -1;
    }

    @Override
    public void execute() {
        System.out.println("Casting task");
        if (Inventory.stream().name(GV.logs).count() == 26) {
            System.out.println("should cast");
            Magic.LunarSpell.PLANK_MAKE.cast();
            Condition.sleep(Random.nextGaussian(1700, 2500, 250));
            //Inventory.stream().name(GV.logs).action("Cast");
            Inventory.stream().name(GV.logs).first().click();
                    System.out.println("Did cast");
            Condition.sleep(Random.nextGaussian(1700, 2500, 250));
            Condition.wait(() -> Players.local().animation() == -1, 100, 20);
        }
        if (Inventory.stream().name(GV.planks).count() == 26) {
            System.out.println("Clicking bank");
            Bank.open();
            Condition.wait(() -> Bank.opened(), 250, 150);
        }
    }
}
