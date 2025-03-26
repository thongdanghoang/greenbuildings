package greenbuildings.idp.validators;


public interface Validator<T extends ToBeValidated> {


    /**
     * Validate the whole object and set value to flag and map
     *
     * @param toValidate the object to be validated
     */
    void validate(T toValidate);
}
