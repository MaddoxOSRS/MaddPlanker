package madd;


import madd.SharedPackage.Constants;
import madd.Tasks.Cast;
import org.powbot.api.Color;
import org.powbot.api.rt4.Game;
import org.powbot.api.rt4.Skills;
import org.powbot.api.rt4.walking.model.Skill;
import org.powbot.api.script.*;
import org.powbot.api.script.paint.Paint;
import org.powbot.api.script.paint.PaintBuilder;
import org.powbot.api.script.paint.TrackInventoryOption;
import org.powbot.mobile.script.ScriptManager;
import org.powbot.mobile.service.ScriptUploader;
import madd.SharedPackage.Task;
import madd.Tasks.Withdraw;
import madd.Utility.GV;

import java.util.ArrayList;

@ScriptManifest(name = "MaddPlanker", description = "Turns teak logs into planks using Plank make",
        version = "1.0.0", category = ScriptCategory.Magic)

@ScriptConfiguration(
        name = "Minutes",
        description = "How many minutes do you want to run the script?",
        defaultValue = "",
        optionType = OptionType.INTEGER,
        enabled = true,
        visible = true
)

@ScriptConfiguration(
        name = "Select log",
        description = "What log do you want to turn to plank?",
        defaultValue = "Oak logs",
        allowedValues = {"Oak logs", "Teak logs", "Mahogany logs"},
        optionType = OptionType.STRING,
        enabled = true,
        visible = true
)



public class MaddSpell extends AbstractScript {


    public int levelCurrent, xpCurrent, startXp, startTime = 0, time = 0, time1 = 0, time2 = 0, time3 = 0, time4 = 0, xpGained;
    double xpPerHour = 0;
    int minutes;
    public boolean xpConfirmed;
    public long lastActivated = 0;
    long currentRuntime = ScriptManager.INSTANCE.getRuntime(true);
    private ArrayList<Task> taskList = new ArrayList<Task>();

    public static void main(String[] args) {
        new ScriptUploader().uploadAndStart("MaddPlanker", "main", "127.0.0.1:5555", true, false);
    }

    public void onStart() {

        GV.logs = (String) getOption("Select log");
        minutes = (Integer) getOption("Minutes") * 60000; // Pulls the chosen settings and saves it to minutes
        if(GV.logs.contentEquals("Oak logs")){
            GV.planks="Oak plank";
        }
        else if(GV.logs.contentEquals("Teak logs")){
            GV.planks="Teak plank";
        }
        else if(GV.logs.contentEquals("Mahogany logs")){
            GV.planks="Mahogany plank";
        }
        taskList.add(new Withdraw(this));
        taskList.add(new Cast(this));
            if (Game.loggedIn() && ! xpConfirmed) {
                xpConfirmed = true;
                startXp = Skills.experience(org.powbot.api.rt4.Constants.SKILLS_MAGIC);
                Paint p = new PaintBuilder()
                        .backgroundColor(Color.argb(128, 128, 20, 20))
                        .trackSkill(Skill.Magic)
                        .trackInventoryItems(Constants.OAKPLANK)
                        .trackInventoryItems(Constants.TEAKPLANK)
                        .trackInventoryItems(Constants.MAHOGPLANK)
                        .trackInventoryItem(995, "Spent", TrackInventoryOption.Price)  .x(30).y(50)
                        .build();

                addPaint(p);
        }

    }

    @Override
    public void poll() {
        if (currentRuntime - lastActivated > minutes) {
            ScriptManager.INSTANCE.stop();
        }

        for (Task t : taskList) {
            if (t.activate()) {
                t.execute();
                if (ScriptManager.INSTANCE.isStopping()) {
                    break;
                }
            }
        }
    }

}
