package crack.firefly.com.System.mods.impl;

import java.awt.Color;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import crack.firefly.com.Firefly;
import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventRender2D;
import crack.firefly.com.System.event.impl.EventRenderScoreboard;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.HUDMod;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;
import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.Support.ColorUtils;
import crack.firefly.com.Support.GlUtils;
import crack.firefly.com.Support.render.RenderUtils;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;

public class ScoreboardMod extends HUDMod {

	private ScoreObjective objective;
	private boolean isFirstLoad;
	
	private BooleanSetting backgroundSetting = new BooleanSetting(TranslateText.BACKGROUND, this, true);
	private BooleanSetting numberSetting = new BooleanSetting(TranslateText.NUMBER, this, true);
	private BooleanSetting shadowSetting = new BooleanSetting(TranslateText.SHADOW, this, false);
	
	public ScoreboardMod() {
		super(TranslateText.SCOREBOARD, TranslateText.SCOREBOARD_DESCRIPTION);
	}

	@EventTarget
	public void onRender2D(EventRender2D event) {
		
		NanoVGManager nvg = Firefly.getInstance().getNanoVGManager();
		
		if(isFirstLoad) {
			isFirstLoad = false;
		}
		
		if(mc.isSingleplayer()) {
			objective = null;
		}
		
		if(objective != null) {
			
			Scoreboard scoreboard = objective.getScoreboard();
			Collection<Score> scores = scoreboard.getSortedScores(objective);
			List<Score> filteredScores = Lists.newArrayList(Iterables.filter(scores, p_apply_1_ -> p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#")));
			Collections.reverse(filteredScores);
			
			nvg.setupAndDraw(() -> {
				if(shadowSetting.isToggled()) {
					this.drawShadow(0, 0, this.getWidth() / this.getScale(), this.getHeight() / this.getScale(), 0);
				}
			});
			
			if(filteredScores.size() > 15) {
				scores = Lists.newArrayList(Iterables.skip(filteredScores, scores.size() - 15));
			} else {
				scores = filteredScores;
			}
			
			int maxWidth = fr.getStringWidth(objective.getDisplayName());
			
			for(Score score : scores) {
				
				ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
				String s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName());
				
				if(numberSetting.isToggled()) {
					 s +=  ": " + EnumChatFormatting.RED + score.getScorePoints();
				}
				
				maxWidth = Math.max(maxWidth, mc.fontRendererObj.getStringWidth(s));
			}
			
			int index = 0;
			
			GlUtils.startScale(this.getX(), this.getY(), this.getScale());
			
			for(Score score : scores) {
				
				index++;
				
				ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
				String playerName = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName());
				String scorePoints = EnumChatFormatting.RED + "" + score.getScorePoints();
				
				RenderUtils.drawRect(this.getX(), this.getY() + (index * fr.FONT_HEIGHT) + 1, maxWidth + 4, fr.FONT_HEIGHT, backgroundSetting.isToggled() ? ColorUtils.getColorByInt(1342177280) : new Color(0, 0, 0, 0));
				
				fr.drawString(playerName, this.getX() + 2, this.getY() + (index * fr.FONT_HEIGHT) + 1, 553648127);
				
				if(numberSetting.isToggled()) {
					fr.drawString(scorePoints, (this.getX() + 2 + maxWidth + 2) - fr.getStringWidth(scorePoints), this.getY() + (index * fr.FONT_HEIGHT) + 1, 553648127);
				}
				
				if(index == scores.size()) {
					
					String displayName = objective.getDisplayName();
					
					RenderUtils.drawRect(this.getX(), this.getY(), 2 + maxWidth + 2, fr.FONT_HEIGHT, backgroundSetting.isToggled() ? ColorUtils.getColorByInt(1610612736) : new Color(0, 0, 0, 0));
					RenderUtils.drawRect(this.getX(), this.getY() + fr.FONT_HEIGHT, 2 + maxWidth + 2, 1, backgroundSetting.isToggled() ? ColorUtils.getColorByInt(1610612736) : new Color(0, 0, 0, 0));

					fr.drawString(displayName, this.getX() + 2 + maxWidth / 2 - fr.getStringWidth(displayName) / 2, this.getY() + 1, 553648127);
				}
			}
			
			GlUtils.stopScale();
			
			int lastMaxWidth = maxWidth + 4;
			int lastMaxHeight = (index * fr.FONT_HEIGHT) + 10;
			
			this.setWidth(lastMaxWidth);
			this.setHeight(lastMaxHeight);
		}
	}
	
	@EventTarget
	public void onRenderScoreboard(EventRenderScoreboard event) {
		event.setCancelled(true);
		objective = event.getObjective();
	}
}
