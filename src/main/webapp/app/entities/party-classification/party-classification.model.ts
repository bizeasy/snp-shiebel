import dayjs from 'dayjs/esm';

import { IParty } from 'app/entities/party/party.model';
import { IPartyClassificationGroup } from 'app/entities/party-classification-group/party-classification-group.model';

export interface IPartyClassification {
  id: number;
  fromDate?: dayjs.Dayjs | null;
  thruDate?: dayjs.Dayjs | null;
  party?: IParty | null;
  classificationGroup?: IPartyClassificationGroup | null;
}

export type NewPartyClassification = Omit<IPartyClassification, 'id'> & { id: null };
