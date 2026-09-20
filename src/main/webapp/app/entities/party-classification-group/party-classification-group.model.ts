import { IPartyClassificationType } from 'app/entities/party-classification-type/party-classification-type.model';

export interface IPartyClassificationGroup {
  id: number;
  name?: string | null;
  description?: string | null;
  classificationType?: IPartyClassificationType | null;
}

export type NewPartyClassificationGroup = Omit<IPartyClassificationGroup, 'id'> & { id: null };
