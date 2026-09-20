import { IParty } from 'app/entities/party/party.model';
import { IPartyIdentificationType } from 'app/entities/party-identification-type/party-identification-type.model';

export interface IPartyIdentification {
  id: number;
  idValue?: string | null;
  party?: IParty | null;
  type?: IPartyIdentificationType | null;
}

export type NewPartyIdentification = Omit<IPartyIdentification, 'id'> & { id: null };
