package com.rebotted.game.content.combat;

import java.util.HashMap;
import com.rebotted.game.content.combat.range.RangeData;
import com.rebotted.game.content.music.sound.CombatSounds;
import com.rebotted.game.npcs.NpcHandler;
import com.rebotted.game.players.Client;
import com.rebotted.game.players.Player;
import com.rebotted.game.players.PlayerHandler;
import com.rebotted.util.Misc;

public class Specials {

	private final Player player;

	public Specials(Player player2) {
		this.player = player2;
	}

	private enum specialAttack {

		// ItemName(ItemId, SpecDamage, SpecAccuracy, SpecAmount, Anim, GFX0,
		// GFX100, DoubleHit, SsSpec, SpecEffect)

		ABYSSAL_WHIP(4151, 1, 1.25, 5, 1658, 341, -1, false, 0),
		DRAGON_DAGGER(1215, 1.15, 1.25, 2.5, 1062, -1, 252, true, 0),
		DRAGON_DAGGER_P(1231, 1.15, 1.25, 2.5, 1062, -1, 252, true, 0),
		DRAGON_DAGGER_PP(5698, 1.15, 1.25, 2.5, 1062, -1, 252, true, 0),
		DRAGON_DAGGER_PPP(5680, 1.15, 1.25, 2.5, 1062, -1, 252, true, 0),
		DRAGON_LONG(1305, 1.20, 1.10, 2.5, 1058, -1, 248, false, 0),
		DRAGON_MACE(1434, 1.55, 1.25, 2.5, 1060, -1, 251, false, 0),
		DRAGON_SCIMITAR(4587, 1, 1.25, 5.5, 1872, -1, 347, false, 1),
		DRAGON_HALBERD(3204, 1.25, .85, 3, 1203, -1, 282, true, 0),
		GRANITE_MAUL(4153, 1.10, .85, 5, 1667, -1, 337, false, 0),
		MAGIC_SHORTBOW(861, 1.05, .95, 5.5, 1074, -1, -1, true, 0), 
		MAGIC_LONGBOW(859, 1.20, 1.05, 5.5, 426, -1, -1, false, 0);
		
		private int weapon, anim, gfx1, gfx2, specEffect;
		private double specDamage, specAccuracy, specAmount;
		private boolean doubleHit;

		private specialAttack(int weapon, double specDamage,
				double specAccuracy, double specAmount, int anim, int gfx1,
				int gfx2, boolean doubleHit, int specEffect) {
			this.weapon = weapon;
			this.specDamage = specDamage;
			this.specAccuracy = specAccuracy;
			this.specAmount = specAmount;
			this.anim = anim;
			this.gfx1 = gfx1;
			this.gfx2 = gfx2;
			this.doubleHit = doubleHit;
			this.specEffect = specEffect;
		}

		private int getWeapon() {
			return weapon;
		}

		private double getSpecDamage() {
			return specDamage;
		}

		private double getSpecAccuracy() {
			return specAccuracy;
		}

		private double getSpecAmount() {
			return specAmount;
		}

		private int getAnim() {
			return anim;
		}

		private int getGfx1() {
			return gfx1;
		}

		private int getGfx2() {
			return gfx2;
		}

		private boolean getDoubleHit() {
			return doubleHit;
		}


		@SuppressWarnings("unused")
		private int getSpecEffect() {
			return specEffect;
		}

		public static HashMap<Integer, specialAttack> specialAttack = new HashMap<Integer, specialAttack>();

		@SuppressWarnings("unused")
		public static specialAttack getWeapon(int weapon) {
			return specialAttack.get(weapon);
		}

		static {
			for (specialAttack SA : specialAttack.values()) {
				specialAttack.put(SA.getWeapon(), SA);
			}
		}
	}

