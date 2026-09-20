import { IParty } from 'app/entities/party/party.model';
import { IQuote } from 'app/entities/quote/quote.model';
import { IQuoteItem } from 'app/entities/quote-item/quote-item.model';
import { IRoleType } from 'app/entities/role-type/role-type.model';

export interface IQuoteRole {
  id: number;
  quote?: IQuote | null;
  quoteItem?: IQuoteItem | null;
  party?: IParty | null;
  roleType?: IRoleType | null;
}

export type NewQuoteRole = Omit<IQuoteRole, 'id'> & { id: null };
