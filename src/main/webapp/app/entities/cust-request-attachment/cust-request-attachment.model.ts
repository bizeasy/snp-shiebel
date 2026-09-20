import { IAttachment } from 'app/entities/attachment/attachment.model';
import { ICustRequest } from 'app/entities/cust-request/cust-request.model';

export interface ICustRequestAttachment {
  id: number;
  name?: string | null;
  custRequest?: ICustRequest | null;
  attachment?: IAttachment | null;
}

export type NewCustRequestAttachment = Omit<ICustRequestAttachment, 'id'> & { id: null };
