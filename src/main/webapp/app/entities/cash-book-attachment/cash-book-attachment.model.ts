import { IAttachment } from 'app/entities/attachment/attachment.model';
import { ICashBook } from 'app/entities/cash-book/cash-book.model';

export interface ICashBookAttachment {
  id: number;
  name?: string | null;
  cashBook?: ICashBook | null;
  attachment?: IAttachment | null;
}

export type NewCashBookAttachment = Omit<ICashBookAttachment, 'id'> & { id: null };
