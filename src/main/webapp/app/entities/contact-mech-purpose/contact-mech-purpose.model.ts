export interface IContactMechPurpose {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewContactMechPurpose = Omit<IContactMechPurpose, 'id'> & { id: null };
