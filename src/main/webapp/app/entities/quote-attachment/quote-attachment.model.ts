import { IAttachment } from 'app/entities/attachment/attachment.model';
import { IQuote } from 'app/entities/quote/quote.model';

export interface IQuoteAttachment {
  id: number;
  name?: string | null;
  quote?: IQuote | null;
  attachment?: IAttachment | null;
}

export type NewQuoteAttachment = Omit<IQuoteAttachment, 'id'> & { id: null };
