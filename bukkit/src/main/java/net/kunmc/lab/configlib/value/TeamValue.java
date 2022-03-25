package net.kunmc.lab.configlib.value;

import net.kunmc.lab.commandlib.ArgumentBuilder;
import net.kunmc.lab.commandlib.argument.StringArgument;
import net.kunmc.lab.configlib.SingleValue;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * If you need completions with NewScoreboard, you should register it with scoreboard(Scoreboard) method.
 * When you use NewScoreboard, this class can't be deserialized. Therefore, a field must be qualified as transient at that time.
 */
public class TeamValue extends SingleValue<Team, TeamValue> {
    private transient Scoreboard scoreboard;

    public TeamValue() {
        this(null);
    }

    public TeamValue(Team value) {
        super(value);
        scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    }

    public @NotNull Scoreboard scoreboard() {
        return scoreboard;
    }

    public TeamValue scoreboard(@NotNull Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
        return this;
    }

    @Override
    protected void appendArgument(ArgumentBuilder builder) {
        builder.stringArgument("team", StringArgument.Type.WORD, suggestionBuilder -> {
            scoreboard.getTeams().stream()
                    .map(Team::getName)
                    .filter(s -> value == null || !s.equals(value.getName()))
                    .forEach(suggestionBuilder::suggest);
        });
    }

    @Override
    protected boolean isCorrectArgument(String entryName, List<Object> argument, CommandSender sender) {
        return scoreboard.getTeams().stream()
                .map(Team::getName)
                .anyMatch(s -> s.equals(argument.get(0)));
    }

    @Override
    protected String incorrectArgumentMessage(String entryName, List<Object> argument, CommandSender sender) {
        return argument.get(0) + "は存在しないチームです.";
    }

    @Override
    protected Team argumentToValue(List<Object> argument, CommandSender sender) {
        return scoreboard.getTeam(argument.get(0).toString());
    }

    @Override
    protected boolean validateOnSet(String entryName, Team newValue, CommandSender sender) {
        if (value == null) {
            return true;
        }

        return !value.getName().equals(newValue.getName());
    }

    @Override
    protected String invalidValueMessage(String entryName, Team newValue, CommandSender sender) {
        return newValue.getName() + "はすでに設定されているチームです.";
    }

    @Override
    protected String valueToString(Team team) {
        return team.getName();
    }
}
