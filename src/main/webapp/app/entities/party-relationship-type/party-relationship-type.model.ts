import { IRoleType } from 'app/entities/role-type/role-type.model';

export interface IPartyRelationshipType {
  id: number;
  name?: string | null;
  description?: string | null;
  roleTypeFrom?: IRoleType | null;
  roleTypeTo?: IRoleType | null;
}

export type NewPartyRelationshipType = Omit<IPartyRelationshipType, 'id'> & { id: null };
