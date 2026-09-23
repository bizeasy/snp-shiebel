/**
 * Flags a value that is nothing but a long number.
 *
 * Every free-text field carried over from snp21 - payment terms, delivery terms, the order
 * factory reference, the billing address - had picked up bare six-digit numbers instead of
 * wording, which then printed on orders and invoices. This is a hint, not a validator: no
 * rule in the application says a field of digits is invalid, so forms use it to warn rather
 * than to block saving.
 */
export function looksLikeId(value: unknown): boolean {
  return typeof value === 'string' && /^[0-9]{4,}$/.test(value.trim());
}
