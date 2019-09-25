package info.mastera.model;

/**
 * Enum of UserType that can use this program
 */
public enum UserType {
    /**
     * CUSTOMER - usual customer, user with minimal program features
     */
    CUSTOMER,
    /**
     * ENGINEER - user with program operations of changing jobs and using parts
     */
    ENGINEER,
    /**
     * RECEIVER - user with program operations of adding orders
     */
    RECEIVER,
    /**
     * ADMIN - user that cat use all possible features
     */
    ADMIN
}