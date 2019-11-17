package Gensokyo.patches.relics;

import Gensokyo.relics.PortableGap;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;

import java.util.Iterator;

public class PortableGapPatch
{
    private static MapRoomNode getNode(int x, int y)
    {
        try {
            return CardCrawlGame.dungeon.getMap().get(y).get(x);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @SpirePatch(
            clz= MapRoomNode.class,
            method="isConnectedTo"
    )
    public static class IsConnectedTo
    {

        public static boolean Postfix(boolean __result, MapRoomNode __instance, MapRoomNode node)
        {
            if (!__result && AbstractDungeon.player.hasRelic(PortableGap.ID)) {
                Iterator var2 = __instance.getEdges().iterator();
                MapEdge edge;
                do {
                    if (!var2.hasNext()) {
                        return false;
                    }

                    edge = (MapEdge)var2.next();
                    if (AbstractDungeon.player.hasRelic(PortableGap.ID) && node.y >= edge.dstY) {
                        return true;
                    }
                } while(node.y < edge.dstY || !AbstractDungeon.player.hasRelic(PortableGap.ID));
            }

            return __result;
        }

        static int getNodeDistance(MapRoomNode start, MapRoomNode end)
        {
            return getNodeDistance(start, end, 1);
        }

        private static int getNodeDistance(MapRoomNode start, MapRoomNode end, int depth)
        {
            if (start == null) {
                return -1;
            }

            for (MapEdge edge : start.getEdges()) {
                MapRoomNode nextNode = getNode(edge.dstX, edge.dstY);
                if (nextNode != null && nextNode.equals(end)) {
                    return depth;
                }
                int dist = getNodeDistance(nextNode, end, depth+1);
                if (dist != -1) {
                    return dist;
                }
            }
            return -1;
        }
    }

    @SpirePatch(
            cls="com.megacrit.cardcrawl.map.MapRoomNode",
            method="playNodeSelectedSound"
    )
    public static class NodeSelected
    {
        public static void Postfix(MapRoomNode __instance)
        {
            if (Settings.isDebug) {
                return;
            }

            int distance = IsConnectedTo.getNodeDistance(AbstractDungeon.getCurrMapNode(), __instance);
            if (distance > 1) {
                PortableGap portableGap = (PortableGap)AbstractDungeon.player.getRelic(PortableGap.ID);
                if (portableGap != null) {
                    portableGap.onTrigger(distance);
                }
            }
        }
    }
}