import { IAttachment } from 'app/entities/attachment/attachment.model';
import { ICustRequestItem } from 'app/entities/cust-request-item/cust-request-item.model';

export interface ICustRequestItemAttachment {
  id: number;
  name?: string | null;
  custRequestItem?: ICustRequestItem | null;
  attachment?: IAttachment | null;
}

export type NewCustRequestItemAttachment = Omit<ICustRequestItemAttachment, 'id'> & { id: null };
