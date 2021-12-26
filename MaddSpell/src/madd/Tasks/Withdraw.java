package madd.Tasks;

import org.powbot.api.Condition;
import org.powbot.api.Random;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Inventory;
import org.powbot.api.rt4.Movement;
import org.powbot.api.rt4.Widgets;
import org.powbot.mobile.script.ScriptManager;
import madd.SharedPackage.Task;
import madd.MaddSpell;
import madd.Utility.GV;

public class Withdraw extends Task {


    MaddSpell main;

    public Withdraw(MaddSpell main) {
        super();
        super.name = "BankInteract";
        this.main = main;
    }

    @Override
    public boolean activate() {
        return Bank.opened();
    }

    @Override
    public void execute() {
        main.currentStatus = "Withdrawing the goods...";
        System.out.println("Bank task activated");

        if (Bank.stream().name(GV.logs).count() == 0) {
            ScriptManager.INSTANCE.stop();
        }

        if (Bank.depositAllExcept("Rune pouch", "Coins")) {
            System.out.println("Deposited everything except rune pouch");
        }

        if (Inventory.stream().name(GV.logs).isEmpty()) {
            System.out.println("Withdrawing logs");
            if(Bank.withdraw(GV.logs, Bank.Amount.ALL)) {
                Condition.wait(() -> Inventory.stream().name(GV.logs).count() > 0, 150, 15);
            }
        }


        Bank.close();
        Condition.wait(() -> !Bank.opened(), 150, 15);


    }
}