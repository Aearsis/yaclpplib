package cz.cuni.mff.yaclpplib.validator;

import cz.cuni.mff.yaclpplib.InvalidArgumentsException;

import java.util.ArrayList;
import java.util.stream.Stream;

public class ValidatorList {

    private final ArrayList<ValidatorList.ValidatorHolder> validators = new ArrayList<>();

    /**
     * Get all the exceptions from invalid validators
     */
    public Stream<InvalidArgumentsException> getExceptions() {
        return validators.stream()
                .filter((validatorHolder) -> !validatorHolder.validator.isValid())
                .flatMap((validatorHolder) -> Stream.of(validatorHolder.exceptionFactory.create()));
    }

    /**
     * Add a new validator.
     * @param validator the validator to be checked
     * @param exceptionFactory factory of the message to be thrown
     * @param <T> a type of the exception thrown
     */
    public <T extends InvalidArgumentsException> void add(Validator validator, ExceptionFactory<T> exceptionFactory) {
        validators.add(new ValidatorHolder(validator, exceptionFactory));
    }

    private static class ValidatorHolder {

        final Validator validator;
        final ExceptionFactory<? extends InvalidArgumentsException> exceptionFactory;

        <T extends InvalidArgumentsException> ValidatorHolder(Validator validator, ExceptionFactory<T> exceptionFactory) {
            this.validator = validator;
            this.exceptionFactory = exceptionFactory;
        }

    }
}