	public void activateSpecial(int weapon, Client o, int i) {
		int equippedWeapon = player.playerEquipment[player.playerWeapon];

		if (NpcHandler.npcs[i] == null && player.npcIndex > 0 || PlayerHandler.players[player.playerIndex] == null && player.playerIndex > 0) {
			return;
		}
		player.doubleHit = false;
		player.specEffect = 0;
		player.projectileStage = 0;
		player.specMaxHitIncrease = 2;
		if (player.npcIndex > 0) {
			player.oldNpcIndex = i;
		} else if (player.playerIndex > 0) {
			player.oldPlayerIndex = i;
			PlayerHandler.players[i].underAttackBy = player.playerId;
			PlayerHandler.players[i].logoutDelay = System.currentTimeMillis();
			PlayerHandler.players[i].singleCombatDelay = System.currentTimeMillis();
			PlayerHandler.players[i].killerId = player.playerId;
		}
		player.specEffect = 0;
		player.projectileStage = 0;
		for (specialAttack SA : specialAttack.values()) {
			if (NpcHandler.npcs[player.npcIndex] == null && player.npcIndex > 0) {
				return;
			}
			if (PlayerHandler.players[player.playerIndex] == null
					&& player.playerIndex > 0) {
				return;
			}
			if (equippedWeapon == SA.getWeapon()) {
				if (SA.getWeapon() == 11235) {
					player.usingBow = true;
					player.dbowSpec = true;
					player.rangeItemUsed = player.playerEquipment[player.playerArrows];
					player.getItemAssistant().deleteArrow();
					player.getItemAssistant().deleteArrow();
					player.lastWeaponUsed = weapon;
					player.hitDelay = 3;
					player.startAnimation(426);
					player.projectileStage = 1;
					player.gfx100(RangeData.getRangeStartGFX(player));
					player.hitDelay = player.getCombatAssistant().getHitDelay();
					if (player.fightMode == 2) {
						player.attackTimer--;
					}
					if (player.playerIndex > 0) {
						player.getCombatAssistant().fireProjectilePlayer();
					} else if (player.npcIndex > 0) {
						player.getCombatAssistant().fireProjectileNpc();
					}
					player.specAccuracy = SA.getSpecAccuracy();
					player.specDamage = SA.getSpecDamage();
				} else if (SA.getWeapon() == 15241) {
					player.usingBow = true;
					player.rangeItemUsed = player.playerEquipment[player.playerArrows];
					player.getItemAssistant().deleteArrow();
					player.lastWeaponUsed = weapon;
					player.startAnimation(12175);
					player.specAccuracy = SA.getSpecAccuracy();
					player.specDamage = SA.getSpecDamage();
					player.hitDelay = 5;
					player.attackTimer -= 7;
					player.hitDelay = player.getCombatAssistant().getHitDelay();
					if (player.fightMode == 2) {
						if (player.playerIndex > 0) {
							player.getCombatAssistant().fireProjectilePlayer();
						} else if (player.npcIndex > 0) {
							player.getCombatAssistant().fireProjectileNpc();
						}
					}
				} else if (SA.getWeapon() == 13879 || SA.getWeapon() == 13883) {
					player.usingRangeWeapon = true;
					player.rangeItemUsed = player.playerEquipment[player.playerWeapon];
					player.getItemAssistant().deleteArrow();
					player.lastWeaponUsed = weapon;
					player.startAnimation(SA.getAnim());
					player.gfx0(SA.getGfx1());
					player.specAccuracy = SA.getSpecAccuracy();
					player.specDamage = SA.getSpecDamage();
					player.projectileStage = 1;
					player.hitDelay = player.getCombatAssistant().getHitDelay();
					if (player.fightMode == 2) {
						player.attackTimer--;
					}
					if (player.playerIndex > 0) {
						player.getCombatAssistant().fireProjectilePlayer();
					} else if (player.npcIndex > 0) {
						player.getCombatAssistant().fireProjectileNpc();
					}
				} else if (SA.getWeapon() == 859 || SA.getWeapon() == 861) {
					player.usingBow = true;
					player.bowSpecShot = 1;
					player.rangeItemUsed = player.playerEquipment[player.playerArrows];
					player.getItemAssistant().deleteArrow();
					player.lastWeaponUsed = weapon;
					player.startAnimation(SA.getAnim());
					player.projectileStage = 1;
					player.hitDelay = player.getCombatAssistant().getHitDelay();
					if (player.fightMode == 2) {
						player.attackTimer--;
					}
					if (player.playerIndex > 0) {
						player.getCombatAssistant().fireProjectilePlayer();
					} else if (player.npcIndex > 0) {
						player.getCombatAssistant().fireProjectileNpc();
					}
				} else if (SA.getGfx1() == -1) {
					player.gfx100(SA.getGfx2());
					player.startAnimation(SA.getAnim());
					player.specDamage = SA.getSpecDamage();
					player.specAccuracy = SA.getSpecAccuracy();
					player.hitDelay = player.getCombatAssistant().getHitDelay();
					player.doubleHit = SA.getDoubleHit();
				} else {
					if (o != null)
					{
						o.gfx0(SA.getGfx1());
					}
					player.startAnimation(SA.getAnim());
					player.specDamage = SA.getSpecDamage();
					player.specAccuracy = SA.getSpecAccuracy();
					player.hitDelay = player.getCombatAssistant().getHitDelay();
					player.doubleHit = SA.getDoubleHit();
				}
			}
			player.delayedDamage = Misc.random(player.getCombatAssistant().meleeMaxHit());
			player.delayedDamage2 = Misc.random(player.getCombatAssistant().meleeMaxHit());
			player.usingSpecial = false;
			player.getItemAssistant().updateSpecialBar();
			if (CombatConstants.COMBAT_SOUNDS) {
				player.getPacketSender().sendSound(CombatSounds.specialSounds(player.playerEquipment[player.playerWeapon]), 100, 0);
			}
		}
	}

