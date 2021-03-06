package Gensokyo.events.act2;

import Gensokyo.GensokyoMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.green.Nightmare;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static Gensokyo.GensokyoMod.makeEventPath;

public class DetectiveSatori extends AbstractImageEvent {


    public static final String ID = GensokyoMod.makeID("DetectiveSatori");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Satori.png");

    private static final float MAX_HP_LOSS = 0.06F;
    private static final float A15_MAX_HP_LOSS = 0.08F;
    private int maxHpLoss;

    private static final float HEALTH_LOSS_PERCENTAGE = 0.10F;
    private static final float HEALTH_LOSS_PERCENTAGE_HIGH_ASCENSION = 0.13F;
    private int healthdamage;

    private static final int GOLD = 250;

    private int screenNum = 0;

    public DetectiveSatori() {
        super(NAME, DESCRIPTIONS[0], IMG);
        imageEventText.setDialogOption(OPTIONS[0]);
        if (AbstractDungeon.ascensionLevel >= 15) {
            this.maxHpLoss = (int)((float)AbstractDungeon.player.maxHealth * A15_MAX_HP_LOSS);
            healthdamage = (int) ((float) AbstractDungeon.player.maxHealth * HEALTH_LOSS_PERCENTAGE_HIGH_ASCENSION);
        } else {
            this.maxHpLoss = (int)((float)AbstractDungeon.player.maxHealth * MAX_HP_LOSS);
            healthdamage = (int) ((float) AbstractDungeon.player.maxHealth * HEALTH_LOSS_PERCENTAGE);
        }
        this.maxHpLoss = Math.max(this.maxHpLoss, 1); //Ensures the player loses a minimum of 1 Max HP.
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                this.imageEventText.clearAllDialogs();
                AbstractCard card = new Nightmare();
                this.imageEventText.setDialogOption(OPTIONS[1] + healthdamage + OPTIONS[2] + FontHelper.colorString(card.name, "g") + OPTIONS[3], card);
                this.imageEventText.setDialogOption(OPTIONS[4] + maxHpLoss + OPTIONS[5] + OPTIONS[6] + GOLD + OPTIONS[7]);
                imageEventText.setDialogOption(OPTIONS[8]);
                screenNum = 1;
                break;
            case 1:
                switch (buttonPressed) {
                    case 0:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[9]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.player.damage(new DamageInfo(null, healthdamage));
                        AbstractCard c = new Nightmare();
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c, (float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[9]);
                        this.imageEventText.clearRemainingOptions();
                        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.MED, false);
                        // Shake the screen
                        CardCrawlGame.sound.play("ATTACK_POISON");  // Play a hit sound
                        AbstractDungeon.player.decreaseMaxHealth(this.maxHpLoss);
                        AbstractDungeon.effectList.add(new RainingGoldEffect(GOLD));
                        AbstractDungeon.player.gainGold(GOLD);
                        break;
                    case 2:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[9]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                }
                break;
            case 2:
                this.openMap();
                break;
            default:
                this.openMap();
        }
    }
}
