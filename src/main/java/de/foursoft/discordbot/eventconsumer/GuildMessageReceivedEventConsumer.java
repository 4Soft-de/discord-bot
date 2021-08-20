package de.foursoft.discordbot.eventconsumer;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public abstract class GuildMessageReceivedEventConsumer extends EventConsumer<GuildMessageReceivedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuildMessageReceivedEventConsumer.class);

    private static final Pattern WHITESPACES_PATTERN = Pattern.compile("\\s+");
    private static final String PREFIX = "!";

    @Override
    public void execute(GuildMessageReceivedEvent event) {
        User user = event.getAuthor();
        Message message = event.getMessage();


        // Mentions will converted to ids, Markdown characters are included
        String contentRaw = message.getContentRaw();

        LOGGER.info("{}: {}", user.getAsTag(), contentRaw);

        if (!contentRaw.startsWith(PREFIX)) {
            return;
        }

        // split at whitespaces
        final String contentWithoutPrefix = contentRaw.substring(PREFIX.length()).trim();
        String[] args = WHITESPACES_PATTERN.split(contentWithoutPrefix);

        String userCommand = args[0].toLowerCase();  // args will never be empty due to the impl of split
        if (userCommand.isEmpty()) {
            return;
        }

        if (getName().equals(userCommand)){
            doExecute(event);
        }

    }

    protected abstract String getName();

    protected abstract void doExecute(GuildMessageReceivedEvent event);

    @Override
    public Class<GuildMessageReceivedEvent> getClassOfT() {
        return GuildMessageReceivedEvent.class;
    }
}