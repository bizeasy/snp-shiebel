export interface IPartyIdentificationType {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewPartyIdentificationType = Omit<IPartyIdentificationType, 'id'> & { id: null };