	public void handleGmaul() {
		if (player.npcIndex > 0 && NpcHandler.npcs[player.npcIndex] != null) {
			if (player.goodDistance(player.getX(), player.getY(), NpcHandler.npcs[player.npcIndex].getX(), NpcHandler.npcs[player.npcIndex].getY(), player.getCombatAssistant().getRequiredDistance())) {
				if (player.getCombatAssistant().checkSpecAmount(4153)) {
					boolean hit = Misc.random(player.getCombatAssistant().calcAtt()) > Misc.random(NpcHandler.npcs[player.npcIndex].defence);
					int damage = 0;
					if (hit) {
						damage = Misc.random(player.getCombatAssistant().meleeMaxHit());
						NpcHandler.npcs[player.npcIndex].HP -= damage;
						NpcHandler.npcs[player.npcIndex].hitDiff2 = damage;
						NpcHandler.npcs[player.npcIndex].hitUpdateRequired2 = true;
						NpcHandler.npcs[player.npcIndex].updateRequired = true;
						player.startAnimation(1667);
						player.gfx100(340);
					}
				}
			}
		} else if (player.playerIndex > 0) {
			final Client o = (Client) PlayerHandler.players[player.playerIndex];
			if (player.goodDistance(player.getX(), player.getY(), o.getX(), o.getY(), player
					.getCombatAssistant().getRequiredDistance())) {
				if (player.getCombatAssistant().checkReqs()) {
					if (player.getCombatAssistant().checkSpecAmount(4153)) {
						boolean hit = Misc.random(player.getCombatAssistant().calcAtt()) > Misc.random(o.getCombatAssistant().calcDef());
						int damage = 0;
						if (hit) {
							damage = Misc.random(player.getCombatAssistant().meleeMaxHit());
						}
						if (o.getPrayer().prayerActive[18] && System.currentTimeMillis() - o.protMeleeDelay > 1500) {
							damage *= .6;
						}
						if (o.playerLevel[3] - damage <= 0) {
							damage = o.playerLevel[3];
						}
						if (o.playerLevel[3] > 0) {
							o.handleHitMask(damage);
							player.startAnimation(1667);
							o.gfx100(337);
							o.dealDamage(damage);
						}
					}
				}
			}
		}
	}

	public double specAmount() {
		for (specialAttack SA : specialAttack.values()) {
			if (player.playerEquipment[player.playerWeapon] == SA.getWeapon()) {
				return SA.getSpecAmount();
			}
		}
		return 0;
	}
	
   public static void specialClicking(Player player2, int actionButtonId) {
	   switch (actionButtonId) {
		case 29188:
			if (player2.playerEquipment[player2.playerWeapon] == 1434) {
				player2.specBarId = 7636;
				player2.usingSpecial = !player2.usingSpecial;
				player2.getItemAssistant().updateSpecialBar();
			}
			break;

		case 29163:
			if (player2.playerEquipment[player2.playerWeapon] == 4587) {
				player2.specBarId = 7611;
				player2.usingSpecial = !player2.usingSpecial;
				player2.getItemAssistant().updateSpecialBar();
			}
			break;

		case 33033:
			if (player2.playerEquipment[player2.playerWeapon] == 4153) {
				player2.specBarId = 8505;
				player2.usingSpecial = !player2.usingSpecial;
				player2.getItemAssistant().updateSpecialBar();
			}
			break;

		case 29038:
			if (player2.playerEquipment[player2.playerWeapon] == 4153) {
				player2.specBarId = 7486;
				player2.getSpecials().handleGmaul();
				player2.usingSpecial = !player2.usingSpecial;
				player2.getItemAssistant().updateSpecialBar();
			}
			break;

		case 29063:
		if (player2.playerEquipment[player2.playerWeapon] == 1377) {
			if (player2.getCombatAssistant().checkSpecAmount(1377)) {
				player2.gfx0(246);
				player2.forcedChat("Raarrrrrgggggghhhhhhh!");
				player2.startAnimation(1056);
				player2.playerLevel[2] = player2.getLevelForXP(player2.playerXP[2]) + player2.getLevelForXP(player2.playerXP[2]) * 15 / 100;
				player2.getPlayerAssistant().refreshSkill(2);
				player2.getItemAssistant().updateSpecialBar();
			} else {
				player2.getPacketSender().sendMessage("You don't have the required special energy to use this attack.");
			}
		}
		break;

		case 48023:
			if (player2.playerEquipment[player2.playerWeapon] == 4151) {
				player2.specBarId = 12335;
				player2.usingSpecial = !player2.usingSpecial;
				player2.getItemAssistant().updateSpecialBar();
			}
			break;

		case 29138:
			if (player2.playerEquipment[player2.playerWeapon] == 1215 || player2.playerEquipment[player2.playerWeapon] == 1231 || player2.playerEquipment[player2.playerWeapon] == 5680 || player2.playerEquipment[player2.playerWeapon] == 5698) {
				player2.specBarId = 7586;
				player2.usingSpecial = !player2.usingSpecial;
				player2.getItemAssistant().updateSpecialBar();
			}
			break;

		case 29113:
			if (player2.playerEquipment[player2.playerWeapon] == 861 || player2.playerEquipment[player2.playerWeapon] == 859) {
				player2.specBarId = 7561;
				player2.usingSpecial = !player2.usingSpecial;
				player2.getItemAssistant().updateSpecialBar();
			}
			break;

		case 29238:
			player2.specBarId = 7686;
			player2.usingSpecial = !player2.usingSpecial;
			player2.getItemAssistant().updateSpecialBar();
			break;
	   }
   }

}
