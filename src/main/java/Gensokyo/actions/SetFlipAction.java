package Gensokyo.actions;

import Gensokyo.monsters.act2.Byakuren;
import Gensokyo.monsters.act2.Kaguya;
import Gensokyo.monsters.act1.NormalEnemies.AbstractFairy;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SetFlipAction extends AbstractGameAction {
    AbstractMonster mo;

    public SetFlipAction(AbstractMonster mo) {
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_FAST;
        this.mo = mo;
    }

    public void update() {
        this.isDone = false;

        if (mo instanceof AbstractFairy) {
            ((AbstractFairy)mo).setFlip(true, false);;
        }
        if (mo instanceof Kaguya) {
            ((Kaguya)mo).setFlip(true, false);;
        }
        if (mo instanceof Byakuren) {
            ((Byakuren)mo).setFlip(false, false);;
        }
        this.isDone = true;
    }
}


