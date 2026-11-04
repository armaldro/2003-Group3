package enums;

/**
 * The stages an {@link entity.Order} passes through, from being taken by the
 * Waiter until it is paid or cancelled.
 *
 * <p>The brief (section 3.2) fixes the names READY, SERVED and PAID. Everything
 * else here is your design decision.
 *
 * <p>TODO 1: list the constants. Walk through the brief's example (section 3.3)
 * turn by turn and write down every state an order is in. Ask yourself:
 * <ul>
 *   <li>What is an order called from the moment the Waiter takes it until the
 *       Chef finishes the last preparation unit?</li>
 *   <li>The Chef may work on an order the Waiter took THIS turn. Does that need
 *       its own state, or does the answer to the previous question cover it?</li>
 *   <li>What happens to an order whose customer leaves without paying
 *       (brief step 5)? Does it need a state, and is it counted in revenue?</li>
 *   <li>The brief also has customer states such as READY_TO_PAY. Is that a state
 *       of the order or of the customer? Keep each fact in the class it
 *       belongs to.</li>
 * </ul>
 *
 * <p>TODO 2: decide whether the enum needs anything beyond constants.
 * <ul>
 *   <li>A readable label for the display (e.g. "Ready to serve")? The brief says
 *       the UI only displays state, so think about who owns the wording.</li>
 *   <li>A method such as {@code canMoveTo(OrderStatus next)}, so the legal order
 *       lives next to the states instead of in {@code Order}? Or is a simple
 *       enum with the checks in {@code Order} clearer? Be ready to justify it
 *       in the design discussion.</li>
 * </ul>
 *
 * <p>TODO 3: write the Javadoc for each constant (what state it means and who
 * moves an order into it).
 */
public enum OrderStatus {
    // TODO 1: replace this line with your constants, separated by commas and
    // ending with a semicolon only if you add fields or methods below.
}
