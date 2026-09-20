export interface IPartyClassificationType {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewPartyClassificationType = Omit<IPartyClassificationType, 'id'> & { id: null };
