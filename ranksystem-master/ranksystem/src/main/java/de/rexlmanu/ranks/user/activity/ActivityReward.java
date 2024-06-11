package de.rexlmanu.ranks.user.activity;

import de.exlll.configlib.Comment;
import java.util.List;

public record ActivityReward(
    @Comment({"Possible types: MILESTONE, RECURRING"}) RewardTrackingType type,
    long progress,
    @Comment(
            "Commands to execute when the reward is reached. %player% will be replaced with the player's name.")
        List<String> commands) {}
