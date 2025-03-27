package seedu.finclient.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.finclient.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.finclient.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.finclient.logic.parser.CliSyntax.PREFIX_TIMESTAMP;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import seedu.finclient.commons.core.index.Index;
import seedu.finclient.commons.exceptions.IllegalValueException;
import seedu.finclient.logic.commands.RemarkCommand;
import seedu.finclient.logic.parser.exceptions.ParseException;
import seedu.finclient.model.person.Remark;

/**
 * Parses input arguments and creates a new {@code RemarkCommand} object
 */
public class RemarkCommandParser implements Parser<RemarkCommand> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    /**
     * Parses the given {@code String} of arguments in the context of the {@code RemarkCommand}
     * and returns a {@code RemarkCommand} object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RemarkCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_REMARK, PREFIX_TIMESTAMP);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE), ive);
        }

        String remark = argMultimap.getValue(PREFIX_REMARK).orElse("").trim();
        Optional<LocalDateTime> timestamp = Optional.empty();

        if (argMultimap.getValue(PREFIX_TIMESTAMP).isPresent()) {
            String timestampStr = argMultimap.getValue(PREFIX_TIMESTAMP).get().trim();
            try {
                timestamp = Optional.of(LocalDateTime.parse(timestampStr, FORMATTER));
            } catch (DateTimeParseException e) {
                throw new ParseException("Invalid date/time format. Use yyyy-MM-dd HH:mm");
            }
        }
        return new RemarkCommand(index, new Remark(remark, timestamp));
    }
}
